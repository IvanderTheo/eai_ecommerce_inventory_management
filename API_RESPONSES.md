# API Response Examples

## 1. Products API

### GET /api/products
**Response (200 OK):**
```json
[
  {
    "id": 1,
    "sku": "PROD-001",
    "name": "Laptop Dell XPS",
    "description": "High-performance laptop for professionals",
    "price": 1299.99,
    "weight": 1.8,
    "unitSize": 0.5
  },
  {
    "id": 2,
    "sku": "PROD-002",
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 29.99,
    "weight": 0.15,
    "unitSize": 0.1
  }
]
```

### POST /api/products
**Request Body:**
```json
{
  "sku": "PROD-003",
  "name": "USB-C Cable",
  "description": "Premium USB-C charging cable",
  "price": 19.99,
  "weight": 0.05,
  "unitSize": 0.05
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "sku": "PROD-003",
  "name": "USB-C Cable",
  "description": "Premium USB-C charging cable",
  "price": 19.99,
  "weight": 0.05,
  "unitSize": 0.05
}
```

### GET /api/products/{id}
**Response (200 OK):**
```json
{
  "id": 1,
  "sku": "PROD-001",
  "name": "Laptop Dell XPS",
  "description": "High-performance laptop for professionals",
  "price": 1299.99,
  "weight": 1.8,
  "unitSize": 0.5
}
```

### PUT /api/products/{id}
**Request Body:**
```json
{
  "sku": "PROD-001",
  "name": "Laptop Dell XPS 15",
  "description": "Updated laptop description",
  "price": 1399.99,
  "weight": 1.9,
  "unitSize": 0.5
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "sku": "PROD-001",
  "name": "Laptop Dell XPS 15",
  "description": "Updated laptop description",
  "price": 1399.99,
  "weight": 1.9,
  "unitSize": 0.5
}
```

---

## 2. Warehouses API

### GET /api/warehouses
**Response (200 OK):**
```json
[
  {
    "id": 1,
    "code": "WH-001",
    "name": "Jakarta Main Warehouse",
    "location": "Jakarta Timur",
    "capacity": 10000.0,
    "usedCapacity": 3500.0,
    "description": "Main distribution center",
    "active": true
  },
  {
    "id": 2,
    "code": "WH-002",
    "name": "Surabaya Regional Warehouse",
    "location": "Surabaya",
    "capacity": 5000.0,
    "usedCapacity": 2100.0,
    "description": "East Java regional hub",
    "active": true
  }
]
```

### POST /api/warehouses
**Request Body:**
```json
{
  "code": "WH-003",
  "name": "Bandung Distribution Center",
  "location": "Bandung",
  "capacity": 7500.0,
  "description": "West Java distribution point",
  "active": true
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "code": "WH-003",
  "name": "Bandung Distribution Center",
  "location": "Bandung",
  "capacity": 7500.0,
  "usedCapacity": 0.0,
  "description": "West Java distribution point",
  "active": true
}
```

### GET /api/warehouses/{id}
**Response (200 OK):**
```json
{
  "id": 1,
  "code": "WH-001",
  "name": "Jakarta Main Warehouse",
  "location": "Jakarta Timur",
  "capacity": 10000.0,
  "usedCapacity": 3500.0,
  "description": "Main distribution center",
  "active": true
}
```

### PUT /api/warehouses/{id}
**Request Body:**
```json
{
  "code": "WH-001",
  "name": "Jakarta Main Warehouse - Updated",
  "location": "Jakarta Timur",
  "capacity": 12000.0,
  "description": "Main distribution center with expanded capacity",
  "active": true
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "code": "WH-001",
  "name": "Jakarta Main Warehouse - Updated",
  "location": "Jakarta Timur",
  "capacity": 12000.0,
  "usedCapacity": 3500.0,
  "description": "Main distribution center with expanded capacity",
  "active": true
}
```

---

## 3. Stocks API

