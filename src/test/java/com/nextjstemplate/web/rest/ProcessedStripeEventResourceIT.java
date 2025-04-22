package com.nextjstemplate.web.rest;

import static com.nextjstemplate.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nextjstemplate.IntegrationTest;
import com.nextjstemplate.domain.ProcessedStripeEvent;
import com.nextjstemplate.repository.ProcessedStripeEventRepository;
import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import com.nextjstemplate.service.mapper.ProcessedStripeEventMapper;
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
 * Integration tests for the {@link ProcessedStripeEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessedStripeEventResourceIT {

    private static final String DEFAULT_EVENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PROCESSED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PROCESSED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PROCESSED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/processed-stripe-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessedStripeEventRepository processedStripeEventRepository;

    @Autowired
    private ProcessedStripeEventMapper processedStripeEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessedStripeEventMockMvc;

    private ProcessedStripeEvent processedStripeEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessedStripeEvent createEntity(EntityManager em) {
        ProcessedStripeEvent processedStripeEvent = new ProcessedStripeEvent()
            .eventId(DEFAULT_EVENT_ID)
            .type(DEFAULT_TYPE)
            .processedAt(DEFAULT_PROCESSED_AT);
        return processedStripeEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessedStripeEvent createUpdatedEntity(EntityManager em) {
        ProcessedStripeEvent processedStripeEvent = new ProcessedStripeEvent()
            .eventId(UPDATED_EVENT_ID)
            .type(UPDATED_TYPE)
            .processedAt(UPDATED_PROCESSED_AT);
        return processedStripeEvent;
    }

    @BeforeEach
    public void initTest() {
        processedStripeEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeCreate = processedStripeEventRepository.findAll().size();
        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);
        restProcessedStripeEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessedStripeEvent testProcessedStripeEvent = processedStripeEventList.get(processedStripeEventList.size() - 1);
        assertThat(testProcessedStripeEvent.getEventId()).isEqualTo(DEFAULT_EVENT_ID);
        assertThat(testProcessedStripeEvent.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProcessedStripeEvent.getProcessedAt()).isEqualTo(DEFAULT_PROCESSED_AT);
    }

    @Test
    @Transactional
    void createProcessedStripeEventWithExistingId() throws Exception {
        // Create the ProcessedStripeEvent with an existing ID
        processedStripeEvent.setId(1L);
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        int databaseSizeBeforeCreate = processedStripeEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessedStripeEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = processedStripeEventRepository.findAll().size();
        // set the field null
        processedStripeEvent.setEventId(null);

        // Create the ProcessedStripeEvent, which fails.
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        restProcessedStripeEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = processedStripeEventRepository.findAll().size();
        // set the field null
        processedStripeEvent.setType(null);

        // Create the ProcessedStripeEvent, which fails.
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        restProcessedStripeEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProcessedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = processedStripeEventRepository.findAll().size();
        // set the field null
        processedStripeEvent.setProcessedAt(null);

        // Create the ProcessedStripeEvent, which fails.
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        restProcessedStripeEventMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEvents() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processedStripeEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(sameInstant(DEFAULT_PROCESSED_AT))));
    }

    @Test
    @Transactional
    void getProcessedStripeEvent() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get the processedStripeEvent
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL_ID, processedStripeEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processedStripeEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.processedAt").value(sameInstant(DEFAULT_PROCESSED_AT)));
    }

    @Test
    @Transactional
    void getProcessedStripeEventsByIdFiltering() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        Long id = processedStripeEvent.getId();

        defaultProcessedStripeEventShouldBeFound("id.equals=" + id);
        defaultProcessedStripeEventShouldNotBeFound("id.notEquals=" + id);

        defaultProcessedStripeEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProcessedStripeEventShouldNotBeFound("id.greaterThan=" + id);

        defaultProcessedStripeEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProcessedStripeEventShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByEventIdIsEqualToSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where eventId equals to DEFAULT_EVENT_ID
        defaultProcessedStripeEventShouldBeFound("eventId.equals=" + DEFAULT_EVENT_ID);

        // Get all the processedStripeEventList where eventId equals to UPDATED_EVENT_ID
        defaultProcessedStripeEventShouldNotBeFound("eventId.equals=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByEventIdIsInShouldWork() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where eventId in DEFAULT_EVENT_ID or UPDATED_EVENT_ID
        defaultProcessedStripeEventShouldBeFound("eventId.in=" + DEFAULT_EVENT_ID + "," + UPDATED_EVENT_ID);

        // Get all the processedStripeEventList where eventId equals to UPDATED_EVENT_ID
        defaultProcessedStripeEventShouldNotBeFound("eventId.in=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByEventIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where eventId is not null
        defaultProcessedStripeEventShouldBeFound("eventId.specified=true");

        // Get all the processedStripeEventList where eventId is null
        defaultProcessedStripeEventShouldNotBeFound("eventId.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByEventIdContainsSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where eventId contains DEFAULT_EVENT_ID
        defaultProcessedStripeEventShouldBeFound("eventId.contains=" + DEFAULT_EVENT_ID);

        // Get all the processedStripeEventList where eventId contains UPDATED_EVENT_ID
        defaultProcessedStripeEventShouldNotBeFound("eventId.contains=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByEventIdNotContainsSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where eventId does not contain DEFAULT_EVENT_ID
        defaultProcessedStripeEventShouldNotBeFound("eventId.doesNotContain=" + DEFAULT_EVENT_ID);

        // Get all the processedStripeEventList where eventId does not contain UPDATED_EVENT_ID
        defaultProcessedStripeEventShouldBeFound("eventId.doesNotContain=" + UPDATED_EVENT_ID);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where type equals to DEFAULT_TYPE
        defaultProcessedStripeEventShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the processedStripeEventList where type equals to UPDATED_TYPE
        defaultProcessedStripeEventShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultProcessedStripeEventShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the processedStripeEventList where type equals to UPDATED_TYPE
        defaultProcessedStripeEventShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where type is not null
        defaultProcessedStripeEventShouldBeFound("type.specified=true");

        // Get all the processedStripeEventList where type is null
        defaultProcessedStripeEventShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByTypeContainsSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where type contains DEFAULT_TYPE
        defaultProcessedStripeEventShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the processedStripeEventList where type contains UPDATED_TYPE
        defaultProcessedStripeEventShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where type does not contain DEFAULT_TYPE
        defaultProcessedStripeEventShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the processedStripeEventList where type does not contain UPDATED_TYPE
        defaultProcessedStripeEventShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt equals to DEFAULT_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.equals=" + DEFAULT_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt equals to UPDATED_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.equals=" + UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsInShouldWork() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt in DEFAULT_PROCESSED_AT or UPDATED_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.in=" + DEFAULT_PROCESSED_AT + "," + UPDATED_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt equals to UPDATED_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.in=" + UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt is not null
        defaultProcessedStripeEventShouldBeFound("processedAt.specified=true");

        // Get all the processedStripeEventList where processedAt is null
        defaultProcessedStripeEventShouldNotBeFound("processedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt is greater than or equal to DEFAULT_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.greaterThanOrEqual=" + DEFAULT_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt is greater than or equal to UPDATED_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.greaterThanOrEqual=" + UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt is less than or equal to DEFAULT_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.lessThanOrEqual=" + DEFAULT_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt is less than or equal to SMALLER_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.lessThanOrEqual=" + SMALLER_PROCESSED_AT);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt is less than DEFAULT_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.lessThan=" + DEFAULT_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt is less than UPDATED_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.lessThan=" + UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void getAllProcessedStripeEventsByProcessedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        // Get all the processedStripeEventList where processedAt is greater than DEFAULT_PROCESSED_AT
        defaultProcessedStripeEventShouldNotBeFound("processedAt.greaterThan=" + DEFAULT_PROCESSED_AT);

        // Get all the processedStripeEventList where processedAt is greater than SMALLER_PROCESSED_AT
        defaultProcessedStripeEventShouldBeFound("processedAt.greaterThan=" + SMALLER_PROCESSED_AT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcessedStripeEventShouldBeFound(String filter) throws Exception {
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processedStripeEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].processedAt").value(hasItem(sameInstant(DEFAULT_PROCESSED_AT))));

        // Check, that the count call also returns 1
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcessedStripeEventShouldNotBeFound(String filter) throws Exception {
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcessedStripeEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProcessedStripeEvent() throws Exception {
        // Get the processedStripeEvent
        restProcessedStripeEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcessedStripeEvent() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();

        // Update the processedStripeEvent
        ProcessedStripeEvent updatedProcessedStripeEvent = processedStripeEventRepository
            .findById(processedStripeEvent.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProcessedStripeEvent are not directly saved in db
        em.detach(updatedProcessedStripeEvent);
        updatedProcessedStripeEvent.eventId(UPDATED_EVENT_ID).type(UPDATED_TYPE).processedAt(UPDATED_PROCESSED_AT);
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(updatedProcessedStripeEvent);

        restProcessedStripeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processedStripeEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
        ProcessedStripeEvent testProcessedStripeEvent = processedStripeEventList.get(processedStripeEventList.size() - 1);
        assertThat(testProcessedStripeEvent.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testProcessedStripeEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProcessedStripeEvent.getProcessedAt()).isEqualTo(UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void putNonExistingProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processedStripeEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessedStripeEventWithPatch() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();

        // Update the processedStripeEvent using partial update
        ProcessedStripeEvent partialUpdatedProcessedStripeEvent = new ProcessedStripeEvent();
        partialUpdatedProcessedStripeEvent.setId(processedStripeEvent.getId());

        partialUpdatedProcessedStripeEvent.eventId(UPDATED_EVENT_ID).type(UPDATED_TYPE).processedAt(UPDATED_PROCESSED_AT);

        restProcessedStripeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessedStripeEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessedStripeEvent))
            )
            .andExpect(status().isOk());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
        ProcessedStripeEvent testProcessedStripeEvent = processedStripeEventList.get(processedStripeEventList.size() - 1);
        assertThat(testProcessedStripeEvent.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testProcessedStripeEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProcessedStripeEvent.getProcessedAt()).isEqualTo(UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void fullUpdateProcessedStripeEventWithPatch() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();

        // Update the processedStripeEvent using partial update
        ProcessedStripeEvent partialUpdatedProcessedStripeEvent = new ProcessedStripeEvent();
        partialUpdatedProcessedStripeEvent.setId(processedStripeEvent.getId());

        partialUpdatedProcessedStripeEvent.eventId(UPDATED_EVENT_ID).type(UPDATED_TYPE).processedAt(UPDATED_PROCESSED_AT);

        restProcessedStripeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessedStripeEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessedStripeEvent))
            )
            .andExpect(status().isOk());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
        ProcessedStripeEvent testProcessedStripeEvent = processedStripeEventList.get(processedStripeEventList.size() - 1);
        assertThat(testProcessedStripeEvent.getEventId()).isEqualTo(UPDATED_EVENT_ID);
        assertThat(testProcessedStripeEvent.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProcessedStripeEvent.getProcessedAt()).isEqualTo(UPDATED_PROCESSED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processedStripeEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessedStripeEvent() throws Exception {
        int databaseSizeBeforeUpdate = processedStripeEventRepository.findAll().size();
        processedStripeEvent.setId(longCount.incrementAndGet());

        // Create the ProcessedStripeEvent
        ProcessedStripeEventDTO processedStripeEventDTO = processedStripeEventMapper.toDto(processedStripeEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessedStripeEventMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processedStripeEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessedStripeEvent in the database
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcessedStripeEvent() throws Exception {
        // Initialize the database
        processedStripeEventRepository.saveAndFlush(processedStripeEvent);

        int databaseSizeBeforeDelete = processedStripeEventRepository.findAll().size();

        // Delete the processedStripeEvent
        restProcessedStripeEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, processedStripeEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessedStripeEvent> processedStripeEventList = processedStripeEventRepository.findAll();
        assertThat(processedStripeEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
