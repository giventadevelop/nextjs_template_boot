package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.repository.TicketTransactionRepository;
import com.nextjstemplate.service.criteria.TicketTransactionCriteria;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import com.nextjstemplate.service.mapper.TicketTransactionMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TicketTransaction} entities in the database.
 * The main input is a {@link TicketTransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketTransactionDTO} or a {@link Page} of {@link TicketTransactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketTransactionQueryService extends QueryService<TicketTransaction> {

    private final Logger log = LoggerFactory.getLogger(TicketTransactionQueryService.class);

    private final TicketTransactionRepository ticketTransactionRepository;

    private final TicketTransactionMapper ticketTransactionMapper;

    public TicketTransactionQueryService(
        TicketTransactionRepository ticketTransactionRepository,
        TicketTransactionMapper ticketTransactionMapper
    ) {
        this.ticketTransactionRepository = ticketTransactionRepository;
        this.ticketTransactionMapper = ticketTransactionMapper;
    }

    /**
     * Return a {@link List} of {@link TicketTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketTransactionDTO> findByCriteria(TicketTransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TicketTransaction> specification = createSpecification(criteria);
        return ticketTransactionMapper.toDto(ticketTransactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketTransactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketTransactionDTO> findByCriteria(TicketTransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TicketTransaction> specification = createSpecification(criteria);
        return ticketTransactionRepository.findAll(specification, page).map(ticketTransactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketTransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TicketTransaction> specification = createSpecification(criteria);
        return ticketTransactionRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketTransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TicketTransaction> createSpecification(TicketTransactionCriteria criteria) {
        Specification<TicketTransaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TicketTransaction_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), TicketTransaction_.email));
            }
            if (criteria.getTicketType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTicketType(), TicketTransaction_.ticketType));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), TicketTransaction_.quantity));
            }
            if (criteria.getPricePerUnit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPricePerUnit(), TicketTransaction_.pricePerUnit));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), TicketTransaction_.totalAmount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TicketTransaction_.status));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), TicketTransaction_.purchaseDate));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventId(), TicketTransaction_.eventId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserId(), TicketTransaction_.userId));
            }
        }
        return specification;
    }
}
