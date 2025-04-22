package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static com.nextjstemplate.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.repository.TicketTransactionRepository;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import com.nextjstemplate.service.mapper.TicketTransactionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TicketTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketTransactionResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TICKET_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE_PER_UNIT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PURCHASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PURCHASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PURCHASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ticket-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketTransactionRepository ticketTransactionRepository;

    @Autowired
    private TicketTransactionMapper ticketTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketTransactionMockMvc;

    private TicketTransaction ticketTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketTransaction createEntity(EntityManager em) {
        TicketTransaction ticketTransaction = new TicketTransaction()
            .email(DEFAULT_EMAIL)
            .ticketType(DEFAULT_TICKET_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .status(DEFAULT_STATUS)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .eventId(DEFAULT_EVENT_ID)
            .userId(DEFAULT_USER_ID);
        return ticketTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TicketTransaction createUpdatedEntity(EntityManager em) {
        TicketTransaction ticketTransaction = new TicketTransaction()
            .email(UPDATED_EMAIL)
            .ticketType(UPDATED_TICKET_TYPE)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .eventId(UPDATED_EVENT_ID)
            .userId(UPDATED_USER_ID);
        return ticketTransaction;
    }

    @BeforeEach
    public void initTest() {
        ticketTransaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTicketTransaction() throws Exception {
        int databaseSizeBeforeCreate = ticketTransactionRepository.findAll().size();
        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);
        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTicketTransaction.getTicketType()).isEqualTo(DEFAULT_TICKET_TYPE);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testTicketTransaction.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
        assertThat(testTicketTransaction.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createTicketTransactionWithExistingId() throws Exception {
        // Create the TicketTransaction with an existing ID
        ticketTransaction.setId(1L);
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        int databaseSizeBeforeCreate = ticketTransactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setEmail(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTicketTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setTicketType(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setQuantity(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setPricePerUnit(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setTotalAmount(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setStatus(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPurchaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setPurchaseDate(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketTransactionRepository.findAll().size();
        // set the field null
        ticketTransaction.setEventId(null);

        // Create the TicketTransaction, which fails.
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        restTicketTransactionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTicketTransactions() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].ticketType").value(hasItem(DEFAULT_TICKET_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(sameInstant(DEFAULT_PURCHASE_DATE))))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get the ticketTransaction
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, ticketTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticketTransaction.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.ticketType").value(DEFAULT_TICKET_TYPE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.pricePerUnit").value(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.purchaseDate").value(sameInstant(DEFAULT_PURCHASE_DATE)))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getTicketTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        Long id = ticketTransaction.getId();

        defaultTicketTransactionShouldBeFound("id.equals=" + id);
        defaultTicketTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultTicketTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email equals to DEFAULT_EMAIL
        defaultTicketTransactionShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email equals to UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTicketTransactionShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the ticketTransactionList where email equals to UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email is not null
        defaultTicketTransactionShouldBeFound("email.specified=true");

        // Get all the ticketTransactionList where email is null
        defaultTicketTransactionShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email contains DEFAULT_EMAIL
        defaultTicketTransactionShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email contains UPDATED_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where email does not contain DEFAULT_EMAIL
        defaultTicketTransactionShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the ticketTransactionList where email does not contain UPDATED_EMAIL
        defaultTicketTransactionShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where ticketType equals to DEFAULT_TICKET_TYPE
        defaultTicketTransactionShouldBeFound("ticketType.equals=" + DEFAULT_TICKET_TYPE);

        // Get all the ticketTransactionList where ticketType equals to UPDATED_TICKET_TYPE
        defaultTicketTransactionShouldNotBeFound("ticketType.equals=" + UPDATED_TICKET_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where ticketType in DEFAULT_TICKET_TYPE or UPDATED_TICKET_TYPE
        defaultTicketTransactionShouldBeFound("ticketType.in=" + DEFAULT_TICKET_TYPE + "," + UPDATED_TICKET_TYPE);

        // Get all the ticketTransactionList where ticketType equals to UPDATED_TICKET_TYPE
        defaultTicketTransactionShouldNotBeFound("ticketType.in=" + UPDATED_TICKET_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where ticketType is not null
        defaultTicketTransactionShouldBeFound("ticketType.specified=true");

        // Get all the ticketTransactionList where ticketType is null
        defaultTicketTransactionShouldNotBeFound("ticketType.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where ticketType contains DEFAULT_TICKET_TYPE
        defaultTicketTransactionShouldBeFound("ticketType.contains=" + DEFAULT_TICKET_TYPE);

        // Get all the ticketTransactionList where ticketType contains UPDATED_TICKET_TYPE
        defaultTicketTransactionShouldNotBeFound("ticketType.contains=" + UPDATED_TICKET_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTicketTypeNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where ticketType does not contain DEFAULT_TICKET_TYPE
        defaultTicketTransactionShouldNotBeFound("ticketType.doesNotContain=" + DEFAULT_TICKET_TYPE);

        // Get all the ticketTransactionList where ticketType does not contain UPDATED_TICKET_TYPE
        defaultTicketTransactionShouldBeFound("ticketType.doesNotContain=" + UPDATED_TICKET_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity equals to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the ticketTransactionList where quantity equals to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is not null
        defaultTicketTransactionShouldBeFound("quantity.specified=true");

        // Get all the ticketTransactionList where quantity is null
        defaultTicketTransactionShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is less than or equal to SMALLER_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is less than DEFAULT_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is less than UPDATED_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where quantity is greater than DEFAULT_QUANTITY
        defaultTicketTransactionShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the ticketTransactionList where quantity is greater than SMALLER_QUANTITY
        defaultTicketTransactionShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit equals to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit in DEFAULT_PRICE_PER_UNIT or UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit equals to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is not null
        defaultTicketTransactionShouldBeFound("pricePerUnit.specified=true");

        // Get all the ticketTransactionList where pricePerUnit is null
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is greater than or equal to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is greater than or equal to UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is less than or equal to DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is less than or equal to SMALLER_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is less than DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is less than UPDATED_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where pricePerUnit is greater than DEFAULT_PRICE_PER_UNIT
        defaultTicketTransactionShouldNotBeFound("pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT);

        // Get all the ticketTransactionList where pricePerUnit is greater than SMALLER_PRICE_PER_UNIT
        defaultTicketTransactionShouldBeFound("pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is not null
        defaultTicketTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the ticketTransactionList where totalAmount is null
        defaultTicketTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultTicketTransactionShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the ticketTransactionList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultTicketTransactionShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status equals to DEFAULT_STATUS
        defaultTicketTransactionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status equals to UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTicketTransactionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ticketTransactionList where status equals to UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status is not null
        defaultTicketTransactionShouldBeFound("status.specified=true");

        // Get all the ticketTransactionList where status is null
        defaultTicketTransactionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status contains DEFAULT_STATUS
        defaultTicketTransactionShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status contains UPDATED_STATUS
        defaultTicketTransactionShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where status does not contain DEFAULT_STATUS
        defaultTicketTransactionShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the ticketTransactionList where status does not contain UPDATED_STATUS
        defaultTicketTransactionShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate equals to DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.equals=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.equals=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate in DEFAULT_PURCHASE_DATE or UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.in=" + DEFAULT_PURCHASE_DATE + "," + UPDATED_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate equals to UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.in=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is not null
        defaultTicketTransactionShouldBeFound("purchaseDate.specified=true");

        // Get all the ticketTransactionList where purchaseDate is null
        defaultTicketTransactionShouldNotBeFound("purchaseDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is greater than or equal to DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate is greater than or equal to UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.greaterThanOrEqual=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is less than or equal to DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.lessThanOrEqual=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate is less than or equal to SMALLER_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.lessThanOrEqual=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is less than DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.lessThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate is less than UPDATED_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.lessThan=" + UPDATED_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByPurchaseDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where purchaseDate is greater than DEFAULT_PURCHASE_DATE
        defaultTicketTransactionShouldNotBeFound("purchaseDate.greaterThan=" + DEFAULT_PURCHASE_DATE);

        // Get all the ticketTransactionList where purchaseDate is greater than SMALLER_PURCHASE_DATE
        defaultTicketTransactionShouldBeFound("purchaseDate.greaterThan=" + SMALLER_PURCHASE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where eventId equals to DEFAULT_EVENT_ID
        defaultTicketTransactionShouldBeFound("eventId.equals=" + DEFAULT_EVENT_ID);

        // Get all the ticketTransactionList where eventId equals to UPDATED_EVENT_ID
        defaultTicketTransactionShouldNotBeFound("eventId.equals=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where eventId in DEFAULT_EVENT_ID or UPDATED_EVENT_ID
        defaultTicketTransactionShouldBeFound("eventId.in=" + DEFAULT_EVENT_ID + "," + UPDATED_EVENT_ID);

        // Get all the ticketTransactionList where eventId equals to UPDATED_EVENT_ID
        defaultTicketTransactionShouldNotBeFound("eventId.in=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where eventId is not null
        defaultTicketTransactionShouldBeFound("eventId.specified=true");

        // Get all the ticketTransactionList where eventId is null
        defaultTicketTransactionShouldNotBeFound("eventId.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIdContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where eventId contains DEFAULT_EVENT_ID
        defaultTicketTransactionShouldBeFound("eventId.contains=" + DEFAULT_EVENT_ID);

        // Get all the ticketTransactionList where eventId contains UPDATED_EVENT_ID
        defaultTicketTransactionShouldNotBeFound("eventId.contains=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByEventIdNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where eventId does not contain DEFAULT_EVENT_ID
        defaultTicketTransactionShouldNotBeFound("eventId.doesNotContain=" + DEFAULT_EVENT_ID);

        // Get all the ticketTransactionList where eventId does not contain UPDATED_EVENT_ID
        defaultTicketTransactionShouldBeFound("eventId.doesNotContain=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where userId equals to DEFAULT_USER_ID
        defaultTicketTransactionShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the ticketTransactionList where userId equals to UPDATED_USER_ID
        defaultTicketTransactionShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultTicketTransactionShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the ticketTransactionList where userId equals to UPDATED_USER_ID
        defaultTicketTransactionShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where userId is not null
        defaultTicketTransactionShouldBeFound("userId.specified=true");

        // Get all the ticketTransactionList where userId is null
        defaultTicketTransactionShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where userId contains DEFAULT_USER_ID
        defaultTicketTransactionShouldBeFound("userId.contains=" + DEFAULT_USER_ID);

        // Get all the ticketTransactionList where userId contains UPDATED_USER_ID
        defaultTicketTransactionShouldNotBeFound("userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTicketTransactionsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        // Get all the ticketTransactionList where userId does not contain DEFAULT_USER_ID
        defaultTicketTransactionShouldNotBeFound("userId.doesNotContain=" + DEFAULT_USER_ID);

        // Get all the ticketTransactionList where userId does not contain UPDATED_USER_ID
        defaultTicketTransactionShouldBeFound("userId.doesNotContain=" + UPDATED_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketTransactionShouldBeFound(String filter) throws Exception {
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticketTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].ticketType").value(hasItem(DEFAULT_TICKET_TYPE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(sameInstant(DEFAULT_PURCHASE_DATE))))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)));

        // Check, that the count call also returns 1
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketTransactionShouldNotBeFound(String filter) throws Exception {
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicketTransaction() throws Exception {
        // Get the ticketTransaction
        restTicketTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction
        TicketTransaction updatedTicketTransaction = ticketTransactionRepository.findById(ticketTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTicketTransaction are not directly saved in db
        em.detach(updatedTicketTransaction);
        updatedTicketTransaction
            .email(UPDATED_EMAIL)
            .ticketType(UPDATED_TICKET_TYPE)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .eventId(UPDATED_EVENT_ID)
            .userId(UPDATED_USER_ID);
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(updatedTicketTransaction);

        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTicketTransaction.getTicketType()).isEqualTo(UPDATED_TICKET_TYPE);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testTicketTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction using partial update
        TicketTransaction partialUpdatedTicketTransaction = new TicketTransaction();
        partialUpdatedTicketTransaction.setId(ticketTransaction.getId());

        partialUpdatedTicketTransaction
            .email(UPDATED_EMAIL)
            .ticketType(UPDATED_TICKET_TYPE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .eventId(UPDATED_EVENT_ID)
            .userId(UPDATED_USER_ID);

        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTicketTransaction.getTicketType()).isEqualTo(UPDATED_TICKET_TYPE);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testTicketTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateTicketTransactionWithPatch() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();

        // Update the ticketTransaction using partial update
        TicketTransaction partialUpdatedTicketTransaction = new TicketTransaction();
        partialUpdatedTicketTransaction.setId(ticketTransaction.getId());

        partialUpdatedTicketTransaction
            .email(UPDATED_EMAIL)
            .ticketType(UPDATED_TICKET_TYPE)
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .status(UPDATED_STATUS)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .eventId(UPDATED_EVENT_ID)
            .userId(UPDATED_USER_ID);

        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicketTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicketTransaction))
            )
            .andExpect(status().isOk());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
        TicketTransaction testTicketTransaction = ticketTransactionList.get(ticketTransactionList.size() - 1);
        assertThat(testTicketTransaction.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTicketTransaction.getTicketType()).isEqualTo(UPDATED_TICKET_TYPE);
        assertThat(testTicketTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testTicketTransaction.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testTicketTransaction.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTicketTransaction.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicketTransaction.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testTicketTransaction.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testTicketTransaction.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticketTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicketTransaction() throws Exception {
        int databaseSizeBeforeUpdate = ticketTransactionRepository.findAll().size();
        ticketTransaction.setId(longCount.incrementAndGet());

        // Create the TicketTransaction
        TicketTransactionDTO ticketTransactionDTO = ticketTransactionMapper.toDto(ticketTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticketTransactionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TicketTransaction in the database
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicketTransaction() throws Exception {
        // Initialize the database
        ticketTransactionRepository.saveAndFlush(ticketTransaction);

        int databaseSizeBeforeDelete = ticketTransactionRepository.findAll().size();

        // Delete the ticketTransaction
        restTicketTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticketTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TicketTransaction> ticketTransactionList = ticketTransactionRepository.findAll();
        assertThat(ticketTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
