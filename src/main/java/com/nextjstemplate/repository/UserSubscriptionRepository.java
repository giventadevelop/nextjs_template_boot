package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, JpaSpecificationExecutor<UserSubscription> {}
