package com.nextjstemplate.service.mapper;

import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TicketTransaction} and its DTO {@link TicketTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketTransactionMapper extends EntityMapper<TicketTransactionDTO, TicketTransaction> {}
