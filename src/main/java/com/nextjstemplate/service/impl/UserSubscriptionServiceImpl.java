package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserSubscription;
import com.nextjstemplate.repository.UserSubscriptionRepository;
import com.nextjstemplate.service.UserSubscriptionService;
import com.nextjstemplate.service.dto.UserSubscriptionDTO;
import com.nextjstemplate.service.mapper.UserSubscriptionMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.nextjstemplate.domain.UserSubscription}.
 */
@Service
@Transactional
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(UserSubscriptionServiceImpl.class);

    private final UserSubscriptionRepository userSubscriptionRepository;

    private final UserSubscriptionMapper userSubscriptionMapper;

    public UserSubscriptionServiceImpl(
        UserSubscriptionRepository userSubscriptionRepository,
        UserSubscriptionMapper userSubscriptionMapper
    ) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionMapper = userSubscriptionMapper;
    }

    @Override
    public UserSubscriptionDTO save(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to save UserSubscription : {}", userSubscriptionDTO);
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription = userSubscriptionRepository.save(userSubscription);
        return userSubscriptionMapper.toDto(userSubscription);
    }

    @Override
    public UserSubscriptionDTO update(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to update UserSubscription : {}", userSubscriptionDTO);
        UserSubscription userSubscription = userSubscriptionMapper.toEntity(userSubscriptionDTO);
        userSubscription = userSubscriptionRepository.save(userSubscription);
        return userSubscriptionMapper.toDto(userSubscription);
    }

    @Override
    public Optional<UserSubscriptionDTO> partialUpdate(UserSubscriptionDTO userSubscriptionDTO) {
        log.debug("Request to partially update UserSubscription : {}", userSubscriptionDTO);

        return userSubscriptionRepository
            .findById(userSubscriptionDTO.getId())
            .map(existingUserSubscription -> {
                userSubscriptionMapper.partialUpdate(existingUserSubscription, userSubscriptionDTO);

                return existingUserSubscription;
            })
            .map(userSubscriptionRepository::save)
            .map(userSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSubscriptions");
        return userSubscriptionRepository.findAll(pageable).map(userSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get UserSubscription : {}", id);
        return userSubscriptionRepository.findById(id).map(userSubscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserSubscription : {}", id);
        userSubscriptionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSubscriptionDTO> findByUserProfileId(Long userProfileId) {
        log.debug("Request to get all UserSubscriptions for userProfile : {}", userProfileId);
        return userSubscriptionRepository
            .findByUserProfileId(userProfileId)
            .stream()
            .map(userSubscriptionMapper::toDto)
            .collect(java.util.stream.Collectors.toList());
    }
}
