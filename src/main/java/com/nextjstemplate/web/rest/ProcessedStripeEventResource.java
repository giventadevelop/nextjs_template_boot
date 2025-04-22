package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.ProcessedStripeEventRepository;
import com.nextjstemplate.service.ProcessedStripeEventQueryService;
import com.nextjstemplate.service.ProcessedStripeEventService;
import com.nextjstemplate.service.criteria.ProcessedStripeEventCriteria;
import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.ProcessedStripeEvent}.
 */
@RestController
@RequestMapping("/api/processed-stripe-events")
public class ProcessedStripeEventResource {

    private final Logger log = LoggerFactory.getLogger(ProcessedStripeEventResource.class);

    private static final String ENTITY_NAME = "processedStripeEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessedStripeEventService processedStripeEventService;

    private final ProcessedStripeEventRepository processedStripeEventRepository;

    private final ProcessedStripeEventQueryService processedStripeEventQueryService;

    public ProcessedStripeEventResource(
        ProcessedStripeEventService processedStripeEventService,
        ProcessedStripeEventRepository processedStripeEventRepository,
        ProcessedStripeEventQueryService processedStripeEventQueryService
    ) {
        this.processedStripeEventService = processedStripeEventService;
        this.processedStripeEventRepository = processedStripeEventRepository;
        this.processedStripeEventQueryService = processedStripeEventQueryService;
    }

    /**
     * {@code POST  /processed-stripe-events} : Create a new processedStripeEvent.
     *
     * @param processedStripeEventDTO the processedStripeEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processedStripeEventDTO, or with status {@code 400 (Bad Request)} if the processedStripeEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProcessedStripeEventDTO> createProcessedStripeEvent(
        @Valid @RequestBody ProcessedStripeEventDTO processedStripeEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProcessedStripeEvent : {}", processedStripeEventDTO);
        if (processedStripeEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new processedStripeEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessedStripeEventDTO result = processedStripeEventService.save(processedStripeEventDTO);
        return ResponseEntity
            .created(new URI("/api/processed-stripe-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /processed-stripe-events/:id} : Updates an existing processedStripeEvent.
     *
     * @param id the id of the processedStripeEventDTO to save.
     * @param processedStripeEventDTO the processedStripeEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processedStripeEventDTO,
     * or with status {@code 400 (Bad Request)} if the processedStripeEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processedStripeEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProcessedStripeEventDTO> updateProcessedStripeEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProcessedStripeEventDTO processedStripeEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessedStripeEvent : {}, {}", id, processedStripeEventDTO);
        if (processedStripeEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processedStripeEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processedStripeEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessedStripeEventDTO result = processedStripeEventService.update(processedStripeEventDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processedStripeEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /processed-stripe-events/:id} : Partial updates given fields of an existing processedStripeEvent, field will ignore if it is null
     *
     * @param id the id of the processedStripeEventDTO to save.
     * @param processedStripeEventDTO the processedStripeEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processedStripeEventDTO,
     * or with status {@code 400 (Bad Request)} if the processedStripeEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processedStripeEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processedStripeEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessedStripeEventDTO> partialUpdateProcessedStripeEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProcessedStripeEventDTO processedStripeEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessedStripeEvent partially : {}, {}", id, processedStripeEventDTO);
        if (processedStripeEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processedStripeEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processedStripeEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessedStripeEventDTO> result = processedStripeEventService.partialUpdate(processedStripeEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processedStripeEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /processed-stripe-events} : get all the processedStripeEvents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processedStripeEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProcessedStripeEventDTO>> getAllProcessedStripeEvents(
        ProcessedStripeEventCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ProcessedStripeEvents by criteria: {}", criteria);

        Page<ProcessedStripeEventDTO> page = processedStripeEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /processed-stripe-events/count} : count all the processedStripeEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProcessedStripeEvents(ProcessedStripeEventCriteria criteria) {
        log.debug("REST request to count ProcessedStripeEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(processedStripeEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /processed-stripe-events/:id} : get the "id" processedStripeEvent.
     *
     * @param id the id of the processedStripeEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processedStripeEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProcessedStripeEventDTO> getProcessedStripeEvent(@PathVariable Long id) {
        log.debug("REST request to get ProcessedStripeEvent : {}", id);
        Optional<ProcessedStripeEventDTO> processedStripeEventDTO = processedStripeEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processedStripeEventDTO);
    }

    /**
     * {@code DELETE  /processed-stripe-events/:id} : delete the "id" processedStripeEvent.
     *
     * @param id the id of the processedStripeEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessedStripeEvent(@PathVariable Long id) {
        log.debug("REST request to delete ProcessedStripeEvent : {}", id);
        processedStripeEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
