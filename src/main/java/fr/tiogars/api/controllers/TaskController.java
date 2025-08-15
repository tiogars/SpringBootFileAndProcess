package fr.tiogars.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.api.models.Task;
import fr.tiogars.api.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = {
        "http://localhost:8181",
})
@RequestMapping("/task")
@Tag(name = "Task", description = "The task API")
public class TaskController {

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    /**
     * Constructor for TaskController.
     * 
     * @param taskService The task service interface.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Create a new task.
     * 
     * @param task The task to create.
     * @return The created task.
     */
    @Operation(summary = "Create a new task")
    @PostMapping("/")
    public ResponseEntity<Task> create(@RequestBody Task task) {
        logger.info("create task");
        return new ResponseEntity<>(taskService.create(task), HttpStatus.CREATED);
    }

    /**
     * Delete a task by ID.
     * 
     * @param id The ID of the task to delete.
     * @return The deleted task if found, otherwise NOT_FOUND status.
     */
    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> delete(
            @Parameter(in = ParameterIn.PATH, description = "id of the task", required = true, example = "1", schema = @Schema()) @PathVariable Long id) {
        logger.info("delete task");
        Task task = taskService.delete(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Find all tasks.
     * 
     * @return All tasks.
     */
    @Operation(summary = "Find all tasks")
    @GetMapping("/")
    public ResponseEntity<Iterable<Task>> findAll() {
        logger.info("find all tasks");
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    /**
     * Find all tasks with paging and sorting.
     * 
     * @param pageable The pageable object for pagination and sorting.
     * @return All tasks with paging and sorting.
     */
    @Operation(summary = "Find all tasks with paging and sorting")
    @GetMapping("/page")
    public ResponseEntity<Page<Task>> findAll(@ParameterObject Pageable pageable) {
        logger.info("find all tasks with paging and sorting");
        return new ResponseEntity<>(taskService.findAll(pageable), HttpStatus.OK);
    }

    /**
     * Find a task by ID.
     * 
     * @param id The ID of the task to find.
     * @return The found task if exists, otherwise NOT_FOUND status.
     */
    @Operation(summary = "Find a task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(
            @Parameter(in = ParameterIn.PATH, description = "id of the task", required = true, example = "1", schema = @Schema()) @PathVariable Long id) {
        logger.info("find task by id");
        Task task = taskService.findById(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Update a task by ID.
     * 
     * @param id   The ID of the task to update.
     * @param task The updated task.
     * @return The updated task if found, otherwise NOT_FOUND status.
     */
    @Operation(summary = "Update a task by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @Parameter(in = ParameterIn.PATH, description = "id of the task", required = true, example = "1", schema = @Schema()) @PathVariable Long id,
            @RequestBody Task task) {
        logger.info("update task");
        Task updatedTask = taskService.update(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}