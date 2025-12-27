# File Management API

The File Management API provides endpoints for browsing and retrieving information about files and directories.

## Endpoints

### List Files

Lists files and directories in a specified directory.

**Endpoint:** `GET /file/list`

**Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| directoryParam | string | Yes | The absolute path to the directory to list |

**Request Example:**

```bash
curl "http://localhost:8181/file/list?directoryParam=/home/user/documents"
```

**Response:**

Returns an array of `FileInfo` objects.

**Response Schema:**

```json
[
  {
    "path": "/home/user/documents/file.txt",
    "name": "file.txt",
    "size": 1024,
    "lastModified": 1672531200000,
    "directory": false,
    "file": true,
    "hidden": false
  }
]
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| path | string | Full absolute path to the file or directory |
| name | string | Name of the file or directory |
| size | number | Size of the file in bytes (0 for directories) |
| lastModified | number | Last modification timestamp in milliseconds since epoch |
| directory | boolean | Whether the item is a directory |
| file | boolean | Whether the item is a regular file |
| hidden | boolean | Whether the item is hidden |

**Success Response:**

- **Code:** 200 OK
- **Content:** Array of FileInfo objects

**Error Responses:**

- **Code:** 400 Bad Request
  - **Reason:** Invalid directory path
  
- **Code:** 403 Forbidden
  - **Reason:** Insufficient permissions to access directory

- **Code:** 404 Not Found
  - **Reason:** Directory does not exist

- **Code:** 500 Internal Server Error
  - **Reason:** Server error while reading directory

## Usage Examples

### List Files in a Directory

**Linux:**
```bash
curl "http://localhost:8181/file/list?directoryParam=/home/user/downloads"
```

**Windows:**
```bash
curl "http://localhost:8181/file/list?directoryParam=C:/Users/user/Downloads"
```

### Using with JavaScript

```javascript
async function listFiles(directory) {
  const response = await fetch(
    `http://localhost:8181/file/list?directoryParam=${encodeURIComponent(directory)}`
  );
  const files = await response.json();
  return files;
}

// Usage
listFiles('/home/user/documents')
  .then(files => {
    files.forEach(file => {
      console.log(`${file.name} - ${file.size} bytes`);
    });
  });
```

### Using with Python

```python
import requests

def list_files(directory):
    url = "http://localhost:8181/file/list"
    params = {"directoryParam": directory}
    response = requests.get(url, params=params)
    return response.json()

# Usage
files = list_files("/home/user/documents")
for file in files:
    print(f"{file['name']} - {file['size']} bytes")
```

## Security Considerations

!!! warning
    - Ensure proper input validation when passing directory paths from user input
    - Be aware of path traversal vulnerabilities (e.g., using `../` in paths)
    - Consider implementing authentication and authorization for file access
    - Restrict access to sensitive system directories
    - Validate that the application has appropriate file system permissions

## CORS Support

The File Management API supports Cross-Origin Resource Sharing (CORS) and accepts requests from any origin. In production, consider restricting this to trusted domains.
