# API Testing Guide - cURL Commands

## Base URL
```
http://localhost:8080
```

---

## 1. PRODUCTS API

### 1.1 Get All Products
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Content-Type: application/json"
```

### 1.2 Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD-001",
    "name": "Laptop Dell XPS",
    "description": "High-performance laptop for professionals",
    "price": 1299.99,
    "weight": 1.8,
    "unitSize": 0.5
  }'
```

### 1.3 Get Product by ID
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json"
```

### 1.4 Update Product
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD-001",
    "name": "Laptop Dell XPS 15",
    "description": "Updated laptop description",
    "price": 1399.99,
    "weight": 1.9,
    "unitSize": 0.5
  }'
```

### 1.5 Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json"
```

---

## 2. WAREHOUSES API

### 2.1 Get All Warehouses
```bash
curl -X GET http://localhost:8080/api/warehouses \
  -H "Content-Type: application/json"
```

### 2.2 Create Warehouse
```bash
curl -X POST http://localhost:8080/api/warehouses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WH-001",
    "name": "Jakarta Main Warehouse",
    "location": "Jakarta Timur",
    "capacity": 10000.0,
    "description": "Main distribution center",
    "active": true
  }'
```

### 2.3 Get Warehouse by ID
```bash
curl -X GET http://localhost:8080/api/warehouses/1 \
  -H "Content-Type: application/json"
```

### 2.4 Update Warehouse
```bash
curl -X PUT http://localhost:8080/api/warehouses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WH-001",
    "name": "Jakarta Main Warehouse - Updated",
    "location": "Jakarta Timur",
    "capacity": 12000.0,
    "description": "Main distribution center with expanded capacity",
    "active": true
  }'
```

### 2.5 Delete Warehouse
```bash
curl -X DELETE http://localhost:8080/api/warehouses/1 \
  -H "Content-Type: application/json"
```

---

## 3. STOCKS API

### 3.1 Get All Stocks
```bash
curl -X GET http://localhost:8080/api/stocks \
  -H "Content-Type: application/json"
```

### 3.2 Create Stock
```bash
curl -X POST http://localhost:8080/api/stocks \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "productId": 1,
    "quantity": 50,
    "reservedQuantity": 0,
    "minimalStock": 20
  }'
```

### 3.3 Get Stock by ID
```bash
curl -X GET http://localhost:8080/api/stocks/1 \
  -H "Content-Type: application/json"
```

### 3.4 Update Stock
```bash
curl -X PUT http://localhost:8080/api/stocks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "productId": 1,
    "quantity": 75,
    "reservedQuantity": 15,
    "minimalStock": 25
  }'
```

### 3.5 Delete Stock
```bash
curl -X DELETE http://localhost:8080/api/stocks/1 \
  -H "Content-Type: application/json"
```

---

## 4. STOCK MOVEMENT API

### 4.1 Move Stock Between Warehouses
```bash
curl -X POST http://localhost:8080/api/stocks/move \
  -H "Content-Type: application/json" \
  -d '{
    "sourceWarehouseId": 1,
    "destinationWarehouseId": 2,
    "productId": 1,
    "quantity": 20,
    "notes": "Transfer to Surabaya warehouse"
  }'
```

---

## Testing Script (Bash)

### Complete Test Flow
```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

echo "=== Creating Products ==="
PROD1=$(curl -s -X POST $BASE_URL/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD-001",
    "name": "Laptop Dell XPS",
    "description": "High-performance laptop",
    "price": 1299.99,
    "weight": 1.8,
    "unitSize": 0.5
  }' | jq '.id')

echo "Product 1 ID: $PROD1"

echo -e "\n=== Creating Warehouses ==="
WH1=$(curl -s -X POST $BASE_URL/api/warehouses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WH-001",
    "name": "Jakarta Main Warehouse",
    "location": "Jakarta Timur",
    "capacity": 10000.0,
    "description": "Main distribution center",
    "active": true
  }' | jq '.id')

WH2=$(curl -s -X POST $BASE_URL/api/warehouses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WH-002",
    "name": "Surabaya Regional Warehouse",
    "location": "Surabaya",
    "capacity": 5000.0,
    "description": "East Java regional hub",
    "active": true
  }' | jq '.id')

echo "Warehouse 1 ID: $WH1"
echo "Warehouse 2 ID: $WH2"

echo -e "\n=== Creating Stocks ==="
curl -s -X POST $BASE_URL/api/stocks \
  -H "Content-Type: application/json" \
  -d "{
    \"warehouseId\": $WH1,
    \"productId\": $PROD1,
    \"quantity\": 50,
    \"reservedQuantity\": 0,
    \"minimalStock\": 20
  }" | jq .

echo -e "\n=== Getting All Products ==="
curl -s -X GET $BASE_URL/api/products | jq .

echo -e "\n=== Getting All Warehouses ==="
curl -s -X GET $BASE_URL/api/warehouses | jq .

echo -e "\n=== Getting All Stocks ==="
curl -s -X GET $BASE_URL/api/stocks | jq .

echo -e "\n=== Moving Stock Between Warehouses ==="
curl -s -X POST $BASE_URL/api/stocks/move \
  -H "Content-Type: application/json" \
  -d "{
    \"sourceWarehouseId\": $WH1,
    \"destinationWarehouseId\": $WH2,
    \"productId\": $PROD1,
    \"quantity\": 20,
    \"notes\": \"Transfer to Surabaya warehouse\"
  }" | jq .
```

### Run Testing Script
```bash
chmod +x test_api.sh
./test_api.sh
```

---

## Requirements untuk Testing

- `curl` - HTTP client
- `jq` - JSON processor (optional, untuk format output)

### Install jq (Linux)
```bash
sudo apt-get install jq
```

### Install jq (macOS)
```bash
brew install jq
```

### Install jq (Windows)
```powershell
choco install jq
```

---

## Response Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request berhasil |
| 201 | Created - Resource berhasil dibuat |
| 204 | No Content - Sukses tanpa response body |
| 400 | Bad Request - Validasi error |
| 404 | Not Found - Resource tidak ditemukan |
| 409 | Conflict - Data duplikat |
| 500 | Internal Server Error - Server error |

---

## Common Error Examples

### Missing Required Field
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop without SKU"
  }'
```

**Response (400):**
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "sku",
      "message": "SKU is required"
    }
  ]
}
```

### Product Not Found
```bash
curl -X GET http://localhost:8080/api/products/9999
```

**Response (404):**
```json
{
  "status": 404,
  "message": "Product with ID 9999 not found"
}
```
