# Examples

This page provides practical examples of using the SpringBootFileAndProcess API in real-world scenarios.

## Example 1: Video Transcoding with FFmpeg

This example demonstrates how to use the Process Management API to transcode video files using FFmpeg.

### Use Case

Convert a video file to a web-friendly format with H.264 video codec and AAC audio codec.

### Request

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "ffmpeg",
    "workingDirectory": "/home/user/videos",
    "commandPath": "/usr/bin",
    "arguments": [
      "-i",
      "input.mp4",
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
      "output.mp4"
    ]
  }'
```

### Explanation

- `-i input.mp4`: Input file
- `-c:v libx264`: Use H.264 video codec
- `-preset fast`: Encoding speed preset
- `-crf 22`: Constant Rate Factor (quality level, lower = better)
- `-c:a aac`: Use AAC audio codec
- `-b:a 192k`: Audio bitrate of 192 kbps

## Example 2: Building a Maven Project

This example shows how to build a Maven project remotely.

### Use Case

Execute Maven build commands on a remote server.

### Request

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "mvn",
    "workingDirectory": "/home/user/my-java-project",
    "commandPath": "/usr/local/bin",
    "arguments": ["clean", "package", "-DskipTests"]
  }'
```

### Response

```json
{
  "exitCode": 0,
  "output": [
    "[INFO] Scanning for projects...",
    "[INFO] ",
    "[INFO] --------------------< com.example:my-project >--------------------",
    "[INFO] Building My Project 1.0.0",
    "[INFO] --------------------------------[ jar ]---------------------------------",
    "[INFO] ",
    "[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ my-project ---",
    "[INFO] Deleting /home/user/my-java-project/target",
    "...",
    "[INFO] BUILD SUCCESS",
    "[INFO] ------------------------------------------------------------------------",
    "[INFO] Total time:  30.123 s",
    "[INFO] Finished at: 2025-01-15T10:30:45Z",
    "[INFO] ------------------------------------------------------------------------"
  ]
}
```

## Example 3: File System Browser

This example demonstrates building a simple web-based file browser using the File Management API.

### HTML/JavaScript Application

```html
<!DOCTYPE html>
<html>
<head>
    <title>File Browser</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .file-list { list-style: none; padding: 0; }
        .file-item { padding: 8px; border-bottom: 1px solid #ddd; cursor: pointer; }
        .file-item:hover { background-color: #f0f0f0; }
        .directory { font-weight: bold; color: #0066cc; }
        .file { color: #333; }
        .path { background-color: #e0e0e0; padding: 10px; margin-bottom: 20px; }
    </style>
</head>
<body>
    <h1>File Browser</h1>
    <div class="path" id="currentPath">/</div>
    <ul class="file-list" id="fileList"></ul>

    <script>
        const API_BASE = 'http://localhost:8181';
        let currentDirectory = '/home/user';

        async function listFiles(directory) {
            const response = await fetch(
                `${API_BASE}/file/list?directoryParam=${encodeURIComponent(directory)}`
            );
            return await response.json();
        }

        async function displayFiles(directory) {
            const files = await listFiles(directory);
            const fileList = document.getElementById('fileList');
            const pathDisplay = document.getElementById('currentPath');
            
            pathDisplay.textContent = directory;
            fileList.innerHTML = '';

            files.forEach(file => {
                const li = document.createElement('li');
                li.className = file.directory ? 'file-item directory' : 'file-item file';
                
                const size = file.directory ? '' : ` (${formatSize(file.size)})`;
                li.textContent = `${file.name}${size}`;
                
                if (file.directory) {
                    li.onclick = () => {
                        currentDirectory = file.path;
                        displayFiles(file.path);
                    };
                }
                
                fileList.appendChild(li);
            });
        }

        function formatSize(bytes) {
            if (bytes === 0) return '0 Bytes';
            const k = 1024;
            const sizes = ['Bytes', 'KB', 'MB', 'GB'];
            const i = Math.floor(Math.log(bytes) / Math.log(k));
            return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
        }

        // Initial load
        displayFiles(currentDirectory);
    </script>
</body>
</html>
```

## Example 4: Batch Image Processing

This example shows how to process multiple images using ImageMagick.

### Python Script

```python
import requests
import json
from pathlib import Path

API_BASE = "http://localhost:8181"

def resize_image(input_file, output_file, width, height):
    """Resize an image using ImageMagick convert command"""
    payload = {
        "command": "convert",
        "workingDirectory": str(Path(input_file).parent),
        "commandPath": "/usr/bin",
        "arguments": [
            input_file,
            "-resize",
            f"{width}x{height}",
            output_file
        ]
    }
    
    response = requests.post(f"{API_BASE}/process/execute", json=payload)
    return response.json()

def batch_resize_images(input_dir, output_dir, width=800, height=600):
    """Resize all images in a directory"""
    # List files in input directory
    response = requests.get(
        f"{API_BASE}/file/list",
        params={"directoryParam": input_dir}
    )
    files = response.json()
    
    # Filter image files
    image_extensions = ['.jpg', '.jpeg', '.png', '.gif']
    images = [f for f in files if any(f['name'].lower().endswith(ext) for ext in image_extensions)]
    
    results = []
    for image in images:
        input_path = image['path']
        output_path = f"{output_dir}/{image['name']}"
        
        print(f"Processing {image['name']}...")
        result = resize_image(input_path, output_path, width, height)
        results.append({
            'file': image['name'],
            'status': 'success' if result['exitCode'] == 0 else 'failed',
            'exitCode': result['exitCode']
        })
    
    return results

# Usage
if __name__ == "__main__":
    results = batch_resize_images(
        input_dir="/home/user/photos/originals",
        output_dir="/home/user/photos/resized",
        width=1920,
        height=1080
    )
    
    for result in results:
        print(f"{result['file']}: {result['status']}")
```

