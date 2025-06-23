#!/bin/bash

echo "Configurando Keycloak para MS Order..."

# Aguarda o Keycloak estar disponível
echo "Aguardando Keycloak estar disponível..."
until curl -s http://localhost:8080 > /dev/null; do
    echo "Aguardando Keycloak..."
    sleep 5
done

echo "Keycloak está disponível!"

# Obtém o token de admin
echo "Obtendo token de admin..."
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/realms/master/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=admin-cli" \
  -d "username=admin" \
  -d "password=admin" | jq -r '.access_token')

if [ "$ADMIN_TOKEN" = "null" ] || [ -z "$ADMIN_TOKEN" ]; then
    echo "Erro ao obter token de admin. Verifique se o Keycloak está rodando."
    exit 1
fi

echo "Token de admin obtido com sucesso!"

# Cria o realm ms-order
echo "Criando realm ms-order..."
curl -s -X POST http://localhost:8080/admin/realms \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "realm": "ms-order",
    "enabled": true,
    "displayName": "MS Order Realm"
  }'

echo "Realm ms-order criado!"

# Cria o client ms-order-client
echo "Criando client ms-order-client..."
curl -s -X POST http://localhost:8080/admin/realms/ms-order/clients \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "ms-order-client",
    "enabled": true,
    "publicClient": false,
    "standardFlowEnabled": true,
    "directAccessGrantsEnabled": true,
    "serviceAccountsEnabled": false,
    "redirectUris": ["http://localhost:8081/*"],
    "webOrigins": ["http://localhost:8081"],
    "defaultClientScopes": ["openid", "profile", "email", "read", "write"]
  }'

echo "Client ms-order-client criado!"

# Obtém o ID do client
CLIENT_ID=$(curl -s http://localhost:8080/admin/realms/ms-order/clients?clientId=ms-order-client \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

# Adiciona os scopes read e write ao client
echo "Adicionando scopes ao client..."
curl -s -X PUT http://localhost:8080/admin/realms/ms-order/clients/$CLIENT_ID/default-client-scopes \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '["openid", "profile", "email", "read", "write"]'

echo "Scopes adicionados ao client!"

# Cria as roles
echo "Criando roles..."
curl -s -X POST http://localhost:8080/admin/realms/ms-order/roles \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "USER",
    "description": "Usuário padrão"
  }'

curl -s -X POST http://localhost:8080/admin/realms/ms-order/roles \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ADMIN",
    "description": "Administrador"
  }'

echo "Roles criadas!"

# Cria um usuário de teste
echo "Criando usuário de teste..."
curl -s -X POST http://localhost:8080/admin/realms/ms-order/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "enabled": true,
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User",
    "credentials": [{
      "type": "password",
      "value": "password123",
      "temporary": false
    }]
  }'

echo "Usuário de teste criado!"

# Obtém o ID do usuário
USER_ID=$(curl -s http://localhost:8080/admin/realms/ms-order/users?username=testuser \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[0].id')

# Obtém o ID da role USER
ROLE_ID=$(curl -s http://localhost:8080/admin/realms/ms-order/roles \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq -r '.[] | select(.name=="USER") | .id')

# Atribui a role USER ao usuário
curl -s -X POST http://localhost:8080/admin/realms/ms-order/users/$USER_ID/role-mappings/realm \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d "[{\"id\":\"$ROLE_ID\",\"name\":\"USER\"}]"

echo "Role USER atribuída ao usuário!"

echo ""
echo "Configuração do Keycloak concluída!"
echo ""
echo "Credenciais de teste:"
echo "Username: testuser"
echo "Password: password123"
echo ""
echo "Para obter um token de acesso:"
echo "curl -X POST http://localhost:8080/realms/ms-order/protocol/openid-connect/token \\"
echo "  -H \"Content-Type: application/x-www-form-urlencoded\" \\"
echo "  -d \"grant_type=password\" \\"
echo "  -d \"client_id=ms-order-client\" \\"
echo "  -d \"username=testuser\" \\"
echo "  -d \"password=password123\""
echo ""
echo "Acesse o Keycloak em: http://localhost:8080"
echo "Login: admin/admin" 