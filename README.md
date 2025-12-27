# SpringBootFileAndProcess

Java Spring Boot File and process management

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

## 

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