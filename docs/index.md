# SpringBootFileAndProcess

Welcome to the documentation for SpringBootFileAndProcess - a Spring Boot application that provides REST API endpoints for file and process management.

## Overview

SpringBootFileAndProcess is a Java-based REST API service built with Spring Boot that enables:

- **File Management**: List and browse files in directories
- **Process Execution**: Execute system commands and processes with control over working directory and arguments
- **System Information**: Retrieve system information such as hostname

## Key Features

### File Management
- Browse directory contents
- Retrieve file information including size, modification dates, and attributes
- Cross-platform support for both Linux and Windows file systems

### Process Management
- Execute arbitrary system commands
- Control working directory for command execution
- Pass command-line arguments
- Capture command output and exit codes
- Support for various command-line tools (ffmpeg, maven, etc.)

### System Information
- Retrieve system hostname
- Access system properties and configuration

## Technology Stack

- **Spring Boot 4.0.1**: Main application framework
- **Java 21**: Programming language
- **Spring Web**: REST API implementation
- **SpringDoc OpenAPI 3.0.0**: API documentation (Swagger)
- **Spring Actuator**: Application monitoring and management
- **Maven**: Build and dependency management

## Quick Links

- [Getting Started](getting-started.md): Installation and running the application
- [API Reference](api/file-management.md): Detailed API endpoint documentation
- [Configuration](configuration.md): Application configuration options
- [Examples](examples.md): Common usage examples
- [Deployment](deployment.md): Deployment options and best practices

## API Documentation

Once the application is running, you can access the interactive Swagger API documentation at:

```
http://localhost:8181/swagger-ui/index.html
```

## License

This project is licensed under the MIT License.

## Support

For issues, questions, or contributions, please visit the [GitHub repository](https://github.com/tiogars/SpringBootFileAndProcess).
