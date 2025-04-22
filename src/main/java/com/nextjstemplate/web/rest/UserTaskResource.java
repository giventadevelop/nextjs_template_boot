package com.nextjstemplate.web.rest;

import com.nextjstemplate.repository.UserTaskRepository;
import com.nextjstemplate.service.UserTaskQueryService;
import com.nextjstemplate.service.UserTaskService;
import com.nextjstemplate.service.criteria.UserTaskCriteria;
import com.nextjstemplate.service.dto.UserTaskDTO;
import com.nextjstemplate.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nextjstemplate.domain.UserTask}.
 */
@RestController
@RequestMapping("/api/user-tasks")
public class UserTaskResource {

    private final Logger log = LoggerFactory.getLogger(UserTaskResource.class);

    private static final String ENTITY_NAME = "userTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTaskService userTaskService;

    private final UserTaskRepository userTaskRepository;

    private final UserTaskQueryService userTaskQueryService;

    public UserTaskResource(
        UserTaskService userTaskService,
        UserTaskRepository userTaskRepository,
        UserTaskQueryService userTaskQueryService
    ) {
        this.userTaskService = userTaskService;
        this.userTaskRepository = userTaskRepository;
        this.userTaskQueryService = userTaskQueryService;
    }

    /**
     * {@code POST  /user-tasks} : Create a new userTask.
     *
     * @param userTaskDTO the userTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTaskDTO, or with status {@code 400 (Bad Request)} if the userTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserTaskDTO> createUserTask(@Valid @RequestBody UserTaskDTO userTaskDTO) throws URISyntaxException {
        log.debug("REST request to save UserTask : {}", userTaskDTO);
        if (userTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new userTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserTaskDTO result = userTaskService.save(userTaskDTO);
        return ResponseEntity
            .created(new URI("/api/user-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-tasks/:id} : Updates an existing userTask.
     *
     * @param id the id of the userTaskDTO to save.
     * @param userTaskDTO the userTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTaskDTO,
     * or with status {@code 400 (Bad Request)} if the userTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserTaskDTO> updateUserTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserTaskDTO userTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserTask : {}, {}", id, userTaskDTO);
        if (userTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserTaskDTO result = userTaskService.update(userTaskDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-tasks/:id} : Partial updates given fields of an existing userTask, field will ignore if it is null
     *
     * @param id the id of the userTaskDTO to save.
     * @param userTaskDTO the userTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTaskDTO,
     * or with status {@code 400 (Bad Request)} if the userTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserTaskDTO> partialUpdateUserTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserTaskDTO userTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserTask partially : {}, {}", id, userTaskDTO);
        if (userTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserTaskDTO> result = userTaskService.partialUpdate(userTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-tasks} : get all the userTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserTaskDTO>> getAllUserTasks(
        UserTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserTasks by criteria: {}", criteria);

        Page<UserTaskDTO> page = userTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-tasks/count} : count all the userTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserTasks(UserTaskCriteria criteria) {
        log.debug("REST request to count UserTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(userTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-tasks/:id} : get the "id" userTask.
     *
     * @param id the id of the userTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserTaskDTO> getUserTask(@PathVariable Long id) {
        log.debug("REST request to get UserTask : {}", id);
        Optional<UserTaskDTO> userTaskDTO = userTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userTaskDTO);
    }

    /**
     * {@code DELETE  /user-tasks/:id} : delete the "id" userTask.
     *
     * @param id the id of the userTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserTask(@PathVariable Long id) {
        log.debug("REST request to delete UserTask : {}", id);
        userTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
