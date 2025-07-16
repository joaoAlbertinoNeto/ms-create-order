#!/bin/bash

echo "=== Teste de Token JWT para MS Order ==="
echo ""

# Obter token
echo "1. Obtendo token de acesso..."
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:33165/realms/ms-order/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=ms-order-client" \
  -d "username=testuser" \
  -d "password=password123")

ACCESS_TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.access_token')

if [ "$ACCESS_TOKEN" = "null" ] || [ -z "$ACCESS_TOKEN" ]; then
    echo "❌ Erro ao obter token:"
    echo $TOKEN_RESPONSE | jq .
    exit 1
fi

echo "✅ Token obtido com sucesso!"
echo ""

# Decodificar JWT (sem verificar assinatura)
echo "2. Decodificando JWT token..."
JWT_PAYLOAD=$(echo $ACCESS_TOKEN | cut -d'.' -f2 | base64 -d 2>/dev/null | jq .)

echo "📋 Claims do JWT:"
echo $JWT_PAYLOAD | jq .

echo ""

# Testar endpoint com token
echo "3. Testando endpoint /order/create..."
RESPONSE=$(curl -s -w "\nHTTP Status: %{http_code}\n" -X POST http://localhost:8081/order/create \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "TEST001",
    "customerEmail": "test@example.com",
    "customerTelephoneNumber": "11999999999",
    "status": "PENDING",
    "createdAt": "2024-01-01T10:00:00"
  }')

echo "📡 Resposta do endpoint:"
echo "$RESPONSE"

echo ""

# Testar endpoint de health (deve funcionar sem token)
echo "4. Testando endpoint /actuator/health (sem token)..."
HEALTH_RESPONSE=$(curl -s -w "\nHTTP Status: %{http_code}\n" http://localhost:8081/actuator/health)

echo "📡 Resposta do health check:"
echo "$HEALTH_RESPONSE"

echo ""
echo "=== Fim do teste ===" 