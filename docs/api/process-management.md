# Process Management API

The Process Management API allows you to execute system commands and processes programmatically.

## Endpoints

### Execute Command

Executes a system command and waits for it to complete, returning the output and exit code.

**Endpoint:** `POST /process/execute`

**Request Body:**

The request body should be a JSON object conforming to the `ExecutableCommand` schema.

**Request Schema:**

```json
{
  "command": "string",
  "workingDirectory": "string",
  "commandPath": "string",
  "arguments": ["string"]
}
```

**Request Fields:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| command | string | Yes | The command to execute (e.g., "ffmpeg", "mvn") |
| workingDirectory | string | Yes | The working directory where the command should be executed |
| commandPath | string | No | The full path to the command executable (e.g., "/usr/bin/ffmpeg") |
| arguments | array[string] | No | Array of command-line arguments to pass to the command |

**Response:**

Returns a `CommandResult` object containing the exit code and output.

**Response Schema:**

```json
{
  "exitCode": 0,
  "output": [
    "Line 1 of output",
    "Line 2 of output"
  ]
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| exitCode | number | The exit code returned by the command (0 typically indicates success) |
| output | array[string] | Array of output lines from the command's stdout/stderr |

**Success Response:**

- **Code:** 200 OK
- **Content:** CommandResult object

**Error Responses:**

- **Code:** 400 Bad Request
  - **Reason:** Invalid request body or missing required fields

- **Code:** 500 Internal Server Error
  - **Reason:** Command execution failed or I/O error

## Usage Examples

### Execute ffmpeg Command

This example shows how to execute an ffmpeg command to convert a video file.

**Request:**

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "ffmpeg",
    "workingDirectory": "/path/to/input",
    "commandPath": "/usr/bin",
    "arguments": [
      "-i",
      "/path/to/input/file.mp4",
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
      "/path/to/output/file.mp4"
    ]
  }'
```

**Response:**

```json
{
  "exitCode": 0,
  "output": [
    "ffmpeg version 4.4.2",
    "Input #0, mov,mp4,m4a,3gp,3g2,mj2, from '/path/to/input/file.mp4':",
    "...",
    "video:1234kB audio:567kB subtitle:0kB other streams:0kB global headers:0kB muxing overhead: 1.234567%"
  ]
}
```

### Execute Maven Command

**Linux:**

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "mvn",
    "workingDirectory": "/home/user/my-project",
    "commandPath": "/usr/local/bin",
    "arguments": ["clean", "install"]
  }'
```

**Windows:**

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "mvn.cmd",
    "workingDirectory": "C:/Users/user/my-project",
    "commandPath": "C:/Program Files/Apache/maven/bin",
    "arguments": ["clean", "install"]
  }'
```

### Execute Simple Shell Command

**Request:**

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "ls",
    "workingDirectory": "/home/user",
    "arguments": ["-la"]
  }'
```

### Using with JavaScript

```javascript
async function executeCommand(command, workingDirectory, commandPath, args) {
  const response = await fetch('http://localhost:8181/process/execute', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      command: command,
      workingDirectory: workingDirectory,
      commandPath: commandPath,
      arguments: args
    })
  });
  
  const result = await response.json();
  return result;
}

// Usage
executeCommand('ffmpeg', '/tmp', '/usr/bin', ['-version'])
  .then(result => {
    console.log('Exit code:', result.exitCode);
    console.log('Output:', result.output.join('\n'));
  });
```

### Using with Python

```python
import requests
import json

def execute_command(command, working_directory, command_path=None, arguments=None):
    url = "http://localhost:8181/process/execute"
    payload = {
        "command": command,
        "workingDirectory": working_directory,
        "commandPath": command_path or "",
        "arguments": arguments or []
    }
    
    response = requests.post(url, json=payload)
    return response.json()

# Usage
result = execute_command(
    command="mvn",
    working_directory="/home/user/my-project",
    command_path="/usr/local/bin",
    arguments=["site"]
)

print(f"Exit code: {result['exitCode']}")
for line in result['output']:
    print(line)
```

## Platform-Specific Considerations

### Linux

- Use command names directly (e.g., `"mvn"`, `"ffmpeg"`)
- Use forward slashes in paths (e.g., `/home/user/project`)
- Ensure commands are in PATH or provide full `commandPath`

### Windows

- Use `.cmd` or `.exe` extensions (e.g., `"mvn.cmd"`, `"ffmpeg.exe"`)
- Use either forward slashes or backslashes in paths (e.g., `C:/Users` or `C:\\Users`)
- Be aware of Windows-specific command-line syntax

## Security Considerations

!!! danger "Security Warning"
    Executing arbitrary commands poses significant security risks. Consider the following:
    
    - **Input Validation**: Always validate and sanitize command inputs
    - **Command Whitelist**: Consider maintaining a whitelist of allowed commands
    - **Path Validation**: Validate working directories and command paths
    - **Argument Sanitization**: Sanitize all command arguments to prevent injection attacks
    - **Authentication**: Implement authentication and authorization
    - **Auditing**: Log all command executions for security auditing
    - **Resource Limits**: Consider implementing timeouts and resource limits
    - **Sandboxing**: Consider running commands in a sandboxed environment

## Error Handling

The API handles errors in the following ways:

1. **Command Not Found**: Returns 500 with error message in output
2. **Permission Denied**: Returns 500 with error details
3. **Invalid Working Directory**: Returns 500 with error information
4. **Command Execution Failure**: Returns exit code and error output
5. **Timeout/Interruption**: Returns -1 exit code with error message

## CORS Support

The Process Management API supports Cross-Origin Resource Sharing (CORS) and accepts requests from any origin. In production, consider restricting this to trusted domains.
