package fr.tiogars.springbootfileandprocess.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.springbootfileandprocess.models.CommandResult;
import fr.tiogars.springbootfileandprocess.models.ExecutableCommand;


/**
 * Unit tests for ProcessRepositoryImpl class.
 */
@ExtendWith(MockitoExtension.class)
public class ProcessRepositoryImplTest {

    private ProcessRepositoryImpl processRepository;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setUp() {
        processRepository = new ProcessRepositoryImpl();
    }

    /**
     * Test constructor creates instance successfully.
     */
    @Test
    public void testConstructor() {
        ProcessRepositoryImpl repository = new ProcessRepositoryImpl();
        assertNotNull(repository, "Repository should be created successfully");
    }

    /**
     * Test executeAndWaitForResponse with successful command execution.
     */
    @Test
    public void testExecuteAndWaitForResponseSuccess() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "Hello World" });

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0 for successful execution");
        assertNotNull(result.getOutput(), "Output should not be null");
    }

    /**
     * Test executeAndWaitForResponse with command that returns non-zero exit code.
     */
    @Test
    public void testExecuteAndWaitForResponseWithNonZeroExitCode() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("false", "", "", new String[] {});

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.getExitCode(), "Exit code should be 1 for failed command");
        assertNotNull(result.getOutput(), "Output should not be null");
    }

    /**
     * Test executeAndWaitForResponse with invalid command throws IOException.
     */
    @Test
    public void testExecuteAndWaitForResponseWithInvalidCommand() {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("nonexistentcommand12345", "", "", new String[] {});

        // Act & Assert
        assertThrows(IOException.class, () -> {
            processRepository.executeAndWaitForResponse(command);
        }, "Should throw IOException for invalid command");
    }

    /**
     * Test executeAndWaitForResponse with custom working directory.
     */
    @Test
    public void testExecuteAndWaitForResponseWithWorkingDirectory() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("pwd", "/tmp", "", new String[] {});

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0 for successful execution");
        assertNotNull(result.getOutput(), "Output should not be null");
        assertTrue(result.getOutput().size() > 0, "Output should contain working directory");
    }

    /**
     * Test executeAndWaitForResponse with null working directory.
     */
    @Test
    public void testExecuteAndWaitForResponseWithNullWorkingDirectory() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("echo", null, "", new String[] { "test" });

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0 for successful execution");
        assertNotNull(result.getOutput(), "Output should not be null");
    }

    /**
     * Test executeAndWaitForResponse with empty working directory.
     */
    @Test
    public void testExecuteAndWaitForResponseWithEmptyWorkingDirectory() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "test" });

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0 for successful execution");
        assertNotNull(result.getOutput(), "Output should not be null");
    }

    /**
     * Test executeAndWaitForResponse with multiple arguments.
     */
    @Test
    public void testExecuteAndWaitForResponseWithMultipleArguments() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("echo", "", "", new String[] { "arg1", "arg2", "arg3" });

        // Act
        CommandResult result = processRepository.executeAndWaitForResponse(command);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.getExitCode(), "Exit code should be 0 for successful execution");
        assertNotNull(result.getOutput(), "Output should not be null");
        assertTrue(result.getOutput().size() > 0, "Output should contain arguments");
    }

    /**
     * Test readOutput method with simple input stream.
     */
    @Test
    public void testReadOutputWithSimpleInput() throws IOException {
        // Arrange
        String testInput = "Line 1\nLine 2\nLine 3\n";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        // Act
        List<String> result = ProcessRepositoryImpl.readOutput(inputStream);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(3, result.size(), "Should have 3 lines");
        assertEquals("Line 1", result.get(0), "First line should match");
        assertEquals("Line 2", result.get(1), "Second line should match");
        assertEquals("Line 3", result.get(2), "Third line should match");
    }

    /**
     * Test readOutput method with empty input stream.
     */
    @Test
    public void testReadOutputWithEmptyInput() throws IOException {
        // Arrange
        String testInput = "";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        // Act
        List<String> result = ProcessRepositoryImpl.readOutput(inputStream);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.size(), "Should have no lines for empty input");
    }

    /**
     * Test readOutput method with single line input.
     */
    @Test
    public void testReadOutputWithSingleLineInput() throws IOException {
        // Arrange
        String testInput = "Single line without newline";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        // Act
        List<String> result = ProcessRepositoryImpl.readOutput(inputStream);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should have 1 line");
        assertEquals("Single line without newline", result.get(0), "Line should match");
    }

    /**
     * Test readOutput method throws NullPointerException when stream is null.
     */
    @Test
    public void testReadOutputWithNullInputStream() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            ProcessRepositoryImpl.readOutput(null);
        }, "Should throw NullPointerException for null input stream");
    }

    /**
     * Test executeAndWaitForResponse with null ExecutableCommand.
     */
    @Test
    public void testExecuteAndWaitForResponseWithNullCommand() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            processRepository.executeAndWaitForResponse(null);
        }, "Should throw NullPointerException for null command");
    }

    /**
     * Test executeAndWaitForResponse with null command string.
     */
    @Test
    public void testExecuteAndWaitForResponseWithNullCommandString() {
        // Arrange
        ExecutableCommand command = new ExecutableCommand(null, "", "", new String[] {});

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            processRepository.executeAndWaitForResponse(command);
        }, "Should throw NullPointerException for null command string");
    }

    /**
     * Test executeAndWaitForResponse with null arguments array.
     */
    @Test
    public void testExecuteAndWaitForResponseWithNullArguments() throws IOException, InterruptedException {
        // Arrange
        ExecutableCommand command = new ExecutableCommand("echo", "", "", null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            processRepository.executeAndWaitForResponse(command);
        }, "Should throw NullPointerException for null arguments");
    }
}