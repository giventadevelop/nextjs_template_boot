package com.nextjstemplate.repository;

import com.nextjstemplate.domain.ProcessedStripeEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProcessedStripeEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessedStripeEventRepository
    extends JpaRepository<ProcessedStripeEvent, Long>, JpaSpecificationExecutor<ProcessedStripeEvent> {}
