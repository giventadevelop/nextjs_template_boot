package com.nextjstemplate.repository;

import com.nextjstemplate.domain.UserTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long>, JpaSpecificationExecutor<UserTask> {}
