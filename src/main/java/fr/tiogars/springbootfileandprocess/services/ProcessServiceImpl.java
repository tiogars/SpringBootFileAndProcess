package fr.tiogars.springbootfileandprocess.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;
import fr.tiogars.springbootfileandprocess.repository.ProcessRepository;

/**
 * Implementation of the ProcessService interface.
 */
@Service
public class ProcessServiceImpl implements ProcessService {

    /**
     * The process repository for executing commands.
     */
    private ProcessRepository processRepository;

    /**
     * Constructor for ProcessServiceImpl.
     *
     * @param processRepositoryParam the process repository
     */
    public ProcessServiceImpl(final ProcessRepository processRepositoryParam) {
        this.processRepository = processRepositoryParam;
    }

    /**
     * Executes a command and waits for the response.
     *
     * @param executableCommandParam the command to execute
     * @return the result of the command execution
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the execution is interrupted
     */
    public CommandResult executeAndWaitForResponse(final ExecutableCommand executableCommandParam)
            throws IOException, InterruptedException {
        return processRepository.executeAndWaitForResponse(executableCommandParam);
    }
}
