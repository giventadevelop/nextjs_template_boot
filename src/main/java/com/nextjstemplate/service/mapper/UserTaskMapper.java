package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.UserTask;
import com.nextjstemplate.service.dto.UserTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserTask} and its DTO {@link UserTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserTaskMapper extends EntityMapper<UserTaskDTO, UserTask> {}
