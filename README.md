# MS Order - Microservice de Pedidos

Um microserviço Spring Boot para gerenciamento de pedidos com autenticação OAuth2, MongoDB, Kafka e monitoramento com Prometheus.

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Security + OAuth2 Resource Server**
- **Spring Data MongoDB**
- **Apache Kafka**
- **Keycloak** (Servidor OAuth2)
- **Prometheus** (Monitoramento)
- **Docker & Docker Compose**
- **Lombok**
- **ModelMapper**
- **SpringDoc OpenAPI**

## 📋 Pré-requisitos

- Java 21 ou superior
- Docker e Docker Compose
- Maven 3.6+

## 🛠️ Configuração e Execução

### 1. Clone o repositório

```bash
git clone <repository-url>
cd ms-create-order
```

### 2. Executar com Docker Compose

```bash
docker-compose up -d
```

Este comando irá iniciar:
- **MS Order**: http://localhost:8081
- **Keycloak**: http://localhost:8080
- **MongoDB**: localhost:27017
- **Kafka**: localhost:9092
- **Prometheus**: http://localhost:9090

### 3. Configurar Keycloak (OAuth2)

Execute o script de configuração automática:

```bash
./setup-keycloak.sh
```

Este script irá:
- Criar o realm "ms-order"
- Configurar o client OAuth2
- Criar roles e usuários de teste

### 4. Verificar a instalação

```bash
./test-token.sh
```

## 🔐 Autenticação OAuth2

### Obter Token de Acesso

```bash
curl -X POST http://localhost:8080/realms/ms-order/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=ms-order-client" \
  -d "username=testuser" \
  -d "password=password123"
```

### Usuários de Teste

- **Username**: `testuser`
- **Password**: `password123`
- **Roles**: `USER`, `ADMIN`

## 📚 API Endpoints

### Endpoints Protegidos

#### Criar Pedido
```http
POST /order/create
Authorization: Bearer <token>
Content-Type: application/json

{
  "code": "ORDER001",
  "customerEmail": "cliente@email.com",
  "customerTelephoneNumber": "11999999999",
  "status": "PENDING",
  "createdAt": "2024-01-01T10:00:00"
}
```

#### Buscar Pedido por ID
```http
GET /order/{id}
Authorization: Bearer <token>
```

### Endpoints Públicos

- `GET /actuator/health` - Health check
- `GET /actuator/info` - Informações da aplicação
- `GET /actuator/metrics` - Métricas
- `GET /swagger-ui.html` - Documentação da API
- `GET /v3/api-docs` - Especificação OpenAPI

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
src/main/java/br/com/order/ms_order/
├── application/          # Camada de aplicação
├── domain/              # Camada de domínio
│   └── dto/            # Data Transfer Objects
├── infrastructure/      # Camada de infraestrutura
└── MsOrderApplication.java
```

## 📊 Monitoramento

### Prometheus
- URL: http://localhost:9090
- Métricas disponíveis em: http://localhost:8081/actuator/prometheus

### Spring Boot Actuator
- Health Check: http://localhost:8081/actuator/health
- Métricas: http://localhost:8081/actuator/metrics
- Info: http://localhost:8081/actuator/info

## 🔧 Desenvolvimento

### Executar localmente

```bash
mvn spring-boot:run
```

### Executar testes

```bash
mvn test
```

### Build do projeto

```bash
mvn clean package
```

### Executar com perfil de desenvolvimento

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

## 📝 Estrutura do Pedido

```json
{
  "code": "string",                    // Código do pedido
  "customerEmail": "string",           // Email do cliente
  "customerTelephoneNumber": "string", // Telefone do cliente
  "status": "string",                  // Status do pedido
  "createdAt": "datetime",             // Data de criação
  "uuid": "string"                     // ID único (gerado automaticamente)
}
```

## 🔒 Segurança

### Scopes Suportados
- `SCOPE_read`: Permissão para ler pedidos
- `SCOPE_write`: Permissão para criar pedidos

### Roles Suportados
- `ROLE_USER`: Usuário padrão
- `ROLE_ADMIN`: Administrador

## 🐳 Docker

### Build da imagem

```bash
docker build -t ms-order .
```

### Executar container

```bash
docker run -p 8081:8080 ms-order
```

## 📋 Variáveis de Ambiente

```bash
SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/ms-order
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/ms-order
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://keycloak:8080/realms/ms-order/protocol/openid-connect/certs
```

## 🚨 Troubleshooting

### Erro 403 - Acesso Negado
1. Verifique se o token não expirou
2. Confirme se o client ID está correto
3. Verifique se o usuário tem as permissões necessárias

### Erro de Conexão com MongoDB
1. Verifique se o MongoDB está rodando
2. Confirme a URI de conexão
3. Verifique as credenciais

### Erro de Conexão com Kafka
1. Verifique se o Kafka está rodando
2. Confirme as configurações de broker
3. Verifique se o Zookeeper está disponível

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

## 👥 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para suporte, abra uma issue no repositório ou entre em contato com a equipe de desenvolvimento. 