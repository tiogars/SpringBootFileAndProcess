package fr.tiogars.springbootfileandprocess.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for command security.
 * Defines allowed commands and their permitted argument patterns.
 */
@Component
@ConfigurationProperties(prefix = "command.security")
public class CommandSecurityProperties {

    /**
     * Enable or disable command validation.
     */
    private boolean enabled = true;

    /**
     * List of allowed commands.
     * Only these commands can be executed.
     */
    private List<String> allowedCommands = new ArrayList<>();

    /**
     * Map of command to allowed argument patterns.
     * Key: command name
     * Value: list of regex patterns that arguments must match
     */
    private Map<String, List<String>> argumentPatterns = new HashMap<>();

    /**
     * Check if command security is enabled.
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set whether command security is enabled.
     * 
     * @param enabledParam true to enable, false to disable
     */
    public void setEnabled(final boolean enabledParam) {
        this.enabled = enabledParam;
    }

    /**
     * Get the list of allowed commands.
     * 
     * @return list of allowed command names
     */
    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    /**
     * Set the list of allowed commands.
     * 
     * @param allowedCommandsParam list of allowed command names
     */
    public void setAllowedCommands(final List<String> allowedCommandsParam) {
        this.allowedCommands = allowedCommandsParam;
    }

    /**
     * Get the map of argument patterns for each command.
     * 
     * @return map of command to list of allowed argument patterns
     */
    public Map<String, List<String>> getArgumentPatterns() {
        return argumentPatterns;
    }

    /**
     * Set the map of argument patterns for each command.
     * 
     * @param argumentPatternsParam map of command to list of allowed argument patterns
     */
    public void setArgumentPatterns(final Map<String, List<String>> argumentPatternsParam) {
        this.argumentPatterns = argumentPatternsParam;
    }
}
