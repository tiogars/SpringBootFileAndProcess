# SpringBootFileAndProcess

Java Spring Boot File and process management

## 🔒 Security Notice

**Authentication Required:** The process execution endpoints (`/process/**`) now require HTTP Basic authentication to prevent unauthorized command execution. See the [Authentication](#authentication) section below.

## Documentation

📚 **Full documentation is available in the `docs` folder using MkDocs.**

To view the documentation locally:

```bash
pip install mkdocs
mkdocs serve
```

Then open http://127.0.0.1:8000 in your browser.

## Quick Links

- [Swagger API Documentation](http://localhost:8181/swagger-ui/index.html) (when running)
- [Getting Started Guide](docs/getting-started.md)
- [API Reference](docs/api/file-management.md)
- [Configuration](docs/configuration.md)
- [Examples](docs/examples.md)
- [Deployment](docs/deployment.md)

## Authentication

The application uses HTTP Basic authentication to protect sensitive endpoints.

### Default Credentials

- **Username:** `admin`
- **Password:** `admin`

⚠️ **Important:** Change these credentials before deploying to production!

### Configuring Credentials

**Option 1: Environment Variables**
```bash
export SECURITY_USERNAME=your_username
export SECURITY_PASSWORD=your_secure_password
```

**Option 2: application.yml**
```yaml
app:
  security:
    username: your_username
    password: your_secure_password
```

### Protected Endpoints

- `POST /process/execute` - **Requires authentication**
- `GET /actuator/**` (except `/actuator/health`) - **Requires authentication**

### Public Endpoints

- `GET /system/**` - System information (no auth required)
- `GET /file/**` - File management (no auth required)
- `GET /actuator/health` - Health check (no auth required)
- `GET /swagger-ui/**` - API documentation (no auth required)

# Usage

## Start

### From source with Maven

```bash
mvn spring-boot:run
```

### From JAR file

[Download](https://github.com/tiogars?tab=packages&repo_name=SpringBootFileAndProcess)

```bash
java -jar spring-boot-file-and-process-1.0.0.jar
```

## Example: Execute Command with Authentication

```bash
curl -X POST http://localhost:8181/process/execute \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d '{
    "command": "echo",
    "workingDirectory": "/tmp",
    "arguments": ["Hello, World!"]
  }'
```

## Example: Execute ffmpeg Command

```json
{
  "command": "ffmpeg",
  "workingDirectory": "/path/to/input",
  "commandPath": "/usr/bin",
  "arguments": [
    "-i",
    "/path/to/input/file",
    "-c:v",
    "libx264",
    "-preset",
    "fast",
    "-crf",
    "22",
    "-c:a",
    "aac",
    "-b:a",
    "192k",
    "/path/to/output/file"
  ]
}
```

**cURL command:**
```bash
curl -X POST http://localhost:8181/process/execute \
  -u admin:admin \
  -H "Content-Type: application/json" \
  -d @command.json
```