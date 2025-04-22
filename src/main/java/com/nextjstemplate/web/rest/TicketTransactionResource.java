package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.TicketTransactionRepository;
import com.nextjstemplate.service.TicketTransactionQueryService;
import com.nextjstemplate.service.TicketTransactionService;
import com.nextjstemplate.service.criteria.TicketTransactionCriteria;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
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
 * REST controller for managing {@link com.nextjstemplate.domain.TicketTransaction}.
 */
@RestController
@RequestMapping("/api/ticket-transactions")
public class TicketTransactionResource {

    private final Logger log = LoggerFactory.getLogger(TicketTransactionResource.class);

    private static final String ENTITY_NAME = "ticketTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketTransactionService ticketTransactionService;

    private final TicketTransactionRepository ticketTransactionRepository;

    private final TicketTransactionQueryService ticketTransactionQueryService;

    public TicketTransactionResource(
        TicketTransactionService ticketTransactionService,
        TicketTransactionRepository ticketTransactionRepository,
        TicketTransactionQueryService ticketTransactionQueryService
    ) {
        this.ticketTransactionService = ticketTransactionService;
        this.ticketTransactionRepository = ticketTransactionRepository;
        this.ticketTransactionQueryService = ticketTransactionQueryService;
    }

    /**
     * {@code POST  /ticket-transactions} : Create a new ticketTransaction.
     *
     * @param ticketTransactionDTO the ticketTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketTransactionDTO, or with status {@code 400 (Bad Request)} if the ticketTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TicketTransactionDTO> createTicketTransaction(@Valid @RequestBody TicketTransactionDTO ticketTransactionDTO)
        throws URISyntaxException {
        log.debug("REST request to save TicketTransaction : {}", ticketTransactionDTO);
        if (ticketTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketTransactionDTO result = ticketTransactionService.save(ticketTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/ticket-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticket-transactions/:id} : Updates an existing ticketTransaction.
     *
     * @param id the id of the ticketTransactionDTO to save.
     * @param ticketTransactionDTO the ticketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the ticketTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketTransactionDTO> updateTicketTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TicketTransactionDTO ticketTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TicketTransaction : {}, {}", id, ticketTransactionDTO);
        if (ticketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TicketTransactionDTO result = ticketTransactionService.update(ticketTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ticket-transactions/:id} : Partial updates given fields of an existing ticketTransaction, field will ignore if it is null
     *
     * @param id the id of the ticketTransactionDTO to save.
     * @param ticketTransactionDTO the ticketTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the ticketTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ticketTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ticketTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TicketTransactionDTO> partialUpdateTicketTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TicketTransactionDTO ticketTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TicketTransaction partially : {}, {}", id, ticketTransactionDTO);
        if (ticketTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticketTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TicketTransactionDTO> result = ticketTransactionService.partialUpdate(ticketTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ticket-transactions} : get all the ticketTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TicketTransactionDTO>> getAllTicketTransactions(
        TicketTransactionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TicketTransactions by criteria: {}", criteria);

        Page<TicketTransactionDTO> page = ticketTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ticket-transactions/count} : count all the ticketTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTicketTransactions(TicketTransactionCriteria criteria) {
        log.debug("REST request to count TicketTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ticket-transactions/:id} : get the "id" ticketTransaction.
     *
     * @param id the id of the ticketTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketTransactionDTO> getTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to get TicketTransaction : {}", id);
        Optional<TicketTransactionDTO> ticketTransactionDTO = ticketTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketTransactionDTO);
    }

    /**
     * {@code DELETE  /ticket-transactions/:id} : delete the "id" ticketTransaction.
     *
     * @param id the id of the ticketTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicketTransaction(@PathVariable Long id) {
        log.debug("REST request to delete TicketTransaction : {}", id);
        ticketTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
