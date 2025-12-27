# Getting Started

This guide will help you get SpringBootFileAndProcess up and running on your system.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+** (if building from source)

## Installation

### Option 1: Run from Source with Maven

1. Clone the repository:
   ```bash
   git clone https://github.com/tiogars/SpringBootFileAndProcess.git
   cd SpringBootFileAndProcess
   ```

2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

The application will start on port 8181 by default.

### Option 2: Run from JAR File

1. Download the latest JAR file from [GitHub Packages](https://github.com/tiogars?tab=packages&repo_name=SpringBootFileAndProcess)

2. Run the JAR file:
   ```bash
   java -jar spring-boot-file-and-process-1.0.1-SNAPSHOT.jar
   ```

### Option 3: Using Docker Compose

The project includes a Docker Compose configuration for running the Maven site:

```bash
docker-compose up
```

This will start a web server on port 8080 serving the Maven-generated site documentation.

## Building from Source

To build the application from source:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Verifying the Installation

Once the application is running, you can verify it's working correctly:

1. Open your browser and navigate to:
   ```
   http://localhost:8181/swagger-ui/index.html
   ```

2. You should see the Swagger UI interface with the available API endpoints.

3. Test the system endpoint:
   ```bash
   curl http://localhost:8181/system/hostname
   ```

## Next Steps

- Review the [Configuration](configuration.md) guide to customize the application
- Explore the [API Reference](api/file-management.md) to learn about available endpoints
- Check out [Examples](examples.md) for common use cases
