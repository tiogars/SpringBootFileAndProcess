# Deployment

This guide covers various deployment options for SpringBootFileAndProcess.

## Prerequisites

Before deploying, ensure you have:

- Java 21 runtime environment
- Sufficient system resources (minimum 512MB RAM recommended)
- Network access to required ports
- Appropriate file system permissions

## Deployment Options

### 1. Standalone JAR Deployment

The simplest deployment method is running the application as a standalone JAR file.

#### Build the Application

```bash
mvn clean package
```

This creates a JAR file in the `target` directory: `spring-boot-file-and-process-1.0.1-SNAPSHOT.jar`

#### Run the Application

```bash
java -jar target/spring-boot-file-and-process-1.0.1-SNAPSHOT.jar
```

#### Run with Custom Configuration

```bash
java -jar spring-boot-file-and-process-1.0.1-SNAPSHOT.jar \
  --server.port=9090 \
  --logging.level.root=INFO
```

#### Run as Background Process

**Linux:**
```bash
nohup java -jar spring-boot-file-and-process-1.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

**With systemd service (recommended for Linux):**

Create a service file `/etc/systemd/system/springboot-fileprocess.service`:

```ini
[Unit]
Description=SpringBootFileAndProcess
After=syslog.target network.target

[Service]
User=springboot
Group=springboot
Type=simple
WorkingDirectory=/opt/springboot-fileprocess
ExecStart=/usr/bin/java -jar /opt/springboot-fileprocess/spring-boot-file-and-process-1.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable springboot-fileprocess
sudo systemctl start springboot-fileprocess
sudo systemctl status springboot-fileprocess
```

### 2. Docker Deployment

#### Create Dockerfile

Create a `Dockerfile` in the project root:

```dockerfile
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="tiogars@gmail.com"
LABEL description="SpringBootFileAndProcess"

WORKDIR /app

# Copy the JAR file
COPY target/spring-boot-file-and-process-*.jar app.jar

# Expose the application port
EXPOSE 8181

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8181/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Build Docker Image

```bash
mvn clean package
docker build -t springboot-fileprocess:latest .
```

#### Run Docker Container

```bash
docker run -d \
  --name springboot-fileprocess \
  -p 8181:8181 \
  -v /path/to/data:/data \
  springboot-fileprocess:latest
```

#### Docker Compose

Create a `docker-compose.yml` file:

```yaml
version: '3.8'

services:
  springboot-fileprocess:
    image: springboot-fileprocess:latest
    container_name: springboot-fileprocess
    ports:
      - "8181:8181"
    volumes:
      - ./data:/data
      - ./logs:/app/logs
    environment:
      - SERVER_PORT=8181
      - LOGGING_LEVEL_ROOT=INFO
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8181/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

Start with Docker Compose:

```bash
docker-compose up -d
```

### 3. Kubernetes Deployment

#### Create Kubernetes Deployment

Create `k8s-deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-fileprocess
  labels:
    app: springboot-fileprocess
spec:
  replicas: 3
  selector:
    matchLabels:
      app: springboot-fileprocess
  template:
    metadata:
      labels:
        app: springboot-fileprocess
    spec:
      containers:
      - name: springboot-fileprocess
        image: springboot-fileprocess:latest
        ports:
        - containerPort: 8181
          name: http
        env:
        - name: SERVER_PORT
          value: "8181"
        - name: LOGGING_LEVEL_ROOT
          value: "INFO"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8181
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8181
          initialDelaySeconds: 20
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: springboot-fileprocess-service
spec:
  selector:
    app: springboot-fileprocess
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8181
  type: LoadBalancer
```

Deploy to Kubernetes:

```bash
kubectl apply -f k8s-deployment.yaml
```

#### Create ConfigMap for Configuration

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: springboot-fileprocess-config
data:
  application.yml: |
    server:
      port: 8181
    spring:
      application:
        name: SpringBootFileAndProcess
    logging:
      level:
        root: INFO
```

### 4. Cloud Platform Deployment

#### AWS Elastic Beanstalk

1. Install the EB CLI:
   ```bash
   pip install awsebcli
   ```

