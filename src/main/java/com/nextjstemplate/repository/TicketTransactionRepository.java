package com.nextjstemplate.repository;

import com.nextjstemplate.domain.TicketTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TicketTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketTransactionRepository extends JpaRepository<TicketTransaction, Long>, JpaSpecificationExecutor<TicketTransaction> {}
