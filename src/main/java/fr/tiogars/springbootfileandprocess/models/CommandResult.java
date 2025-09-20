package fr.tiogars.springbootfileandprocess.models;

import java.util.List;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    /**
     * The exit code of the command.
     */
    private int exitCode;

    /**
     * The output of the command.
     */
    private List<String> output;

    /**
     * Constructs a CommandResult with the given exit code and output.
     * 
     * @param exitCodeParam the exit code of the command
     * @param outputParam   the output of the command
     */
    public CommandResult(final int exitCodeParam, final List<String> outputParam) {
        this.exitCode = exitCodeParam;
        this.output = outputParam;
    }

    /**
     * Returns the exit code of the command.
     * 
     * @return the exit code of the command
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Returns the output of the command.
     * 
     * @return the output of the command
     */
    public List<String> getOutput() {
        return output;
    }
}