## Example 5: Automated Backup Script

This example demonstrates creating a backup using tar command.

### Request

```bash
curl -X POST http://localhost:8181/process/execute \
  -H "Content-Type: application/json" \
  -d '{
    "command": "tar",
    "workingDirectory": "/home/user",
    "arguments": [
      "-czf",
      "backup-2025-01-15.tar.gz",
      "documents",
      "projects",
      "config"
    ]
  }'
```

### Node.js Script for Automated Daily Backups

```javascript
const axios = require('axios');
const cron = require('node-cron');

const API_BASE = 'http://localhost:8181';

async function createBackup() {
    const date = new Date().toISOString().split('T')[0];
    const backupName = `backup-${date}.tar.gz`;
    
    try {
        const response = await axios.post(`${API_BASE}/process/execute`, {
            command: 'tar',
            workingDirectory: '/home/user',
            arguments: [
                '-czf',
                `/backups/${backupName}`,
                'documents',
                'projects'
            ]
        });
        
        if (response.data.exitCode === 0) {
            console.log(`Backup created successfully: ${backupName}`);
        } else {
            console.error('Backup failed:', response.data.output.join('\n'));
        }
    } catch (error) {
        console.error('Error creating backup:', error.message);
    }
}

// Schedule backup to run daily at 2:00 AM
cron.schedule('0 2 * * *', () => {
    console.log('Starting daily backup...');
    createBackup();
});

console.log('Backup scheduler started');
```

## Example 6: System Monitoring Dashboard

This example shows how to combine multiple APIs to create a monitoring dashboard.

### JavaScript/React Component

```javascript
import React, { useState, useEffect } from 'react';

const API_BASE = 'http://localhost:8181';

function SystemMonitor() {
    const [hostname, setHostname] = useState('');
    const [diskUsage, setDiskUsage] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchSystemInfo();
        const interval = setInterval(fetchSystemInfo, 30000); // Refresh every 30 seconds
        return () => clearInterval(interval);
    }, []);

    async function fetchSystemInfo() {
        try {
            // Get hostname
            const hostnameRes = await fetch(`${API_BASE}/system/hostname`);
            const hostnameText = await hostnameRes.text();
            setHostname(hostnameText);

            // Get disk usage
            const diskRes = await fetch(`${API_BASE}/process/execute`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    command: 'df',
                    workingDirectory: '/',
                    arguments: ['-h', '/']
                })
            });
            const diskData = await diskRes.json();
            setDiskUsage(diskData);
            
            setLoading(false);
        } catch (error) {
            console.error('Error fetching system info:', error);
        }
    }

    if (loading) return <div>Loading...</div>;

    return (
        <div className="system-monitor">
            <h2>System Monitor</h2>
            <div className="info-card">
                <h3>Hostname</h3>
                <p>{hostname}</p>
            </div>
            <div className="info-card">
                <h3>Disk Usage</h3>
                <pre>{diskUsage?.output.join('\n')}</pre>
            </div>
        </div>
    );
}

export default SystemMonitor;
```

## Best Practices

### Error Handling

Always implement proper error handling:

```javascript
async function executeCommand(command) {
    try {
        const response = await fetch('http://localhost:8181/process/execute', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(command)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        
        if (result.exitCode !== 0) {
            console.error('Command failed:', result.output.join('\n'));
            return { success: false, error: result.output };
        }
        
        return { success: true, data: result };
    } catch (error) {
        console.error('Request failed:', error);
        return { success: false, error: error.message };
    }
}
```

### Input Validation

Always validate and sanitize user inputs:

```javascript
function validateDirectory(directory) {
    // Prevent path traversal attacks
    if (directory.includes('..')) {
        throw new Error('Invalid directory path');
    }
    
    // Ensure absolute path
    if (!directory.startsWith('/')) {
        throw new Error('Directory must be an absolute path');
    }
    
    return directory;
}
```

### Rate Limiting

Implement rate limiting for intensive operations:

```javascript
class RateLimiter {
    constructor(maxRequests, timeWindow) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindow;
        this.requests = [];
    }
    
    async execute(fn) {
        const now = Date.now();
        this.requests = this.requests.filter(time => now - time < this.timeWindow);
        
        if (this.requests.length >= this.maxRequests) {
            throw new Error('Rate limit exceeded');
        }
        
        this.requests.push(now);
        return await fn();
    }
}

// Usage: Allow 10 requests per minute
const limiter = new RateLimiter(10, 60000);
```
