package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.ProcessedStripeEvent;
import com.nextjstemplate.service.dto.ProcessedStripeEventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessedStripeEvent} and its DTO {@link ProcessedStripeEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProcessedStripeEventMapper extends EntityMapper<ProcessedStripeEventDTO, ProcessedStripeEvent> {}
