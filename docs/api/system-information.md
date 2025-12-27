# System Information API

The System Information API provides endpoints to retrieve information about the system where the application is running.

## Endpoints

### Get System Hostname

Retrieves the hostname of the system where the application is running.

**Endpoint:** `GET /system/hostname`

**Parameters:** None

**Request Example:**

```bash
curl http://localhost:8181/system/hostname
```

**Response:**

Returns a string containing the system's hostname.

**Response Type:** `text/plain`

**Success Response:**

- **Code:** 200 OK
- **Content:** System hostname as a string

**Response Example:**

```
my-server-hostname
```

**Error Responses:**

- **Code:** 500 Internal Server Error
  - **Reason:** Unable to retrieve system hostname

## Usage Examples

### Get Hostname with cURL

```bash
curl http://localhost:8181/system/hostname
```

### Using with JavaScript

```javascript
async function getSystemHostname() {
  const response = await fetch('http://localhost:8181/system/hostname');
  const hostname = await response.text();
  return hostname;
}

// Usage
getSystemHostname()
  .then(hostname => {
    console.log('System hostname:', hostname);
  })
  .catch(error => {
    console.error('Error fetching hostname:', error);
  });
```

### Using with Python

```python
import requests

def get_system_hostname():
    url = "http://localhost:8181/system/hostname"
    response = requests.get(url)
    return response.text

# Usage
hostname = get_system_hostname()
print(f"System hostname: {hostname}")
```

### Using with Java

```java
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemInfoClient {
    public static String getSystemHostname() throws Exception {
        URL url = new URL("http://localhost:8181/system/hostname");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String hostname = in.readLine();
        in.close();
        
        return hostname;
    }
    
    public static void main(String[] args) {
        try {
            String hostname = getSystemHostname();
            System.out.println("System hostname: " + hostname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Use Cases

### System Monitoring

The hostname endpoint can be used for:

- **Service Discovery**: Identifying which server instance is responding
- **Load Balancing**: Verifying requests are distributed across servers
- **Logging and Debugging**: Correlating logs from different server instances
- **Health Checks**: Verifying the application is running and responsive

### Example: Monitoring Multiple Instances

When running multiple instances of the application, you can use the hostname endpoint to identify which instance is handling requests:

```javascript
async function checkInstances(urls) {
  const results = [];
  
  for (const url of urls) {
    try {
      const response = await fetch(`${url}/system/hostname`);
      const hostname = await response.text();
      results.push({ url, hostname, status: 'online' });
    } catch (error) {
      results.push({ url, hostname: null, status: 'offline' });
    }
  }
  
  return results;
}

// Usage
const instances = [
  'http://server1.example.com:8181',
  'http://server2.example.com:8181',
  'http://server3.example.com:8181'
];

checkInstances(instances).then(results => {
  results.forEach(result => {
    console.log(`${result.url} -> ${result.hostname} [${result.status}]`);
  });
});
```

## Implementation Details

The hostname is retrieved using Java's system properties and network interfaces. The endpoint:

1. Retrieves the computer name from the system
2. Returns it as a plain text response
3. Logs the hostname at INFO level

## CORS Support

The System Information API supports Cross-Origin Resource Sharing (CORS) and accepts requests from any origin. In production, consider restricting this to trusted domains.

## Security Considerations

!!! note
    While the hostname is generally not considered sensitive information, be aware that:
    
    - It may reveal information about your infrastructure
    - In production, consider authentication for all API endpoints
    - Consider rate limiting to prevent abuse
    - Monitor access to system information endpoints