2. Initialize EB application:
   ```bash
   eb init -p "Java 21" springboot-fileprocess
   ```

3. Create environment and deploy:
   ```bash
   eb create springboot-fileprocess-env
   eb deploy
   ```

#### Google Cloud Platform (Cloud Run)

1. Build and push Docker image:
   ```bash
   gcloud builds submit --tag gcr.io/PROJECT_ID/springboot-fileprocess
   ```

2. Deploy to Cloud Run:
   ```bash
   gcloud run deploy springboot-fileprocess \
     --image gcr.io/PROJECT_ID/springboot-fileprocess \
     --platform managed \
     --port 8181 \
     --allow-unauthenticated
   ```

#### Azure App Service

1. Create App Service plan:
   ```bash
   az appservice plan create \
     --name springboot-fileprocess-plan \
     --resource-group myResourceGroup \
     --sku B1 \
     --is-linux
   ```

2. Create web app:
   ```bash
   az webapp create \
     --name springboot-fileprocess \
     --resource-group myResourceGroup \
     --plan springboot-fileprocess-plan \
     --runtime "JAVA:21-java21"
   ```

3. Deploy:
   ```bash
   az webapp deploy \
     --resource-group myResourceGroup \
     --name springboot-fileprocess \
     --src-path target/spring-boot-file-and-process-1.0.1-SNAPSHOT.jar
   ```

## Production Considerations

### Security

1. **Enable HTTPS**: Use reverse proxy (nginx/Apache) or load balancer with SSL/TLS
2. **Restrict CORS**: Modify `@CrossOrigin` annotations to allow only trusted origins
3. **Authentication**: Implement Spring Security for API authentication
4. **Input Validation**: Validate all user inputs, especially file paths and commands
5. **Command Whitelist**: Restrict executable commands to a predefined whitelist
6. **File System Permissions**: Run application with minimal required permissions

### Reverse Proxy Setup (nginx)

Create `/etc/nginx/sites-available/springboot-fileprocess`:

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8181;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # WebSocket support (if needed)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }
}
```

Enable and restart nginx:

```bash
sudo ln -s /etc/nginx/sites-available/springboot-fileprocess /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

### Monitoring and Logging

1. **Application Logs**: Configure log aggregation (ELK stack, Splunk, CloudWatch)
2. **Metrics**: Use Spring Boot Actuator with Prometheus and Grafana
3. **Health Checks**: Monitor `/actuator/health` endpoint
4. **Alerts**: Set up alerts for failures and performance issues

### Performance Tuning

#### JVM Options

```bash
java -Xms512m -Xmx2g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/heapdump.hprof \
  -jar spring-boot-file-and-process-1.0.1-SNAPSHOT.jar
```

#### Application Properties

```yaml
server:
  tomcat:
    threads:
      max: 200
      min-spare: 10
    max-connections: 10000
    accept-count: 100
```

### Backup and Recovery

1. **Application Files**: Regular backups of configuration and data
2. **Database**: If using database, set up regular backups
3. **Logs**: Archive old logs regularly
4. **Disaster Recovery Plan**: Document recovery procedures

### Scaling

#### Horizontal Scaling

- Deploy multiple instances behind a load balancer
- Use session management if needed (Redis, database)
- Ensure stateless application design

#### Load Balancer Configuration

Example with HAProxy:

```
frontend http_front
    bind *:80
    default_backend http_back

backend http_back
    balance roundrobin
    server server1 10.0.0.1:8181 check
    server server2 10.0.0.2:8181 check
    server server3 10.0.0.3:8181 check
```

## Troubleshooting

### Common Issues

1. **Port Already in Use**: Change port in `application.yml` or use `--server.port`
2. **Permission Denied**: Ensure application user has required file system permissions
3. **Out of Memory**: Increase JVM heap size with `-Xmx` option
4. **Connection Refused**: Check firewall rules and network configuration

### Logs

Check logs for errors:

```bash
# Application logs
tail -f logs/springbootfileandprocess.log

# systemd service logs
sudo journalctl -u springboot-fileprocess -f

# Docker logs
docker logs -f springboot-fileprocess
```
