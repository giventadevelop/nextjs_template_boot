package com.nextjstemplate.service;

import com.nextjstemplate.domain.*; // for static metamodels
import com.nextjstemplate.domain.UserTask;
import com.nextjstemplate.repository.UserTaskRepository;
import com.nextjstemplate.service.criteria.UserTaskCriteria;
import com.nextjstemplate.service.dto.UserTaskDTO;
import com.nextjstemplate.service.mapper.UserTaskMapper;
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
 * Service for executing complex queries for {@link UserTask} entities in the database.
 * The main input is a {@link UserTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserTaskDTO} or a {@link Page} of {@link UserTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserTaskQueryService extends QueryService<UserTask> {

    private final Logger log = LoggerFactory.getLogger(UserTaskQueryService.class);

    private final UserTaskRepository userTaskRepository;

    private final UserTaskMapper userTaskMapper;

    public UserTaskQueryService(UserTaskRepository userTaskRepository, UserTaskMapper userTaskMapper) {
        this.userTaskRepository = userTaskRepository;
        this.userTaskMapper = userTaskMapper;
    }

    /**
     * Return a {@link List} of {@link UserTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserTaskDTO> findByCriteria(UserTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserTask> specification = createSpecification(criteria);
        return userTaskMapper.toDto(userTaskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserTaskDTO> findByCriteria(UserTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserTask> specification = createSpecification(criteria);
        return userTaskRepository.findAll(specification, page).map(userTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserTask> specification = createSpecification(criteria);
        return userTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link UserTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserTask> createSpecification(UserTaskCriteria criteria) {
        Specification<UserTask> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserTask_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), UserTask_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), UserTask_.description));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), UserTask_.status));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPriority(), UserTask_.priority));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), UserTask_.dueDate));
            }
            if (criteria.getCompleted() != null) {
                specification = specification.and(buildSpecification(criteria.getCompleted(), UserTask_.completed));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserId(), UserTask_.userId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserTask_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserTask_.updatedAt));
            }
        }
        return specification;
    }
}
