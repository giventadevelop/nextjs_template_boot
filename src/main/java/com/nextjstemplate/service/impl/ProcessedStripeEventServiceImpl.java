package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.ProcessedStripeEvent;
import com.nextjstemplate.repository.ProcessedStripeEventRepository;
import com.nextjstemplate.service.ProcessedStripeEventService;
import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import com.nextjstemplate.service.mapper.ProcessedStripeEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.ProcessedStripeEvent}.
 */
@Service
@Transactional
public class ProcessedStripeEventServiceImpl implements ProcessedStripeEventService {

    private final Logger log = LoggerFactory.getLogger(ProcessedStripeEventServiceImpl.class);

    private final ProcessedStripeEventRepository processedStripeEventRepository;

    private final ProcessedStripeEventMapper processedStripeEventMapper;

    public ProcessedStripeEventServiceImpl(
        ProcessedStripeEventRepository processedStripeEventRepository,
        ProcessedStripeEventMapper processedStripeEventMapper
    ) {
        this.processedStripeEventRepository = processedStripeEventRepository;
        this.processedStripeEventMapper = processedStripeEventMapper;
    }

    @Override
    public ProcessedStripeEventDTO save(ProcessedStripeEventDTO processedStripeEventDTO) {
        log.debug("Request to save ProcessedStripeEvent : {}", processedStripeEventDTO);
        ProcessedStripeEvent processedStripeEvent = processedStripeEventMapper.toEntity(processedStripeEventDTO);
        processedStripeEvent = processedStripeEventRepository.save(processedStripeEvent);
        return processedStripeEventMapper.toDto(processedStripeEvent);
    }

    @Override
    public ProcessedStripeEventDTO update(ProcessedStripeEventDTO processedStripeEventDTO) {
        log.debug("Request to update ProcessedStripeEvent : {}", processedStripeEventDTO);
        ProcessedStripeEvent processedStripeEvent = processedStripeEventMapper.toEntity(processedStripeEventDTO);
        processedStripeEvent = processedStripeEventRepository.save(processedStripeEvent);
        return processedStripeEventMapper.toDto(processedStripeEvent);
    }

    @Override
    public Optional<ProcessedStripeEventDTO> partialUpdate(ProcessedStripeEventDTO processedStripeEventDTO) {
        log.debug("Request to partially update ProcessedStripeEvent : {}", processedStripeEventDTO);

        return processedStripeEventRepository
            .findById(processedStripeEventDTO.getId())
            .map(existingProcessedStripeEvent -> {
                processedStripeEventMapper.partialUpdate(existingProcessedStripeEvent, processedStripeEventDTO);

                return existingProcessedStripeEvent;
            })
            .map(processedStripeEventRepository::save)
            .map(processedStripeEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProcessedStripeEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProcessedStripeEvents");
        return processedStripeEventRepository.findAll(pageable).map(processedStripeEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessedStripeEventDTO> findOne(Long id) {
        log.debug("Request to get ProcessedStripeEvent : {}", id);
        return processedStripeEventRepository.findById(id).map(processedStripeEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcessedStripeEvent : {}", id);
        processedStripeEventRepository.deleteById(id);
    }
}
