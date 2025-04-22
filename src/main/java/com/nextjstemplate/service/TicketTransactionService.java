package com.nextjstemplate.service;

import com.nextjstemplate.service.dto.TicketTransactionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.nextjstemplate.domain.TicketTransaction}.
 */
public interface TicketTransactionService {
    /**
     * Save a ticketTransaction.
     *
     * @param ticketTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    TicketTransactionDTO save(TicketTransactionDTO ticketTransactionDTO);

    /**
     * Updates a ticketTransaction.
     *
     * @param ticketTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    TicketTransactionDTO update(TicketTransactionDTO ticketTransactionDTO);

    /**
     * Partially updates a ticketTransaction.
     *
     * @param ticketTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TicketTransactionDTO> partialUpdate(TicketTransactionDTO ticketTransactionDTO);

    /**
     * Get all the ticketTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TicketTransactionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ticketTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TicketTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" ticketTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
