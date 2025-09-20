package fr.tiogars.springbootfileandprocess.repository;

import java.io.IOException;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Repository interface for managing Process entities.
 */
public interface ProcessRepository {

    /**
     * Executes the given command.
     * 
     * @param executableCommandParam the command to execute
     * @return the process that was started
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the process is interrupted
     */
    CommandResult executeAndWaitForResponse(ExecutableCommand executableCommandParam) throws IOException, InterruptedException;
}
