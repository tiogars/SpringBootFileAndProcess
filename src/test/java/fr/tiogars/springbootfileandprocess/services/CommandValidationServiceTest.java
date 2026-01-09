package fr.tiogars.springbootfileandprocess.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.tiogars.springbootfileandprocess.config.CommandSecurityProperties;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Unit tests for CommandValidationService class.
 */
public class CommandValidationServiceTest {

    private CommandValidationService validationService;
    private CommandSecurityProperties securityProperties;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setUp() {
        securityProperties = new CommandSecurityProperties();
        securityProperties.setEnabled(true);
        securityProperties.setAllowedCommands(Arrays.asList("echo", "ls", "ffmpeg"));
        
        // Setup argument patterns
        Map<String, List<String>> patterns = new HashMap<>();
        patterns.put("echo", Arrays.asList(".*"));
        patterns.put("ls", Arrays.asList("-[a-zA-Z]+", "/.*", "[^/].*"));
        patterns.put("ffmpeg", Arrays.asList("-[a-zA-Z]:[a-zA-Z0-9]+", "-[a-zA-Z]+", "[^-].*"));
        securityProperties.setArgumentPatterns(patterns);
        
        validationService = new CommandValidationService(securityProperties);
    }

    /**
     * Test validation passes for allowed command with no arguments.
     */
    @Test
    public void testValidateAllowedCommandWithNoArguments() {
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] {});
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation passes for allowed command with valid arguments.
     */
    @Test
    public void testValidateAllowedCommandWithValidArguments() {
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "Hello World" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation fails for command not in allow-list.
     */
    @Test
    public void testValidateDisallowedCommand() {
        ExecutableCommand command = new ExecutableCommand("rm", "", "", new String[] { "-rf", "/" });
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Command 'rm' is not in the allow-list", exception.getMessage());
    }

    /**
     * Test validation fails for null command.
     */
    @Test
    public void testValidateNullCommand() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(null));
        assertEquals("Command cannot be null", exception.getMessage());
    }

    /**
     * Test validation fails for command with null name.
     */
    @Test
    public void testValidateCommandWithNullName() {
        ExecutableCommand command = new ExecutableCommand(null, "", "", new String[] {});
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Command name cannot be null or empty", exception.getMessage());
    }

    /**
     * Test validation fails for command with empty name.
     */
    @Test
    public void testValidateCommandWithEmptyName() {
        ExecutableCommand command = new ExecutableCommand("", "", "", new String[] {});
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Command name cannot be null or empty", exception.getMessage());
    }

    /**
     * Test validation passes for ls command with valid flag argument.
     */
    @Test
    public void testValidateLsCommandWithFlag() {
        ExecutableCommand command = new ExecutableCommand("ls", "", "", new String[] { "-la" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation passes for ls command with path argument.
     */
    @Test
    public void testValidateLsCommandWithPath() {
        ExecutableCommand command = new ExecutableCommand("ls", "", "", new String[] { "/home" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation fails for ls command with invalid argument.
     */
    @Test
    public void testValidateLsCommandWithInvalidArgument() {
        // Setup ls with stricter patterns that don't match shell injection
        Map<String, List<String>> strictPatterns = new HashMap<>();
        strictPatterns.put("ls", Arrays.asList("-[a-zA-Z]+", "/[a-zA-Z0-9/_-]+", "[a-zA-Z0-9_-]+"));
        securityProperties.setArgumentPatterns(strictPatterns);
        
        ExecutableCommand command = new ExecutableCommand("ls", "", "", new String[] { "$(rm -rf /)" });
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Argument '$(rm -rf /)' for command 'ls' does not match allowed patterns",
                exception.getMessage());
    }

    /**
     * Test validation passes for ffmpeg command with valid arguments.
     */
    @Test
    public void testValidateFfmpegCommandWithValidArguments() {
        ExecutableCommand command = new ExecutableCommand("ffmpeg", "", "",
                new String[] { "-i", "input.mp4", "-c:v", "libx264", "output.mp4" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation fails for command with arguments when patterns defined but argument doesn't match.
     */
    @Test
    public void testValidateCommandWithInvalidArgument() {
        // Setup ffmpeg with strict patterns
        Map<String, List<String>> strictPatterns = new HashMap<>();
        strictPatterns.put("ffmpeg", Arrays.asList("-[a-zA-Z]"));
        securityProperties.setArgumentPatterns(strictPatterns);
        
        ExecutableCommand command = new ExecutableCommand("ffmpeg", "", "",
                new String[] { "-i", "input.mp4" });
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Argument 'input.mp4' for command 'ffmpeg' does not match allowed patterns",
                exception.getMessage());
    }

    /**
     * Test validation passes when security is disabled.
     */
    @Test
    public void testValidateWhenSecurityDisabled() {
        securityProperties.setEnabled(false);
        securityProperties.setAllowedCommands(Arrays.asList());
        
        ExecutableCommand command = new ExecutableCommand("rm", "", "", new String[] { "-rf", "/" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation passes for command with null arguments array when no patterns defined.
     */
    @Test
    public void testValidateCommandWithNullArgumentsWhenNoPatternsRequired() {
        // Setup command without argument patterns
        Map<String, List<String>> emptyPatterns = new HashMap<>();
        securityProperties.setArgumentPatterns(emptyPatterns);
        
        ExecutableCommand command = new ExecutableCommand("echo", "", "", null);
        // Should throw NullPointerException when trying to iterate null arguments
        // but validation should handle it gracefully
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation passes for command with empty arguments array.
     */
    @Test
    public void testValidateCommandWithEmptyArguments() {
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] {});
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation handles null argument in array gracefully.
     */
    @Test
    public void testValidateCommandWithNullArgumentInArray() {
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "hello", null, "world" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation passes for multiple valid arguments.
     */
    @Test
    public void testValidateCommandWithMultipleValidArguments() {
        ExecutableCommand command = new ExecutableCommand("ls", "", "",
                new String[] { "-la", "/home", "test" });
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation fails when one argument out of many is invalid.
     */
    @Test
    public void testValidateCommandWithOneInvalidArgumentAmongMany() {
        // Setup ls with stricter patterns
        Map<String, List<String>> strictPatterns = new HashMap<>();
        strictPatterns.put("ls", Arrays.asList("-[a-zA-Z]+", "/[a-zA-Z0-9/_-]+", "[a-zA-Z0-9_-]+"));
        securityProperties.setArgumentPatterns(strictPatterns);
        
        ExecutableCommand command = new ExecutableCommand("ls", "", "",
                new String[] { "-la", "/home", "$(malicious)" });
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> validationService.validateCommand(command));
        assertEquals("Argument '$(malicious)' for command 'ls' does not match allowed patterns",
                exception.getMessage());
    }

    /**
     * Test validation with command not having argument patterns defined.
     */
    @Test
    public void testValidateCommandWithoutArgumentPatterns() {
        // Create a mutable list
        java.util.List<String> commands = new java.util.ArrayList<>(securityProperties.getAllowedCommands());
        commands.add("date");
        securityProperties.setAllowedCommands(commands);
        
        ExecutableCommand command = new ExecutableCommand("date", "", "",
                new String[] { "+%Y-%m-%d", "any", "arguments" });
        // Should pass because no patterns are enforced
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }

    /**
     * Test validation with command having empty patterns list.
     */
    @Test
    public void testValidateCommandWithEmptyPatternsList() {
        Map<String, List<String>> patterns = securityProperties.getArgumentPatterns();
        patterns.put("echo", Arrays.asList());
        
        ExecutableCommand command = new ExecutableCommand("echo", "", "",
                new String[] { "test" });
        // Should pass because empty patterns list means no validation
        assertDoesNotThrow(() -> validationService.validateCommand(command));
    }
}
