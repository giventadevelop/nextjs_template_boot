package com.nextjstemplate.service.impl;

import com.nextjstemplate.domain.TicketTransaction;
import com.nextjstemplate.repository.TicketTransactionRepository;
import com.nextjstemplate.service.TicketTransactionService;
import com.nextjstemplate.service.dto.TicketTransactionDTO;
import com.nextjstemplate.service.mapper.TicketTransactionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nextjstemplate.domain.TicketTransaction}.
 */
@Service
@Transactional
public class TicketTransactionServiceImpl implements TicketTransactionService {

    private final Logger log = LoggerFactory.getLogger(TicketTransactionServiceImpl.class);

    private final TicketTransactionRepository ticketTransactionRepository;

    private final TicketTransactionMapper ticketTransactionMapper;

    public TicketTransactionServiceImpl(
        TicketTransactionRepository ticketTransactionRepository,
        TicketTransactionMapper ticketTransactionMapper
    ) {
        this.ticketTransactionRepository = ticketTransactionRepository;
        this.ticketTransactionMapper = ticketTransactionMapper;
    }

    @Override
    public TicketTransactionDTO save(TicketTransactionDTO ticketTransactionDTO) {
        log.debug("Request to save TicketTransaction : {}", ticketTransactionDTO);
        TicketTransaction ticketTransaction = ticketTransactionMapper.toEntity(ticketTransactionDTO);
        ticketTransaction = ticketTransactionRepository.save(ticketTransaction);
        return ticketTransactionMapper.toDto(ticketTransaction);
    }

    @Override
    public TicketTransactionDTO update(TicketTransactionDTO ticketTransactionDTO) {
        log.debug("Request to update TicketTransaction : {}", ticketTransactionDTO);
        TicketTransaction ticketTransaction = ticketTransactionMapper.toEntity(ticketTransactionDTO);
        ticketTransaction = ticketTransactionRepository.save(ticketTransaction);
        return ticketTransactionMapper.toDto(ticketTransaction);
    }

    @Override
    public Optional<TicketTransactionDTO> partialUpdate(TicketTransactionDTO ticketTransactionDTO) {
        log.debug("Request to partially update TicketTransaction : {}", ticketTransactionDTO);

        return ticketTransactionRepository
            .findById(ticketTransactionDTO.getId())
            .map(existingTicketTransaction -> {
                ticketTransactionMapper.partialUpdate(existingTicketTransaction, ticketTransactionDTO);

                return existingTicketTransaction;
            })
            .map(ticketTransactionRepository::save)
            .map(ticketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TicketTransactions");
        return ticketTransactionRepository.findAll(pageable).map(ticketTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketTransactionDTO> findOne(Long id) {
        log.debug("Request to get TicketTransaction : {}", id);
        return ticketTransactionRepository.findById(id).map(ticketTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketTransaction : {}", id);
        ticketTransactionRepository.deleteById(id);
    }
}
