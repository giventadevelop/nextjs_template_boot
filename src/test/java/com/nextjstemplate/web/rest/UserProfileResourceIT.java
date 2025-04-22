package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.domain.UserSubscription;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.mapper.UserProfileMapper;
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
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userId(DEFAULT_USER_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .notes(DEFAULT_NOTES)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return userProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return userProfile;
    }

    @BeforeEach
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUserProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserProfile.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testUserProfile.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testUserProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserProfile.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserProfile.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testUserProfile.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testUserProfile.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testUserProfile.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserProfile.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setUserId(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setCreatedAt(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setUpdatedAt(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));
    }

    @Test
    @Transactional
    void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)));
    }

    @Test
    @Transactional
    void getUserProfilesByIdFiltering() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        Long id = userProfile.getId();

        defaultUserProfileShouldBeFound("id.equals=" + id);
        defaultUserProfileShouldNotBeFound("id.notEquals=" + id);

        defaultUserProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultUserProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserProfileShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId equals to DEFAULT_USER_ID
        defaultUserProfileShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userProfileList where userId equals to UPDATED_USER_ID
        defaultUserProfileShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserProfileShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userProfileList where userId equals to UPDATED_USER_ID
        defaultUserProfileShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId is not null
        defaultUserProfileShouldBeFound("userId.specified=true");

        // Get all the userProfileList where userId is null
        defaultUserProfileShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId contains DEFAULT_USER_ID
        defaultUserProfileShouldBeFound("userId.contains=" + DEFAULT_USER_ID);

        // Get all the userProfileList where userId contains UPDATED_USER_ID
        defaultUserProfileShouldNotBeFound("userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where userId does not contain DEFAULT_USER_ID
        defaultUserProfileShouldNotBeFound("userId.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the userProfileList where userId does not contain UPDATED_USER_ID
        defaultUserProfileShouldBeFound("userId.doesNotContain=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserProfilesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName equals to DEFAULT_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName equals to UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the userProfileList where firstName equals to UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName is not null
        defaultUserProfileShouldBeFound("firstName.specified=true");

        // Get all the userProfileList where firstName is null
        defaultUserProfileShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName contains DEFAULT_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName contains UPDATED_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where firstName does not contain DEFAULT_FIRST_NAME
        defaultUserProfileShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the userProfileList where firstName does not contain UPDATED_FIRST_NAME
        defaultUserProfileShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName equals to DEFAULT_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName equals to UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the userProfileList where lastName equals to UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName is not null
        defaultUserProfileShouldBeFound("lastName.specified=true");

        // Get all the userProfileList where lastName is null
        defaultUserProfileShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName contains DEFAULT_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName contains UPDATED_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where lastName does not contain DEFAULT_LAST_NAME
        defaultUserProfileShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the userProfileList where lastName does not contain UPDATED_LAST_NAME
        defaultUserProfileShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllUserProfilesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email equals to DEFAULT_EMAIL
        defaultUserProfileShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the userProfileList where email equals to UPDATED_EMAIL
        defaultUserProfileShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultUserProfileShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the userProfileList where email equals to UPDATED_EMAIL
        defaultUserProfileShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email is not null
        defaultUserProfileShouldBeFound("email.specified=true");

        // Get all the userProfileList where email is null
        defaultUserProfileShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByEmailContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email contains DEFAULT_EMAIL
        defaultUserProfileShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the userProfileList where email contains UPDATED_EMAIL
        defaultUserProfileShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where email does not contain DEFAULT_EMAIL
        defaultUserProfileShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the userProfileList where email does not contain UPDATED_EMAIL
        defaultUserProfileShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone equals to DEFAULT_PHONE
        defaultUserProfileShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the userProfileList where phone equals to UPDATED_PHONE
        defaultUserProfileShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultUserProfileShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the userProfileList where phone equals to UPDATED_PHONE
        defaultUserProfileShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone is not null
        defaultUserProfileShouldBeFound("phone.specified=true");

        // Get all the userProfileList where phone is null
        defaultUserProfileShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone contains DEFAULT_PHONE
        defaultUserProfileShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the userProfileList where phone contains UPDATED_PHONE
        defaultUserProfileShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where phone does not contain DEFAULT_PHONE
        defaultUserProfileShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the userProfileList where phone does not contain UPDATED_PHONE
        defaultUserProfileShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultUserProfileShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userProfileList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserProfileShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultUserProfileShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the userProfileList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultUserProfileShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine1 is not null
        defaultUserProfileShouldBeFound("addressLine1.specified=true");

        // Get all the userProfileList where addressLine1 is null
        defaultUserProfileShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultUserProfileShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userProfileList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultUserProfileShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultUserProfileShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the userProfileList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultUserProfileShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultUserProfileShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userProfileList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserProfileShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultUserProfileShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the userProfileList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultUserProfileShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine2 is not null
        defaultUserProfileShouldBeFound("addressLine2.specified=true");

        // Get all the userProfileList where addressLine2 is null
        defaultUserProfileShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultUserProfileShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userProfileList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultUserProfileShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserProfilesByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultUserProfileShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the userProfileList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultUserProfileShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city equals to DEFAULT_CITY
        defaultUserProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultUserProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the userProfileList where city equals to UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city is not null
        defaultUserProfileShouldBeFound("city.specified=true");

        // Get all the userProfileList where city is null
        defaultUserProfileShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByCityContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city contains DEFAULT_CITY
        defaultUserProfileShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the userProfileList where city contains UPDATED_CITY
        defaultUserProfileShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where city does not contain DEFAULT_CITY
        defaultUserProfileShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the userProfileList where city does not contain UPDATED_CITY
        defaultUserProfileShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state equals to DEFAULT_STATE
        defaultUserProfileShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the userProfileList where state equals to UPDATED_STATE
        defaultUserProfileShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state in DEFAULT_STATE or UPDATED_STATE
        defaultUserProfileShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the userProfileList where state equals to UPDATED_STATE
        defaultUserProfileShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state is not null
        defaultUserProfileShouldBeFound("state.specified=true");

        // Get all the userProfileList where state is null
        defaultUserProfileShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByStateContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state contains DEFAULT_STATE
        defaultUserProfileShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the userProfileList where state contains UPDATED_STATE
        defaultUserProfileShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where state does not contain DEFAULT_STATE
        defaultUserProfileShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the userProfileList where state does not contain UPDATED_STATE
        defaultUserProfileShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByZipCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where zipCode equals to DEFAULT_ZIP_CODE
        defaultUserProfileShouldBeFound("zipCode.equals=" + DEFAULT_ZIP_CODE);

        // Get all the userProfileList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserProfileShouldNotBeFound("zipCode.equals=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByZipCodeIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where zipCode in DEFAULT_ZIP_CODE or UPDATED_ZIP_CODE
        defaultUserProfileShouldBeFound("zipCode.in=" + DEFAULT_ZIP_CODE + "," + UPDATED_ZIP_CODE);

        // Get all the userProfileList where zipCode equals to UPDATED_ZIP_CODE
        defaultUserProfileShouldNotBeFound("zipCode.in=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByZipCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where zipCode is not null
        defaultUserProfileShouldBeFound("zipCode.specified=true");

        // Get all the userProfileList where zipCode is null
        defaultUserProfileShouldNotBeFound("zipCode.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByZipCodeContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where zipCode contains DEFAULT_ZIP_CODE
        defaultUserProfileShouldBeFound("zipCode.contains=" + DEFAULT_ZIP_CODE);

        // Get all the userProfileList where zipCode contains UPDATED_ZIP_CODE
        defaultUserProfileShouldNotBeFound("zipCode.contains=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByZipCodeNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where zipCode does not contain DEFAULT_ZIP_CODE
        defaultUserProfileShouldNotBeFound("zipCode.doesNotContain=" + DEFAULT_ZIP_CODE);

        // Get all the userProfileList where zipCode does not contain UPDATED_ZIP_CODE
        defaultUserProfileShouldBeFound("zipCode.doesNotContain=" + UPDATED_ZIP_CODE);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country equals to DEFAULT_COUNTRY
        defaultUserProfileShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country equals to UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultUserProfileShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the userProfileList where country equals to UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country is not null
        defaultUserProfileShouldBeFound("country.specified=true");

        // Get all the userProfileList where country is null
        defaultUserProfileShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByCountryContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country contains DEFAULT_COUNTRY
        defaultUserProfileShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country contains UPDATED_COUNTRY
        defaultUserProfileShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where country does not contain DEFAULT_COUNTRY
        defaultUserProfileShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the userProfileList where country does not contain UPDATED_COUNTRY
        defaultUserProfileShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllUserProfilesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where notes equals to DEFAULT_NOTES
        defaultUserProfileShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the userProfileList where notes equals to UPDATED_NOTES
        defaultUserProfileShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllUserProfilesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultUserProfileShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the userProfileList where notes equals to UPDATED_NOTES
        defaultUserProfileShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllUserProfilesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where notes is not null
        defaultUserProfileShouldBeFound("notes.specified=true");

        // Get all the userProfileList where notes is null
        defaultUserProfileShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByNotesContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where notes contains DEFAULT_NOTES
        defaultUserProfileShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the userProfileList where notes contains UPDATED_NOTES
        defaultUserProfileShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllUserProfilesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where notes does not contain DEFAULT_NOTES
        defaultUserProfileShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the userProfileList where notes does not contain UPDATED_NOTES
        defaultUserProfileShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt equals to DEFAULT_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the userProfileList where createdAt equals to UPDATED_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the userProfileList where createdAt equals to UPDATED_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt is not null
        defaultUserProfileShouldBeFound("createdAt.specified=true");

        // Get all the userProfileList where createdAt is null
        defaultUserProfileShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userProfileList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the userProfileList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt is less than DEFAULT_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the userProfileList where createdAt is less than UPDATED_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where createdAt is greater than DEFAULT_CREATED_AT
        defaultUserProfileShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the userProfileList where createdAt is greater than SMALLER_CREATED_AT
        defaultUserProfileShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the userProfileList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the userProfileList where updatedAt equals to UPDATED_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt is not null
        defaultUserProfileShouldBeFound("updatedAt.specified=true");

        // Get all the userProfileList where updatedAt is null
        defaultUserProfileShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt is greater than or equal to DEFAULT_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userProfileList where updatedAt is greater than or equal to UPDATED_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt is less than or equal to DEFAULT_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT);

        // Get all the userProfileList where updatedAt is less than or equal to SMALLER_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt is less than DEFAULT_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.lessThan=" + DEFAULT_UPDATED_AT);

        // Get all the userProfileList where updatedAt is less than UPDATED_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.lessThan=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList where updatedAt is greater than DEFAULT_UPDATED_AT
        defaultUserProfileShouldNotBeFound("updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);

        // Get all the userProfileList where updatedAt is greater than SMALLER_UPDATED_AT
        defaultUserProfileShouldBeFound("updatedAt.greaterThan=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllUserProfilesByUserSubscriptionIsEqualToSomething() throws Exception {
        UserSubscription userSubscription;
        if (TestUtil.findAll(em, UserSubscription.class).isEmpty()) {
            userProfileRepository.saveAndFlush(userProfile);
            userSubscription = UserSubscriptionResourceIT.createEntity(em);
        } else {
            userSubscription = TestUtil.findAll(em, UserSubscription.class).get(0);
        }
        em.persist(userSubscription);
        em.flush();
        userProfile.setUserSubscription(userSubscription);
        userSubscription.setUserProfile(userProfile);
        userProfileRepository.saveAndFlush(userProfile);
        Long userSubscriptionId = userSubscription.getId();
        // Get all the userProfileList where userSubscription equals to userSubscriptionId
        defaultUserProfileShouldBeFound("userSubscriptionId.equals=" + userSubscriptionId);

        // Get all the userProfileList where userSubscription equals to (userSubscriptionId + 1)
        defaultUserProfileShouldNotBeFound("userSubscriptionId.equals=" + (userSubscriptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserProfileShouldBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))));

        // Check, that the count call also returns 1
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserProfileShouldNotBeFound(String filter) throws Exception {
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
        updatedUserProfile
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserProfile.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserProfile.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testUserProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserProfile.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserProfile.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testUserProfile.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserProfile.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testUserProfile.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserProfile.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .country(UPDATED_COUNTRY)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserProfile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserProfile.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserProfile.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testUserProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testUserProfile.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testUserProfile.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testUserProfile.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserProfile.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testUserProfile.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserProfile.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .userId(UPDATED_USER_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .notes(UPDATED_NOTES)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserProfile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUserProfile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUserProfile.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUserProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserProfile.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testUserProfile.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testUserProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testUserProfile.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testUserProfile.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testUserProfile.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testUserProfile.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testUserProfile.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserProfile.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Delete the userProfile
        restUserProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
