package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.UserTask;
import com.nextjstemplate.repository.UserTaskRepository;
import com.nextjstemplate.service.UserTaskService;
import com.nextjstemplate.service.dto.UserTaskDTO;
import com.nextjstemplate.service.mapper.UserTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.UserTask}.
 */
@Service
@Transactional
public class UserTaskServiceImpl implements UserTaskService {

    private final Logger log = LoggerFactory.getLogger(UserTaskServiceImpl.class);

    private final UserTaskRepository userTaskRepository;

    private final UserTaskMapper userTaskMapper;

    public UserTaskServiceImpl(UserTaskRepository userTaskRepository, UserTaskMapper userTaskMapper) {
        this.userTaskRepository = userTaskRepository;
        this.userTaskMapper = userTaskMapper;
    }

    @Override
    public UserTaskDTO save(UserTaskDTO userTaskDTO) {
        log.debug("Request to save UserTask : {}", userTaskDTO);
        UserTask userTask = userTaskMapper.toEntity(userTaskDTO);
        userTask = userTaskRepository.save(userTask);
        return userTaskMapper.toDto(userTask);
    }

    @Override
    public UserTaskDTO update(UserTaskDTO userTaskDTO) {
        log.debug("Request to update UserTask : {}", userTaskDTO);
        UserTask userTask = userTaskMapper.toEntity(userTaskDTO);
        userTask = userTaskRepository.save(userTask);
        return userTaskMapper.toDto(userTask);
    }

    @Override
    public Optional<UserTaskDTO> partialUpdate(UserTaskDTO userTaskDTO) {
        log.debug("Request to partially update UserTask : {}", userTaskDTO);

        return userTaskRepository
            .findById(userTaskDTO.getId())
            .map(existingUserTask -> {
                userTaskMapper.partialUpdate(existingUserTask, userTaskDTO);

                return existingUserTask;
            })
            .map(userTaskRepository::save)
            .map(userTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserTasks");
        return userTaskRepository.findAll(pageable).map(userTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserTaskDTO> findOne(Long id) {
        log.debug("Request to get UserTask : {}", id);
        return userTaskRepository.findById(id).map(userTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserTask : {}", id);
        userTaskRepository.deleteById(id);
    }
}
