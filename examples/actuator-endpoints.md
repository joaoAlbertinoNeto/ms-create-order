# Endpoints do Spring Boot Actuator

O Spring Boot Actuator fornece endpoints de monitoramento e gerenciamento para a aplicação. Todos os endpoints estão disponíveis em `/actuator/*`.

## Endpoints Disponíveis

### 1. Health Check
**Endpoint:** `GET /actuator/health`

Verifica a saúde da aplicação e suas dependências.

**Exemplo de resposta:**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 419430400000,
        "threshold": 10485760
      }
    },
    "mongo": {
      "status": "UP",
      "details": {
        "version": "7.0.0"
      }
    }
  }
}
```

### 2. Application Info
**Endpoint:** `GET /actuator/info`

Retorna informações sobre a aplicação.

**Exemplo de resposta:**
```json
{
  "app": {
    "name": "MS Order Service",
    "description": "Microserviço para gerenciamento de pedidos",
    "version": "1.0.0",
    "team": "Equipe de Desenvolvimento"
  },
  "build": {
    "artifact": "ms_order",
    "name": "ms_order",
    "time": "2024-01-01T10:00:00.000Z",
    "version": "0.0.1",
    "group": "br.com.order"
  }
}
```

### 3. Metrics
**Endpoint:** `GET /actuator/metrics`

Lista todas as métricas disponíveis.

**Exemplo de resposta:**
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.memory.max",
    "process.cpu.usage",
    "http.server.requests",
    "hikaricp.connections"
  ]
}
```

### 4. Specific Metric
**Endpoint:** `GET /actuator/metrics/{metric.name}`

Retorna uma métrica específica.

**Exemplo:** `GET /actuator/metrics/http.server.requests`

```json
{
  "name": "http.server.requests",
  "description": "Number of HTTP server requests",
  "baseUnit": "requests",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 42.0
    }
  ]
}
```

### 5. Prometheus Metrics
**Endpoint:** `GET /actuator/prometheus`

Retorna métricas no formato Prometheus.

**Exemplo de resposta:**
```
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap",id="PS Eden Space"} 1.23456789E8
```

## Como Usar

### Health Check
```bash
curl http://localhost:8081/actuator/health
```

### Application Info
```bash
curl http://localhost:8081/actuator/info
```

### List All Metrics
```bash
curl http://localhost:8081/actuator/metrics
```

### Get Specific Metric
```bash
curl http://localhost:8081/actuator/metrics/http.server.requests
```

### Prometheus Metrics
```bash
curl http://localhost:8081/actuator/prometheus
```

## Configuração

Os endpoints do Actuator são configurados no `application.properties`:

```properties
# Expor endpoints específicos
management.endpoints.web.exposure.include=health,info,metrics,prometheus

# Mostrar detalhes do health check
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Habilitar informações customizadas
management.info.env.enabled=true
management.info.build.enabled=true
```

## Segurança

Todos os endpoints do Actuator são públicos (não requerem autenticação) para facilitar o monitoramento. Em produção, você pode querer proteger alguns endpoints ou usar um proxy reverso com autenticação.

## Integração com Monitoramento

### Prometheus
O endpoint `/actuator/prometheus` pode ser configurado no Prometheus para coleta de métricas:

```yaml
scrape_configs:
  - job_name: 'ms-order'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8081']
```

### Grafana
As métricas podem ser visualizadas no Grafana usando dashboards do Spring Boot.

### Kubernetes
O endpoint `/actuator/health` é usado pelo Kubernetes para health checks:

```yaml
livenessProbe:
  httpGet:
    path: /actuator/health
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 10
readinessProbe:
  httpGet:
    path: /actuator/health
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
``` 