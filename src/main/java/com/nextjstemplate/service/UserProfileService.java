package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.UserProfileDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.UserProfile}.
 */
public interface UserProfileService {
    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    UserProfileDTO save(UserProfileDTO userProfileDTO);

    /**
     * Updates a userProfile.
     *
     * @param userProfileDTO the entity to update.
     * @return the persisted entity.
     */
    UserProfileDTO update(UserProfileDTO userProfileDTO);

    /**
     * Partially updates a userProfile.
     *
     * @param userProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO);

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserProfileDTO> findAll(Pageable pageable);

    /**
     * Get all the UserProfileDTO where UserSubscription is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<UserProfileDTO> findAllWhereUserSubscriptionIsNull();

    /**
     * Get the "id" userProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserProfileDTO> findOne(Long id);

    /**
     * Delete the "id" userProfile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get the userProfile by user ID.
     *
     * @param userId the user id to search for.
     * @return the entity.
     */
    Optional<UserProfileDTO> findByUserId(String userId);
}
