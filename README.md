# SpringBootFileAndProcess

Java Spring Boot File and process management

[Swagger](http://localhost:8181/swagger-ui/index.html)

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