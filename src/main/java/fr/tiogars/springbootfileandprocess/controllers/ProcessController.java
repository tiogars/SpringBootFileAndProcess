package fr.tiogars.springbootfileandprocess.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;
import fr.tiogars.springbootfileandprocess.services.ProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for managing processes.
 */
@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/process")
@Tag(name = "Process", description = "Process management")
public class ProcessController {

    /**
     * Logger for ProcessController.
     */
    private final Logger logger = LoggerFactory.getLogger(ProcessController.class);

    /**
     * The process service for executing commands.
     */
    private final ProcessService processService;

    /**
     * Constructor for ProcessController.
     *
     * @param processServiceParam the process service
     */
    public ProcessController(final ProcessService processServiceParam) {
        this.processService = processServiceParam;
    }

    /**
     * Execute a command.
     *
     * @param command the command to execute
     * @return the result of the command execution
     */
    @Operation(summary = "Execute a command")
    @PostMapping("/execute")
    public ResponseEntity<CommandResult> executeCommand(
        final @RequestBody ExecutableCommand command
    ) {
        try {
            return ResponseEntity.ok(processService.executeAndWaitForResponse(command));
        } catch (SecurityException e) {
            // Handle security exceptions
            logger.error("Security violation: {}", e.getMessage());
            return ResponseEntity.status(403).body(new CommandResult(-1, java.util.Arrays.asList("Security Error", e.getMessage())));
        } catch (IOException | InterruptedException e) {
            // Handle exceptions
            logger.error("Error executing command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new CommandResult(-1, java.util.Arrays.asList("Error", e.getMessage())));
        }
    }
}
