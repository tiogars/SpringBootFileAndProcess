# Security

This document describes the security features implemented in SpringBootFileAndProcess.

## Command Allow-listing

To prevent arbitrary command execution and potential security vulnerabilities, the application implements a command allow-listing mechanism. This feature ensures that only pre-approved commands can be executed and that their arguments match specific patterns.

### How It Works

1. **Command Validation**: Before any command is executed, it must pass validation checks.
2. **Allow-list Check**: The command name must be in the configured list of allowed commands.
3. **Argument Pattern Validation**: If argument patterns are defined for a command, all arguments must match at least one of the allowed regex patterns.

### Configuration

The security configuration is defined in `application.yml`:

```yaml
command:
  security:
    # Enable or disable command validation (default: true)
    enabled: true
    
    # List of allowed commands
    allowedCommands:
      - echo
      - ls
      - pwd
      - date
      - ffmpeg
      - mvn
      - java
    
    # Argument patterns for each command (optional)
    argumentPatterns:
      echo:
        - "[a-zA-Z0-9 _.,!?-]+"  # Alphanumeric and safe punctuation only
      ls:
        - "-[a-zA-Z]+"           # Options like -la, -l, -a
        - "/[a-zA-Z0-9/_.-]+"    # Absolute paths
        - "[a-zA-Z0-9_.-]+"      # Relative paths
      ffmpeg:
        - "-[a-zA-Z]:[a-zA-Z0-9]+"         # Options like -c:v, -c:a
        - "-[a-zA-Z]+"                     # Flags like -i
        - "[a-zA-Z0-9][a-zA-Z0-9_./+-]*"   # Filenames and values
```

### Disabling Security (Not Recommended)

For testing or development purposes, you can disable command validation:

```yaml
command:
  security:
    enabled: false
```

**WARNING**: Disabling command validation removes all security checks and should only be done in trusted, isolated environments.

### Security Best Practices

1. **Minimize Allowed Commands**: Only include commands that are absolutely necessary for your application.
2. **Use Strict Patterns**: Define argument patterns that are as restrictive as possible while still allowing legitimate use cases.
3. **Prevent Shell Injection**: Avoid patterns that match shell metacharacters like:
   - `$` (command substitution)
   - `;` (command separator)
   - `|` (pipe)
   - `&` (background execution)
   - `>` or `<` (redirection)
   - `` ` `` (backticks for command substitution)
4. **Regular Review**: Periodically review your allowed commands and patterns to ensure they're still necessary.

### Examples

#### Valid Commands

```json
{
  "command": "echo",
  "arguments": ["Hello World"]
}
```

```json
{
  "command": "ls",
  "arguments": ["-la", "/home"]
}
```

#### Invalid Commands (Will be Rejected)

```json
{
  "command": "rm",
  "arguments": ["-rf", "/"]
}
```
*Rejected: 'rm' is not in the allow-list*

```json
{
  "command": "ls",
  "arguments": ["$(rm -rf /)"]
}
```
*Rejected: Argument contains shell injection attempt*

### Error Handling

When a command fails validation, the API returns:
- **HTTP Status**: 403 Forbidden
- **Response Body**: 
```json
{
  "exitCode": -1,
  "output": ["Security Error", "Command 'rm' is not in the allow-list"]
}
```

### Testing

The security implementation includes comprehensive tests:
- **Unit Tests**: Test the validation logic in isolation
- **Integration Tests**: Test end-to-end command execution with validation
- **Security Tests**: Test that malicious commands are properly blocked

Run the tests with:
```bash
mvn test
```

### Architecture

The security implementation consists of:

1. **CommandSecurityProperties**: Configuration class that reads settings from `application.yml`
2. **CommandValidationService**: Service that performs validation checks
3. **ProcessServiceImpl**: Integrates validation into the command execution flow
4. **ProcessController**: Handles SecurityException and returns appropriate HTTP responses

### Migration Guide

If you have an existing deployment, no changes are required to your code. However, you should:

1. Review the default allowed commands in `application.yml`
2. Add or remove commands based on your needs
3. Define argument patterns for commands that accept user input
4. Test your application thoroughly after enabling the security feature

### Additional Resources

- [OWASP Command Injection](https://owasp.org/www-community/attacks/Command_Injection)
- [Spring Boot Configuration Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
