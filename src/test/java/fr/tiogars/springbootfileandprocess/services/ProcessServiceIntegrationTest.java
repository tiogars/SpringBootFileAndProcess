package fr.tiogars.springbootfileandprocess.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;

/**
 * Integration tests for ProcessService with command validation.
 */
@SpringBootTest
public class ProcessServiceIntegrationTest {

    @Autowired
    private ProcessService processService;

    /**
     * Test that allowed command executes successfully.
     */
    @Test
    public void testAllowedCommandExecutes() throws IOException, InterruptedException {
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "Hello World" });
        
        CommandResult result = processService.executeAndWaitForResponse(command);
        
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0");
    }

    /**
     * Test that disallowed command throws SecurityException.
     */
    @Test
    public void testDisallowedCommandThrowsSecurityException() {
        ExecutableCommand command = new ExecutableCommand("rm", "", "", new String[] { "-rf", "/" });
        
        SecurityException exception = assertThrows(SecurityException.class,
                () -> processService.executeAndWaitForResponse(command));
        assertEquals("Command 'rm' is not in the allow-list", exception.getMessage());
    }

    /**
     * Test that command with invalid arguments throws SecurityException.
     */
    @Test
    public void testCommandWithInvalidArgumentsThrowsSecurityException() {
        // ls is allowed but with strict patterns in application.yml
        ExecutableCommand command = new ExecutableCommand("ls", "", "", new String[] { "$(rm -rf /)" });
        
        assertThrows(SecurityException.class,
                () -> processService.executeAndWaitForResponse(command));
    }

    /**
     * Test that ls command with valid flag executes.
     */
    @Test
    public void testLsWithValidFlagExecutes() throws IOException, InterruptedException {
        ExecutableCommand command = new ExecutableCommand("ls", "", "", new String[] { "-la" });
        
        CommandResult result = processService.executeAndWaitForResponse(command);
        
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0");
    }

    /**
     * Test that pwd command executes (no arguments needed).
     */
    @Test
    public void testPwdExecutes() throws IOException, InterruptedException {
        ExecutableCommand command = new ExecutableCommand("pwd", "", "", new String[] {});
        
        CommandResult result = processService.executeAndWaitForResponse(command);
        
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0");
    }

    /**
     * Test that date command executes.
     */
    @Test
    public void testDateExecutes() throws IOException, InterruptedException {
        ExecutableCommand command = new ExecutableCommand("date", "", "", new String[] {});
        
        CommandResult result = processService.executeAndWaitForResponse(command);
        
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0");
    }
}
