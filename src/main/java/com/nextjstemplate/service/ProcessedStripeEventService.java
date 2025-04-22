package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.ProcessedStripeEvent}.
 */
public interface ProcessedStripeEventService {
    /**
     * Save a processedStripeEvent.
     *
     * @param processedStripeEventDTO the entity to save.
     * @return the persisted entity.
     */
    ProcessedStripeEventDTO save(ProcessedStripeEventDTO processedStripeEventDTO);

    /**
     * Updates a processedStripeEvent.
     *
     * @param processedStripeEventDTO the entity to update.
     * @return the persisted entity.
     */
    ProcessedStripeEventDTO update(ProcessedStripeEventDTO processedStripeEventDTO);

    /**
     * Partially updates a processedStripeEvent.
     *
     * @param processedStripeEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProcessedStripeEventDTO> partialUpdate(ProcessedStripeEventDTO processedStripeEventDTO);

    /**
     * Get all the processedStripeEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProcessedStripeEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" processedStripeEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProcessedStripeEventDTO> findOne(Long id);

    /**
     * Delete the "id" processedStripeEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
