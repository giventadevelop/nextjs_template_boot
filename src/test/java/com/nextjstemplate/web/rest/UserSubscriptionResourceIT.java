package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserSubscription;
import com.nextjstemplate.repository.UserSubscriptionRepository;
import com.nextjstemplate.service.dto.UserSubscriptionDTO;
import com.nextjstemplate.service.mapper.UserSubscriptionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSubscriptionResourceIT {

    private static final String DEFAULT_STRIPE_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STRIPE_SUBSCRIPTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_SUBSCRIPTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STRIPE_PRICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_STRIPE_PRICE_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_STRIPE_CURRENT_PERIOD_END = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(0L),
        ZoneOffset.UTC
    );
    private static final ZonedDateTime UPDATED_STRIPE_CURRENT_PERIOD_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_STRIPE_CURRENT_PERIOD_END = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(-1L),
        ZoneOffset.UTC
    );

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSubscriptionMockMvc;

    private UserSubscription userSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createEntity(EntityManager em) {
        UserSubscription userSubscription = new UserSubscription()
            .stripeCustomerId(DEFAULT_STRIPE_CUSTOMER_ID)
            .stripeSubscriptionId(DEFAULT_STRIPE_SUBSCRIPTION_ID)
            .stripePriceId(DEFAULT_STRIPE_PRICE_ID)
            .stripeCurrentPeriodEnd(DEFAULT_STRIPE_CURRENT_PERIOD_END)
            .status(DEFAULT_STATUS);
        return userSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSubscription createUpdatedEntity(EntityManager em) {
        UserSubscription userSubscription = new UserSubscription()
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .stripeSubscriptionId(UPDATED_STRIPE_SUBSCRIPTION_ID)
            .stripePriceId(UPDATED_STRIPE_PRICE_ID)
            .stripeCurrentPeriodEnd(UPDATED_STRIPE_CURRENT_PERIOD_END)
            .status(UPDATED_STATUS);
        return userSubscription;
    }

    @BeforeEach
    public void initTest() {
        userSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createUserSubscription() throws Exception {
        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();
        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);
        restUserSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStripeCustomerId()).isEqualTo(DEFAULT_STRIPE_CUSTOMER_ID);
        assertThat(testUserSubscription.getStripeSubscriptionId()).isEqualTo(DEFAULT_STRIPE_SUBSCRIPTION_ID);
        assertThat(testUserSubscription.getStripePriceId()).isEqualTo(DEFAULT_STRIPE_PRICE_ID);
        assertThat(testUserSubscription.getStripeCurrentPeriodEnd()).isEqualTo(DEFAULT_STRIPE_CURRENT_PERIOD_END);
        assertThat(testUserSubscription.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUserSubscriptionWithExistingId() throws Exception {
        // Create the UserSubscription with an existing ID
        userSubscription.setId(1L);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        int databaseSizeBeforeCreate = userSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSubscriptionRepository.findAll().size();
        // set the field null
        userSubscription.setStatus(null);

        // Create the UserSubscription, which fails.
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        restUserSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSubscriptions() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].stripeCustomerId").value(hasItem(DEFAULT_STRIPE_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].stripeSubscriptionId").value(hasItem(DEFAULT_STRIPE_SUBSCRIPTION_ID)))
            .andExpect(jsonPath("$.[*].stripePriceId").value(hasItem(DEFAULT_STRIPE_PRICE_ID)))
            .andExpect(jsonPath("$.[*].stripeCurrentPeriodEnd").value(hasItem(sameInstant(DEFAULT_STRIPE_CURRENT_PERIOD_END))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get the userSubscription
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, userSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSubscription.getId().intValue()))
            .andExpect(jsonPath("$.stripeCustomerId").value(DEFAULT_STRIPE_CUSTOMER_ID))
            .andExpect(jsonPath("$.stripeSubscriptionId").value(DEFAULT_STRIPE_SUBSCRIPTION_ID))
            .andExpect(jsonPath("$.stripePriceId").value(DEFAULT_STRIPE_PRICE_ID))
            .andExpect(jsonPath("$.stripeCurrentPeriodEnd").value(sameInstant(DEFAULT_STRIPE_CURRENT_PERIOD_END)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getUserSubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        Long id = userSubscription.getId();

        defaultUserSubscriptionShouldBeFound("id.equals=" + id);
        defaultUserSubscriptionShouldNotBeFound("id.notEquals=" + id);

        defaultUserSubscriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserSubscriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultUserSubscriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserSubscriptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCustomerId equals to DEFAULT_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldBeFound("stripeCustomerId.equals=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the userSubscriptionList where stripeCustomerId equals to UPDATED_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldNotBeFound("stripeCustomerId.equals=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCustomerId in DEFAULT_STRIPE_CUSTOMER_ID or UPDATED_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldBeFound("stripeCustomerId.in=" + DEFAULT_STRIPE_CUSTOMER_ID + "," + UPDATED_STRIPE_CUSTOMER_ID);

        // Get all the userSubscriptionList where stripeCustomerId equals to UPDATED_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldNotBeFound("stripeCustomerId.in=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCustomerId is not null
        defaultUserSubscriptionShouldBeFound("stripeCustomerId.specified=true");

        // Get all the userSubscriptionList where stripeCustomerId is null
        defaultUserSubscriptionShouldNotBeFound("stripeCustomerId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCustomerId contains DEFAULT_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldBeFound("stripeCustomerId.contains=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the userSubscriptionList where stripeCustomerId contains UPDATED_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldNotBeFound("stripeCustomerId.contains=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCustomerId does not contain DEFAULT_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldNotBeFound("stripeCustomerId.doesNotContain=" + DEFAULT_STRIPE_CUSTOMER_ID);

        // Get all the userSubscriptionList where stripeCustomerId does not contain UPDATED_STRIPE_CUSTOMER_ID
        defaultUserSubscriptionShouldBeFound("stripeCustomerId.doesNotContain=" + UPDATED_STRIPE_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeSubscriptionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeSubscriptionId equals to DEFAULT_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldBeFound("stripeSubscriptionId.equals=" + DEFAULT_STRIPE_SUBSCRIPTION_ID);

        // Get all the userSubscriptionList where stripeSubscriptionId equals to UPDATED_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldNotBeFound("stripeSubscriptionId.equals=" + UPDATED_STRIPE_SUBSCRIPTION_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeSubscriptionIdIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeSubscriptionId in DEFAULT_STRIPE_SUBSCRIPTION_ID or UPDATED_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldBeFound(
            "stripeSubscriptionId.in=" + DEFAULT_STRIPE_SUBSCRIPTION_ID + "," + UPDATED_STRIPE_SUBSCRIPTION_ID
        );

        // Get all the userSubscriptionList where stripeSubscriptionId equals to UPDATED_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldNotBeFound("stripeSubscriptionId.in=" + UPDATED_STRIPE_SUBSCRIPTION_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeSubscriptionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeSubscriptionId is not null
        defaultUserSubscriptionShouldBeFound("stripeSubscriptionId.specified=true");

        // Get all the userSubscriptionList where stripeSubscriptionId is null
        defaultUserSubscriptionShouldNotBeFound("stripeSubscriptionId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeSubscriptionIdContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeSubscriptionId contains DEFAULT_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldBeFound("stripeSubscriptionId.contains=" + DEFAULT_STRIPE_SUBSCRIPTION_ID);

        // Get all the userSubscriptionList where stripeSubscriptionId contains UPDATED_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldNotBeFound("stripeSubscriptionId.contains=" + UPDATED_STRIPE_SUBSCRIPTION_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeSubscriptionIdNotContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeSubscriptionId does not contain DEFAULT_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldNotBeFound("stripeSubscriptionId.doesNotContain=" + DEFAULT_STRIPE_SUBSCRIPTION_ID);

        // Get all the userSubscriptionList where stripeSubscriptionId does not contain UPDATED_STRIPE_SUBSCRIPTION_ID
        defaultUserSubscriptionShouldBeFound("stripeSubscriptionId.doesNotContain=" + UPDATED_STRIPE_SUBSCRIPTION_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripePriceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripePriceId equals to DEFAULT_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldBeFound("stripePriceId.equals=" + DEFAULT_STRIPE_PRICE_ID);

        // Get all the userSubscriptionList where stripePriceId equals to UPDATED_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldNotBeFound("stripePriceId.equals=" + UPDATED_STRIPE_PRICE_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripePriceIdIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripePriceId in DEFAULT_STRIPE_PRICE_ID or UPDATED_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldBeFound("stripePriceId.in=" + DEFAULT_STRIPE_PRICE_ID + "," + UPDATED_STRIPE_PRICE_ID);

        // Get all the userSubscriptionList where stripePriceId equals to UPDATED_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldNotBeFound("stripePriceId.in=" + UPDATED_STRIPE_PRICE_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripePriceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripePriceId is not null
        defaultUserSubscriptionShouldBeFound("stripePriceId.specified=true");

        // Get all the userSubscriptionList where stripePriceId is null
        defaultUserSubscriptionShouldNotBeFound("stripePriceId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripePriceIdContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripePriceId contains DEFAULT_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldBeFound("stripePriceId.contains=" + DEFAULT_STRIPE_PRICE_ID);

        // Get all the userSubscriptionList where stripePriceId contains UPDATED_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldNotBeFound("stripePriceId.contains=" + UPDATED_STRIPE_PRICE_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripePriceIdNotContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripePriceId does not contain DEFAULT_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldNotBeFound("stripePriceId.doesNotContain=" + DEFAULT_STRIPE_PRICE_ID);

        // Get all the userSubscriptionList where stripePriceId does not contain UPDATED_STRIPE_PRICE_ID
        defaultUserSubscriptionShouldBeFound("stripePriceId.doesNotContain=" + UPDATED_STRIPE_PRICE_ID);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd equals to DEFAULT_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.equals=" + DEFAULT_STRIPE_CURRENT_PERIOD_END);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd equals to UPDATED_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.equals=" + UPDATED_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd in DEFAULT_STRIPE_CURRENT_PERIOD_END or UPDATED_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound(
            "stripeCurrentPeriodEnd.in=" + DEFAULT_STRIPE_CURRENT_PERIOD_END + "," + UPDATED_STRIPE_CURRENT_PERIOD_END
        );

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd equals to UPDATED_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.in=" + UPDATED_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is not null
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.specified=true");

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is null
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is greater than or equal to DEFAULT_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.greaterThanOrEqual=" + DEFAULT_STRIPE_CURRENT_PERIOD_END);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is greater than or equal to UPDATED_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.greaterThanOrEqual=" + UPDATED_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is less than or equal to DEFAULT_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.lessThanOrEqual=" + DEFAULT_STRIPE_CURRENT_PERIOD_END);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is less than or equal to SMALLER_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.lessThanOrEqual=" + SMALLER_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsLessThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is less than DEFAULT_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.lessThan=" + DEFAULT_STRIPE_CURRENT_PERIOD_END);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is less than UPDATED_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.lessThan=" + UPDATED_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStripeCurrentPeriodEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is greater than DEFAULT_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldNotBeFound("stripeCurrentPeriodEnd.greaterThan=" + DEFAULT_STRIPE_CURRENT_PERIOD_END);

        // Get all the userSubscriptionList where stripeCurrentPeriodEnd is greater than SMALLER_STRIPE_CURRENT_PERIOD_END
        defaultUserSubscriptionShouldBeFound("stripeCurrentPeriodEnd.greaterThan=" + SMALLER_STRIPE_CURRENT_PERIOD_END);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where status equals to DEFAULT_STATUS
        defaultUserSubscriptionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the userSubscriptionList where status equals to UPDATED_STATUS
        defaultUserSubscriptionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultUserSubscriptionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the userSubscriptionList where status equals to UPDATED_STATUS
        defaultUserSubscriptionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where status is not null
        defaultUserSubscriptionShouldBeFound("status.specified=true");

        // Get all the userSubscriptionList where status is null
        defaultUserSubscriptionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where status contains DEFAULT_STATUS
        defaultUserSubscriptionShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the userSubscriptionList where status contains UPDATED_STATUS
        defaultUserSubscriptionShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        // Get all the userSubscriptionList where status does not contain DEFAULT_STATUS
        defaultUserSubscriptionShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the userSubscriptionList where status does not contain UPDATED_STATUS
        defaultUserSubscriptionShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllUserSubscriptionsByUserProfileIsEqualToSomething() throws Exception {
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            userSubscriptionRepository.saveAndFlush(userSubscription);
            userProfile = UserProfileResourceIT.createEntity(em);
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(userProfile);
        em.flush();
        userSubscription.setUserProfile(userProfile);
        userSubscriptionRepository.saveAndFlush(userSubscription);
        Long userProfileId = userProfile.getId();
        // Get all the userSubscriptionList where userProfile equals to userProfileId
        defaultUserSubscriptionShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the userSubscriptionList where userProfile equals to (userProfileId + 1)
        defaultUserSubscriptionShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserSubscriptionShouldBeFound(String filter) throws Exception {
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].stripeCustomerId").value(hasItem(DEFAULT_STRIPE_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].stripeSubscriptionId").value(hasItem(DEFAULT_STRIPE_SUBSCRIPTION_ID)))
            .andExpect(jsonPath("$.[*].stripePriceId").value(hasItem(DEFAULT_STRIPE_PRICE_ID)))
            .andExpect(jsonPath("$.[*].stripeCurrentPeriodEnd").value(hasItem(sameInstant(DEFAULT_STRIPE_CURRENT_PERIOD_END))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserSubscriptionShouldNotBeFound(String filter) throws Exception {
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserSubscription() throws Exception {
        // Get the userSubscription
        restUserSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription
        UserSubscription updatedUserSubscription = userSubscriptionRepository.findById(userSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSubscription are not directly saved in db
        em.detach(updatedUserSubscription);
        updatedUserSubscription
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .stripeSubscriptionId(UPDATED_STRIPE_SUBSCRIPTION_ID)
            .stripePriceId(UPDATED_STRIPE_PRICE_ID)
            .stripeCurrentPeriodEnd(UPDATED_STRIPE_CURRENT_PERIOD_END)
            .status(UPDATED_STATUS);
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(updatedUserSubscription);

        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testUserSubscription.getStripeSubscriptionId()).isEqualTo(UPDATED_STRIPE_SUBSCRIPTION_ID);
        assertThat(testUserSubscription.getStripePriceId()).isEqualTo(UPDATED_STRIPE_PRICE_ID);
        assertThat(testUserSubscription.getStripeCurrentPeriodEnd()).isEqualTo(UPDATED_STRIPE_CURRENT_PERIOD_END);
        assertThat(testUserSubscription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .stripePriceId(UPDATED_STRIPE_PRICE_ID)
            .stripeCurrentPeriodEnd(UPDATED_STRIPE_CURRENT_PERIOD_END);

        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testUserSubscription.getStripeSubscriptionId()).isEqualTo(DEFAULT_STRIPE_SUBSCRIPTION_ID);
        assertThat(testUserSubscription.getStripePriceId()).isEqualTo(UPDATED_STRIPE_PRICE_ID);
        assertThat(testUserSubscription.getStripeCurrentPeriodEnd()).isEqualTo(UPDATED_STRIPE_CURRENT_PERIOD_END);
        assertThat(testUserSubscription.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserSubscriptionWithPatch() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();

        // Update the userSubscription using partial update
        UserSubscription partialUpdatedUserSubscription = new UserSubscription();
        partialUpdatedUserSubscription.setId(userSubscription.getId());

        partialUpdatedUserSubscription
            .stripeCustomerId(UPDATED_STRIPE_CUSTOMER_ID)
            .stripeSubscriptionId(UPDATED_STRIPE_SUBSCRIPTION_ID)
            .stripePriceId(UPDATED_STRIPE_PRICE_ID)
            .stripeCurrentPeriodEnd(UPDATED_STRIPE_CURRENT_PERIOD_END)
            .status(UPDATED_STATUS);

        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSubscription))
            )
            .andExpect(status().isOk());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        UserSubscription testUserSubscription = userSubscriptionList.get(userSubscriptionList.size() - 1);
        assertThat(testUserSubscription.getStripeCustomerId()).isEqualTo(UPDATED_STRIPE_CUSTOMER_ID);
        assertThat(testUserSubscription.getStripeSubscriptionId()).isEqualTo(UPDATED_STRIPE_SUBSCRIPTION_ID);
        assertThat(testUserSubscription.getStripePriceId()).isEqualTo(UPDATED_STRIPE_PRICE_ID);
        assertThat(testUserSubscription.getStripeCurrentPeriodEnd()).isEqualTo(UPDATED_STRIPE_CURRENT_PERIOD_END);
        assertThat(testUserSubscription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSubscription() throws Exception {
        int databaseSizeBeforeUpdate = userSubscriptionRepository.findAll().size();
        userSubscription.setId(longCount.incrementAndGet());

        // Create the UserSubscription
        UserSubscriptionDTO userSubscriptionDTO = userSubscriptionMapper.toDto(userSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSubscription in the database
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSubscription() throws Exception {
        // Initialize the database
        userSubscriptionRepository.saveAndFlush(userSubscription);

        int databaseSizeBeforeDelete = userSubscriptionRepository.findAll().size();

        // Delete the userSubscription
        restUserSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAll();
        assertThat(userSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
