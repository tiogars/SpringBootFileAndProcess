package fr.tiogars.springbootfileandprocess.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for CommandSecurityProperties class.
 */
public class CommandSecurityPropertiesTest {

    private CommandSecurityProperties properties;

    /**
     * Setup method executed before each test.
     */
    @BeforeEach
    public void setUp() {
        properties = new CommandSecurityProperties();
    }

    /**
     * Test default enabled value is true.
     */
    @Test
    public void testDefaultEnabledIsTrue() {
        assertTrue(properties.isEnabled(), "Default enabled should be true");
    }

    /**
     * Test setting and getting enabled property.
     */
    @Test
    public void testSetAndGetEnabled() {
        properties.setEnabled(false);
        assertFalse(properties.isEnabled(), "Enabled should be false after setting");
        
        properties.setEnabled(true);
        assertTrue(properties.isEnabled(), "Enabled should be true after setting");
    }

    /**
     * Test default allowed commands list is empty.
     */
    @Test
    public void testDefaultAllowedCommandsIsEmpty() {
        assertNotNull(properties.getAllowedCommands(), "Default allowed commands should not be null");
        assertTrue(properties.getAllowedCommands().isEmpty(),
                "Default allowed commands should be empty");
    }

    /**
     * Test setting and getting allowed commands.
     */
    @Test
    public void testSetAndGetAllowedCommands() {
        List<String> commands = Arrays.asList("echo", "ls", "pwd");
        properties.setAllowedCommands(commands);
        
        assertEquals(commands, properties.getAllowedCommands(),
                "Allowed commands should match what was set");
    }

    /**
     * Test default argument patterns map is empty.
     */
    @Test
    public void testDefaultArgumentPatternsIsEmpty() {
        assertNotNull(properties.getArgumentPatterns(),
                "Default argument patterns should not be null");
        assertTrue(properties.getArgumentPatterns().isEmpty(),
                "Default argument patterns should be empty");
    }

    /**
     * Test setting and getting argument patterns.
     */
    @Test
    public void testSetAndGetArgumentPatterns() {
        Map<String, List<String>> patterns = new HashMap<>();
        patterns.put("echo", Arrays.asList(".*"));
        patterns.put("ls", Arrays.asList("-[a-zA-Z]+", "/.*"));
        
        properties.setArgumentPatterns(patterns);
        
        assertEquals(patterns, properties.getArgumentPatterns(),
                "Argument patterns should match what was set");
    }

    /**
     * Test modifying allowed commands list after setting.
     */
    @Test
    public void testModifyAllowedCommandsList() {
        List<String> commands = Arrays.asList("echo", "ls");
        properties.setAllowedCommands(commands);
        
        List<String> retrieved = properties.getAllowedCommands();
        assertEquals(2, retrieved.size(), "Should have 2 commands");
        assertTrue(retrieved.contains("echo"), "Should contain echo");
        assertTrue(retrieved.contains("ls"), "Should contain ls");
    }

    /**
     * Test modifying argument patterns map after setting.
     */
    @Test
    public void testModifyArgumentPatternsMap() {
        Map<String, List<String>> patterns = new HashMap<>();
        patterns.put("echo", Arrays.asList(".*"));
        properties.setArgumentPatterns(patterns);
        
        Map<String, List<String>> retrieved = properties.getArgumentPatterns();
        assertEquals(1, retrieved.size(), "Should have 1 pattern entry");
        assertTrue(retrieved.containsKey("echo"), "Should contain echo key");
        assertEquals(Arrays.asList(".*"), retrieved.get("echo"),
                "Echo pattern should match");
    }
}
