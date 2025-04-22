package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.ProcessedStripeEvent;
import com.nextjstemplate.repository.ProcessedStripeEventRepository;
import com.nextjstemplate.service.criteria.ProcessedStripeEventCriteria;
import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import com.nextjstemplate.service.mapper.ProcessedStripeEventMapper;
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
 * Service for executing complex queries for {@link ProcessedStripeEvent} entities in the database.
 * The main input is a {@link ProcessedStripeEventCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProcessedStripeEventDTO} or a {@link Page} of {@link ProcessedStripeEventDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcessedStripeEventQueryService extends QueryService<ProcessedStripeEvent> {

    private final Logger log = LoggerFactory.getLogger(ProcessedStripeEventQueryService.class);

    private final ProcessedStripeEventRepository processedStripeEventRepository;

    private final ProcessedStripeEventMapper processedStripeEventMapper;

    public ProcessedStripeEventQueryService(
        ProcessedStripeEventRepository processedStripeEventRepository,
        ProcessedStripeEventMapper processedStripeEventMapper
    ) {
        this.processedStripeEventRepository = processedStripeEventRepository;
        this.processedStripeEventMapper = processedStripeEventMapper;
    }

    /**
     * Return a {@link List} of {@link ProcessedStripeEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessedStripeEventDTO> findByCriteria(ProcessedStripeEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProcessedStripeEvent> specification = createSpecification(criteria);
        return processedStripeEventMapper.toDto(processedStripeEventRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProcessedStripeEventDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessedStripeEventDTO> findByCriteria(ProcessedStripeEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProcessedStripeEvent> specification = createSpecification(criteria);
        return processedStripeEventRepository.findAll(specification, page).map(processedStripeEventMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessedStripeEventCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProcessedStripeEvent> specification = createSpecification(criteria);
        return processedStripeEventRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessedStripeEventCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProcessedStripeEvent> createSpecification(ProcessedStripeEventCriteria criteria) {
        Specification<ProcessedStripeEvent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProcessedStripeEvent_.id));
            }
            if (criteria.getEventId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventId(), ProcessedStripeEvent_.eventId));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ProcessedStripeEvent_.type));
            }
            if (criteria.getProcessedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessedAt(), ProcessedStripeEvent_.processedAt));
            }
        }
        return specification;
    }
}
