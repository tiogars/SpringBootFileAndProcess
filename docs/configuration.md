# Configuration

SpringBootFileAndProcess can be configured through the `application.yml` file located in `src/main/resources/`.

## Server Configuration

### Port Configuration

By default, the application runs on port 8181. You can change this by modifying the `application.yml`:

```yaml
server:
  port: 8181
```

To use a different port, update the value:

```yaml
server:
  port: 8080
```

## Application Configuration

### Application Name

The application name is used for identification in logs and monitoring:

```yaml
spring:
  application:
    name: SpringBootFileAndProcess
```

## Logging Configuration

### Log Levels

The application uses different log levels for different components:

```yaml
logging:
  level:
    root: INFO
    fr.tiogars.springbootfileandprocess: DEBUG
```

- `root: INFO` - Sets the default log level for all libraries to INFO
- `fr.tiogars.springbootfileandprocess: DEBUG` - Sets DEBUG level for application code

Available log levels (from least to most verbose):
- `ERROR` - Only error messages
- `WARN` - Warnings and errors
- `INFO` - Informational messages, warnings, and errors
- `DEBUG` - Debug information (useful for development)
- `TRACE` - Very detailed debug information

### Log Format

The console log format can be customized:

```yaml
logging:
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

This format includes:
- Date and time
- Thread name
- Log level
- Logger name
- Log message

### Log File

Logs are written to a file:

```yaml
logging:
  file:
    name: logs/springbootfileandprocess.log
```

The log file will be created in the `logs` directory relative to the application's working directory.

## CORS Configuration

The application is configured to allow cross-origin requests from any origin. This is set in the controller classes with:

```java
@CrossOrigin(origins = { "*" })
```

!!! warning
    In production environments, you should restrict CORS to specific origins for security reasons. Modify the `@CrossOrigin` annotations in the controller classes to specify allowed origins.

## Spring Actuator

The application includes Spring Boot Actuator for monitoring and management. Actuator endpoints are available at:

```
http://localhost:8181/actuator
```

Common actuator endpoints:
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Application metrics

## Environment Variables

You can override configuration properties using environment variables:

```bash
# Set server port
export SERVER_PORT=9090

# Set log level
export LOGGING_LEVEL_ROOT=DEBUG

# Run application
java -jar spring-boot-file-and-process.jar
```

Or pass them as command-line arguments:

```bash
java -jar spring-boot-file-and-process.jar --server.port=9090 --logging.level.root=DEBUG
```

## Production Considerations

For production deployments, consider:

1. **Restrict CORS**: Limit cross-origin access to trusted domains
2. **Log Levels**: Use INFO or WARN level logging to reduce log volume
3. **File Paths**: Ensure the application has appropriate file system permissions
4. **Process Execution**: Validate and sanitize command inputs to prevent security issues
5. **Monitoring**: Enable and configure Spring Actuator endpoints appropriately
