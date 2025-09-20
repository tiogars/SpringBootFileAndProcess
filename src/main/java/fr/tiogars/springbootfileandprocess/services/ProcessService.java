package fr.tiogars.springbootfileandprocess.services;

import java.io.IOException;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Service interface for executing and managing processes.
 */
public interface ProcessService {

    /**
     * Executes a command and waits for the response.
     *
     * @param executableCommandParam the command to execute
     * @return the result of the command execution
     * @throws IOException              if an I/O error occurs
     * @throws InterruptedException     if the execution is interrupted
     */
    CommandResult executeAndWaitForResponse(ExecutableCommand executableCommandParam)
            throws IOException, InterruptedException;
}
