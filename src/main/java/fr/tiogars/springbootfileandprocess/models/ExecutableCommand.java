package fr.tiogars.springbootfileandprocess.models;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a command to be executed.
 */
public class ExecutableCommand {

    /**
     * The command to be executed.
     * Linux example: "mvn"
     * Windows example: "mvn.cmd"
     */
    @Schema(example = "mvn")
    private String command;

    /**
     * The working directory for the command.
     * Linux example: "/java-public-lib"
     * Windows example: "C:\\java-public-lib"
     */
    @Schema(example = "/java-public-lib")
    private String workingDirectory;

    /**
     * The path where the command is installed.
     * Linux example: "/usr/local/bin/mvn"
     * Windows example: "C:\\Program Files\\Apache\\maven\\bin\\mvn.cmd"
     */
    @Schema(example = "/usr/local/bin/mvn")
    private String commandPath;

    /**
     * The arguments for the command.
     */
    @Schema(example = "[\"site\"]")
    private String[] arguments;

    /**
     * Default constructor.
     */
    public ExecutableCommand() {
        this.command = "";
        this.workingDirectory = "";
        this.commandPath = "";
        this.arguments = new String[0];
    }

    /**
     * Parameterized constructor.
     * 
     * @param commandParam          the command to be executed
     * @param workingDirectoryParam the working directory
     * @param commandPathParam      the command path
     * @param argumentsParam        the command arguments
     */
    public ExecutableCommand(final String commandParam, final String workingDirectoryParam,
            final String commandPathParam, final String[] argumentsParam) {
        this.command = commandParam;
        this.workingDirectory = workingDirectoryParam;
        this.commandPath = commandPathParam;
        this.arguments = argumentsParam;
    }

    /**
     * Gets the command to be executed.
     * 
     * @return the command to be executed
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the command to be executed.
     * 
     * @param commandParam the command to be executed
     */
    public void setCommand(final String commandParam) {
        this.command = commandParam;
    }

    /**
     * Gets the arguments for the command.
     * 
     * @return the arguments for the command
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * Sets the arguments for the command.
     * 
     * @param argumentsParam the arguments for the command
     */
    public void setArguments(final String[] argumentsParam) {
        this.arguments = argumentsParam;
    }

    /**
     * Gets the working directory.
     * 
     * @return the working directory
     */
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Sets the working directory.
     * 
     * @param workingDirectoryParam the working directory
     */
    public void setWorkingDirectory(final String workingDirectoryParam) {
        this.workingDirectory = workingDirectoryParam;
    }

    /**
     * Gets the path where the command is installed.
     * 
     * @return the command path
     */
    public String getCommandPath() {
        return commandPath;
    }

    /**
     * Sets the path where the command is installed.
     * 
     * @param commandPathParam the command path
     */
    public void setCommandPath(final String commandPathParam) {
        this.commandPath = commandPathParam;
    }
}
