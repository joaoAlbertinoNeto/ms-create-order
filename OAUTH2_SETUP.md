# Configuração OAuth2 para MS Order

Este documento explica como configurar e usar a autenticação OAuth2 no microserviço de pedidos.

## Dependências Adicionadas

As seguintes dependências foram adicionadas ao `pom.xml`:

- `spring-boot-starter-security`: Para funcionalidades básicas de segurança
- `spring-boot-starter-oauth2-resource-server`: Para autenticação OAuth2 Resource Server
- `spring-boot-starter-actuator`: Para endpoints de monitoramento e health check
- `spring-security-test`: Para testes de segurança
- `springdoc-openapi-starter-webmvc-ui`: Para documentação da API com suporte a OAuth2

## Configuração

### 1. Configuração do Keycloak (Servidor OAuth2)

Para usar esta aplicação, você precisa de um servidor OAuth2. Recomendamos o Keycloak:

#### Instalação do Keycloak via Docker:

```bash
docker run -p 8080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:latest start-dev
```

#### Configuração Automática:

Execute o script de configuração:

```bash
./setup-keycloak.sh
```

Este script irá:
- Criar o realm "ms-order"
- Criar o client "ms-order-client" com scopes "read" e "write"
- Criar roles "USER" e "ADMIN"
- Criar um usuário de teste "testuser" com senha "password123"

#### Configuração Manual do Realm:

1. Acesse http://localhost:8080
2. Faça login com admin/admin
3. Crie um novo realm chamado "ms-order"
4. Crie um novo client:
   - Client ID: `ms-order-client`
   - Client Protocol: `openid-connect`
   - Access Type: `confidential`
   - Valid Redirect URIs: `http://localhost:8081/*`
   - Web Origins: `http://localhost:8081`
   - Default Client Scopes: `openid`, `profile`, `email`, `read`, `write`

#### Criação de Usuários e Roles:

1. Crie roles: `USER` e `ADMIN`
2. Crie usuários e atribua as roles

### 2. Configuração da Aplicação

Atualize o `application.properties` com as URLs corretas do seu servidor OAuth2:

```properties
# OAuth2 Resource Server Configuration
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/ms-order
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/ms-order/protocol/openid-connect/certs
spring.security.oauth2.resourceserver.jwt.client-id=ms-order-client
```

### 3. Endpoints Protegidos

Os seguintes endpoints agora requerem autenticação OAuth2:

- `POST /order/create` - Criar novo pedido (requer SCOPE_write ou ROLE_USER/ADMIN)
- `GET /order/{id}` - Buscar pedido por ID (requer SCOPE_read ou ROLE_USER/ADMIN)

### 4. Endpoints Públicos

Os seguintes endpoints são públicos:

- `GET /actuator/health` - Health check da aplicação
- `GET /actuator/info` - Informações da aplicação
- `GET /actuator/metrics` - Métricas da aplicação
- `GET /swagger-ui.html` - Documentação da API
- `GET /v3/api-docs` - Especificação OpenAPI

## Como Usar

### 1. Obter Token de Acesso

```bash
curl -X POST http://localhost:8080/realms/ms-order/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=ms-order-client" \
  -d "username=testuser" \
  -d "password=password123"
```

### 2. Usar a API com Token

```bash
curl -X POST http://localhost:8081/order/create \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "ORDER001",
    "customerEmail": "cliente@email.com",
    "customerTelephoneNumber": "11999999999",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }'
```

### 3. Teste Automático

Execute o script de teste para verificar se tudo está funcionando:

```bash
./test-token.sh
```

### 4. Acessar a Documentação

Acesse http://localhost:8081/swagger-ui.html para ver a documentação interativa da API com suporte a autenticação OAuth2.

## Scopes e Roles

A aplicação suporta tanto scopes quanto roles:

### Scopes:
- `SCOPE_read`: Permissão para ler pedidos
- `SCOPE_write`: Permissão para criar pedidos

### Roles:
- `ROLE_USER`: Usuário padrão (pode criar e visualizar pedidos)
- `ROLE_ADMIN`: Administrador (pode criar e visualizar pedidos)

## Troubleshooting

### Erro 403 - Acesso Negado

Se você está recebendo erro 403, verifique:

1. **Token válido**: O token não expirou
2. **Client ID correto**: O token foi gerado para o client correto
3. **Scopes/Roles**: O usuário tem as permissões necessárias

Para debugar, execute:

```bash
./test-token.sh
```

Este script irá:
- Obter um token
- Decodificar o JWT para mostrar os claims
- Testar o endpoint
- Mostrar a resposta completa

### Erro de JWT Inválido

- Verifique se o token não expirou
- Confirme se o issuer URI está correto
- Verifique se o client ID está correto

### Erro de CORS

- A configuração CORS já está incluída na `SecurityConfig`
- Se necessário, ajuste as origens permitidas

## Desenvolvimento Local

Para desenvolvimento sem OAuth2, você pode:

1. Usar o perfil `dev`:
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

2. Ou desabilitar temporariamente a segurança comentando a linha no `application-dev.properties`:
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```

## Testes

Para testar a autenticação, use o `spring-security-test`:

```java
@WithMockUser(roles = "USER")
@Test
void testCreateOrderWithUserRole() {
    // seu teste aqui
}
```

## Monitoramento

### Spring Boot Actuator

A aplicação inclui o Spring Boot Actuator para monitoramento e observabilidade:

- **Health Check**: `GET /actuator/health` - Status da aplicação e dependências
- **Application Info**: `GET /actuator/info` - Informações da aplicação
- **Metrics**: `GET /actuator/metrics` - Métricas da aplicação
- **Prometheus**: `GET /actuator/prometheus` - Métricas no formato Prometheus

### Prometheus

O Prometheus está configurado no `docker-compose.yml` para coletar métricas automaticamente:

- **URL**: http://localhost:9090
- **Configuração**: `prometheus.yml`
- **Retenção**: 200 horas

### Exemplos de Uso

```bash
# Health check
curl http://localhost:8081/actuator/health

# Informações da aplicação
curl http://localhost:8081/actuator/info

# Métricas HTTP
curl http://localhost:8081/actuator/metrics/http.server.requests

# Métricas Prometheus
curl http://localhost:8081/actuator/prometheus
``` 