package fr.tiogars.springbootfileandprocess.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.springbootfileandprocess.services.SystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for system information endpoints.
 */
@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/system")
@Tag(name = "System", description = "System information")
public class SystemController {

    /**
     * Logger for system information.
     */
    private final Logger logger = LoggerFactory.getLogger(
            SystemController.class);

    /**
     * System service for retrieving system information.
     */
    private final SystemService systemService;

    /**
     * Constructor for SystemController.
     * @param systemServiceParam The system service interface.
     */
    public SystemController(final SystemService systemServiceParam) {
        this.systemService = systemServiceParam;
    }

    /**
     * Get system host name.
     * @return The system host name.
     */
    @Operation(summary = "Get system host name")
    @GetMapping("/hostname")
    public ResponseEntity<String> getSystemHostName() {

        String hostName = systemService.getComputerName();

        logger.info(hostName);

        return new ResponseEntity<String>(hostName, HttpStatus.OK);
    }
}
