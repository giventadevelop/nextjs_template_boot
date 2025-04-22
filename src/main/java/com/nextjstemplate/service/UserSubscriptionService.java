package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.UserSubscriptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.UserSubscription}.
 */
public interface UserSubscriptionService {
    /**
     * Save a userSubscription.
     *
     * @param userSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    UserSubscriptionDTO save(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Updates a userSubscription.
     *
     * @param userSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    UserSubscriptionDTO update(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Partially updates a userSubscription.
     *
     * @param userSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserSubscriptionDTO> partialUpdate(UserSubscriptionDTO userSubscriptionDTO);

    /**
     * Get all the userSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserSubscriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" userSubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
