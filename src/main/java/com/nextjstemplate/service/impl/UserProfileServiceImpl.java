package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserProfile;
import com.nextjstemplate.repository.UserProfileRepository;
import com.nextjstemplate.service.UserProfileService;
import com.nextjstemplate.service.dto.UserProfileDTO;
import com.nextjstemplate.service.mapper.UserProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public UserProfileDTO save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    public UserProfileDTO update(UserProfileDTO userProfileDTO) {
        log.debug("Request to update UserProfile : {}", userProfileDTO);
        UserProfile userProfile = userProfileMapper.toEntity(userProfileDTO);
        userProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    public Optional<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to partially update UserProfile : {}", userProfileDTO);

        return userProfileRepository
            .findById(userProfileDTO.getId())
            .map(existingUserProfile -> {
                userProfileMapper.partialUpdate(existingUserProfile, userProfileDTO);

                return existingUserProfile;
            })
            .map(userProfileRepository::save)
            .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAll(pageable).map(userProfileMapper::toDto);
    }

    /**
     *  Get all the userProfiles where UserSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserProfileDTO> findAllWhereUserSubscriptionIsNull() {
        log.debug("Request to get all userProfiles where UserSubscription is null");
        return StreamSupport
            .stream(userProfileRepository.findAll().spliterator(), false)
            .filter(userProfile -> userProfile.getUserSubscription() == null)
            .map(userProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findById(id).map(userProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
    }
}
