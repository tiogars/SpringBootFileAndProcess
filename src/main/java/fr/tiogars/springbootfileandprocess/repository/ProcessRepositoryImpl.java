package fr.tiogars.springbootfileandprocess.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Implementation of {@link ProcessRepository} for process management.
 */
@Component
public class ProcessRepositoryImpl implements ProcessRepository {

    /**
     * The response to return when no result is found.
     */
    private static final String NO_RESULT_RESPONSE = "No result";

    /**
     * Logger for ProcessRepositoryImpl.
     */
    private final Logger logger = LoggerFactory.getLogger(
            ProcessRepositoryImpl.class);

    /**
     * Default constructor.
     */
    public ProcessRepositoryImpl() {
        // Default constructor
    }

    /**
     * Executes the given command and waits for a response.
     * 
     * @param executableCommandParam the command to execute
     * @return the process that was started
     * @throws IOException          if an I/O error occurs
     * @throws InterruptedException if the process is interrupted
     */
    public CommandResult executeAndWaitForResponse(final ExecutableCommand executableCommandParam)
            throws IOException, InterruptedException {
        try {

            logger.debug("Starting SystemRepository.getJavaVersion()");

            CommandResult result = new CommandResult(0, List.of(NO_RESULT_RESPONSE));

            String[] command = {
                    executableCommandParam.getCommand()
            };

            String[] args = executableCommandParam.getArguments();

            String[] allStrings = getAllStrings(command, args);

            ProcessBuilder processBuilder = new ProcessBuilder(allStrings);

            processBuilder.redirectErrorStream(true);

            if (executableCommandParam.getWorkingDirectory() != null && !executableCommandParam.getWorkingDirectory().isEmpty()) {
                processBuilder.directory(new java.io.File(executableCommandParam.getWorkingDirectory()));
            }

            Process process = processBuilder.start();

            List<String> results = readOutput(process.getInputStream());

            int exitValue = process.waitFor();

            if (result.getOutput().size() > 0) {
                result = new CommandResult(exitValue, results);
            }

            logger.info("Exit code {}", result.getExitCode());
            logger.info("Output {}", result.getOutput());

            return result;
        } catch (IOException | InterruptedException e) {
            logger.error("Error executing command: {}", e.getMessage());
            throw e;
        } finally {
            logger.debug("Ending SystemRepository.getJavaVersion()");
        }
    }

    /**
     * Combines the command and arguments into a single array.
     * 
     * @param commandParam the command to execute
     * @param argsParam    the arguments for the command
     * @return a combined array of command and arguments
     */
    private String[] getAllStrings(final String[] commandParam, final String[] argsParam) {
        String[] allStrings = Stream.concat(Arrays.stream(commandParam), Arrays.stream(argsParam))
                .toArray(String[]::new);
        return allStrings;
    }

    /**
     * Reads the output from the given InputStream.
     * 
     * @param inputStreamParam the InputStream to read from
     * @return a list of strings representing the output
     * @throws IOException if an I/O error occurs
     */
    public static List<String> readOutput(final InputStream inputStreamParam) throws IOException {
        Logger logger = LoggerFactory.getLogger(ProcessRepositoryImpl.class);
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStreamParam))) {
            List<String> lines = output.lines()
                    .peek(line -> logger.debug("Process output: {}", line))
                    .collect(Collectors.toList());
            return lines;
        }
    }
}