### GET /api/stocks
**Response (200 OK):**
```json
[
  {
    "id": 1,
    "warehouseId": 1,
    "warehouseName": "Jakarta Main Warehouse",
    "productId": 1,
    "productName": "Laptop Dell XPS",
    "quantity": 50,
    "reservedQuantity": 10,
    "minimalStock": 20
  },
  {
    "id": 2,
    "warehouseId": 1,
    "warehouseName": "Jakarta Main Warehouse",
    "productId": 2,
    "productName": "Wireless Mouse",
    "quantity": 200,
    "reservedQuantity": 25,
    "minimalStock": 50
  },
  {
    "id": 3,
    "warehouseId": 2,
    "warehouseName": "Surabaya Regional Warehouse",
    "productId": 1,
    "productName": "Laptop Dell XPS",
    "quantity": 30,
    "reservedQuantity": 5,
    "minimalStock": 20
  }
]
```

### POST /api/stocks
**Request Body:**
```json
{
  "warehouseId": 1,
  "productId": 3,
  "quantity": 100,
  "reservedQuantity": 0,
  "minimalStock": 25
}
```

**Response (201 Created):**
```json
{
  "id": 4,
  "warehouseId": 1,
  "warehouseName": "Jakarta Main Warehouse",
  "productId": 3,
  "productName": "USB-C Cable",
  "quantity": 100,
  "reservedQuantity": 0,
  "minimalStock": 25
}
```

### GET /api/stocks/{id}
**Response (200 OK):**
```json
{
  "id": 1,
  "warehouseId": 1,
  "warehouseName": "Jakarta Main Warehouse",
  "productId": 1,
  "productName": "Laptop Dell XPS",
  "quantity": 50,
  "reservedQuantity": 10,
  "minimalStock": 20
}
```

### PUT /api/stocks/{id}
**Request Body:**
```json
{
  "warehouseId": 1,
  "productId": 1,
  "quantity": 75,
  "reservedQuantity": 15,
  "minimalStock": 25
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "warehouseId": 1,
  "warehouseName": "Jakarta Main Warehouse",
  "productId": 1,
  "productName": "Laptop Dell XPS",
  "quantity": 75,
  "reservedQuantity": 15,
  "minimalStock": 25
}
```

---

## 4. Stock Movement API

### POST /api/stocks/move
**Request Body:**
```json
{
  "sourceWarehouseId": 1,
  "destinationWarehouseId": 2,
  "productId": 1,
  "quantity": 20,
  "notes": "Transfer to Surabaya warehouse"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "sourceWarehouseId": 1,
  "sourceWarehouseName": "Jakarta Main Warehouse",
  "destinationWarehouseId": 2,
  "destinationWarehouseName": "Surabaya Regional Warehouse",
  "productId": 1,
  "productName": "Laptop Dell XPS",
  "quantity": 20,
  "notes": "Transfer to Surabaya warehouse",
  "movementType": "TRANSFER",
  "timestamp": "2026-04-19T16:30:00Z"
}
```

---

## 5. Error Responses

### 400 Bad Request (Validation Error)
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "sku",
      "message": "SKU is required"
    },
    {
      "field": "price",
      "message": "Price must be greater than 0"
    }
  ],
  "timestamp": "2026-04-19T16:30:00Z"
}
```

### 404 Not Found
```json
{
  "status": 404,
  "message": "Product with ID 999 not found",
  "timestamp": "2026-04-19T16:30:00Z"
}
```

### 409 Conflict (Duplicate SKU)
```json
{
  "status": 409,
  "message": "Product with SKU 'PROD-001' already exists",
  "timestamp": "2026-04-19T16:30:00Z"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "An internal server error occurred",
  "timestamp": "2026-04-19T16:30:00Z"
}
```

---

## API Endpoints Summary

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| GET | `/api/products` | List all products | ProductResponse[] |
| POST | `/api/products` | Create new product | ProductResponse |
| GET | `/api/products/{id}` | Get product by ID | ProductResponse |
| PUT | `/api/products/{id}` | Update product | ProductResponse |
| GET | `/api/warehouses` | List all warehouses | WarehouseResponse[] |
| POST | `/api/warehouses` | Create new warehouse | WarehouseResponse |
| GET | `/api/warehouses/{id}` | Get warehouse by ID | WarehouseResponse |
| PUT | `/api/warehouses/{id}` | Update warehouse | WarehouseResponse |
| GET | `/api/stocks` | List all stocks | StockResponse[] |
| POST | `/api/stocks` | Create new stock | StockResponse |
| GET | `/api/stocks/{id}` | Get stock by ID | StockResponse |
| PUT | `/api/stocks/{id}` | Update stock | StockResponse |
| POST | `/api/stocks/move` | Move stock between warehouses | StockMovementResponse |
