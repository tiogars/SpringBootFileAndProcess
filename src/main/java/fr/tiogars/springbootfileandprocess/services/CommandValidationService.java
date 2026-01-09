package fr.tiogars.springbootfileandprocess.services;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.tiogars.springbootfileandprocess.config.CommandSecurityProperties;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Service for validating commands against security policies.
 */
@Service
public class CommandValidationService {

    /**
     * Logger for CommandValidationService.
     */
    private final Logger logger = LoggerFactory.getLogger(CommandValidationService.class);

    /**
     * Security properties configuration.
     */
    private final CommandSecurityProperties securityProperties;

    /**
     * Constructor for CommandValidationService.
     * 
     * @param securityPropertiesParam the security properties
     */
    public CommandValidationService(final CommandSecurityProperties securityPropertiesParam) {
        this.securityProperties = securityPropertiesParam;
    }

    /**
     * Validates a command against the allow-list and argument patterns.
     * 
     * @param command the command to validate
     * @throws SecurityException if the command or its arguments are not allowed
     */
    public void validateCommand(final ExecutableCommand command) {
        if (!securityProperties.isEnabled()) {
            logger.debug("Command validation is disabled");
            return;
        }

        if (command == null) {
            throw new SecurityException("Command cannot be null");
        }

        String commandName = command.getCommand();
        if (commandName == null || commandName.trim().isEmpty()) {
            throw new SecurityException("Command name cannot be null or empty");
        }

        // Validate command is in allow-list
        if (!securityProperties.getAllowedCommands().contains(commandName)) {
            logger.warn("Attempted to execute unauthorized command: {}", commandName);
            throw new SecurityException("Command '" + commandName + "' is not in the allow-list");
        }

        // Validate arguments against patterns if configured
        List<String> patterns = securityProperties.getArgumentPatterns().get(commandName);
        if (patterns != null && !patterns.isEmpty()) {
            validateArguments(commandName, command.getArguments(), patterns);
        }

        logger.debug("Command '{}' passed validation", commandName);
    }

    /**
     * Validates command arguments against allowed patterns.
     * 
     * @param commandName the name of the command
     * @param arguments   the arguments to validate
     * @param patterns    the list of allowed regex patterns
     * @throws SecurityException if any argument doesn't match the allowed patterns
     */
    private void validateArguments(final String commandName, final String[] arguments,
            final List<String> patterns) {
        if (arguments == null || arguments.length == 0) {
            return;
        }

        for (String argument : arguments) {
            if (argument == null) {
                continue;
            }

            boolean matched = false;
            for (String patternStr : patterns) {
                try {
                    Pattern pattern = Pattern.compile(patternStr);
                    if (pattern.matcher(argument).matches()) {
                        matched = true;
                        break;
                    }
                } catch (Exception e) {
                    logger.error("Invalid regex pattern '{}' for command '{}': {}",
                            patternStr, commandName, e.getMessage());
                }
            }

            if (!matched) {
                logger.warn("Argument '{}' for command '{}' does not match any allowed patterns",
                        argument, commandName);
                throw new SecurityException("Argument '" + argument + "' for command '" + commandName
                        + "' does not match allowed patterns");
            }
        }
    }
}
