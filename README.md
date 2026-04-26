# Inventory Management System

## 📋 Project Overview

Inventory Management System adalah REST API untuk mengelola stok barang di multiple warehouses, tracking stock movements, dan alert management.

**Database:** `inventory_management_db`  
**Port:** `8086`

---

## 🚀 Entities

### 1. Warehouse
- Gudang penyimpanan (WH001, WH002, dll)
- Kapasitas, lokasi, deskripsi

### 2. Product
- Produk yang disimpan
- SKU, price, weight, unit size

### 3. Stock
- Stok produk per warehouse
- Quantity, reserved quantity, minimal/maximal stock
- Tracking available quantity

### 4. StockMovement
- Pergerakan stok (IN/OUT)
- Quantity, reference, timestamp
- Types: IN (masuk), OUT (keluar)

### 5. InventoryAlert
- Alert untuk kondisi stok (LOW_STOCK, OVERSTOCK)
- Status: ACTIVE/RESOLVED
- Automatic creation ketika stok < minimal

---

## 🔌 API Endpoints

> **Dokumentasi lengkap dengan JSON examples:** Lihat [API_RESPONSES.md](API_RESPONSES.md)  
> **Testing Guide dengan cURL commands:** Lihat [API_TEST_GUIDE.md](API_TEST_GUIDE.md)

### Products API
- `GET /api/products` - Get all products → ProductResponse[]
- `POST /api/products` - Create new product → ProductResponse
- `GET /api/products/{id}` - Get product by ID → ProductResponse
- `PUT /api/products/{id}` - Update product → ProductResponse
- `DELETE /api/products/{id}` - Delete product

### Warehouses API
- `GET /api/warehouses` - Get all warehouses → WarehouseResponse[]
- `POST /api/warehouses` - Create new warehouse → WarehouseResponse
- `GET /api/warehouses/{id}` - Get warehouse by ID → WarehouseResponse
- `PUT /api/warehouses/{id}` - Update warehouse → WarehouseResponse
- `DELETE /api/warehouses/{id}` - Delete warehouse

### Stocks API
- `GET /api/stocks` - Get all stocks → StockResponse[]
- `POST /api/stocks` - Create new stock → StockResponse
- `GET /api/stocks/{id}` - Get stock by ID → StockResponse
- `PUT /api/stocks/{id}` - Update stock → StockResponse
- `DELETE /api/stocks/{id}` - Delete stock

### Stock Movement API
- `POST /api/stocks/move` - Move stock between warehouses → StockMovementResponse

---

## 📝 Response DTOs

### ProductResponse
```json
{
  "id": 1,
  "sku": "PROD-001",
  "name": "Laptop Dell XPS",
  "description": "High-performance laptop",
  "price": 1299.99,
  "weight": 1.8,
  "unitSize": 0.5
}
```

### WarehouseResponse
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

### StockResponse
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

### StockMovementResponse
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

## 🔄 No Circular References

Semua endpoints mengembalikan **Response DTOs** yang tidak memiliki circular references:
- ✅ Tidak ada nested collections
- ✅ Relasi ditampilkan sebagai ID + Name
- ✅ Serialization aman tanpa infinite loops
- ✅ Response payload lebih ringan

---

## 📖 Documentation Files

- **[API_RESPONSES.md](API_RESPONSES.md)** - Complete JSON response examples untuk semua endpoints
- **[API_TEST_GUIDE.md](API_TEST_GUIDE.md)** - cURL commands untuk testing API

---
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Stocks
- `GET /api/stocks` - Get all stocks
- `GET /api/stocks/{id}` - Get stock by ID
- `POST /api/stocks` - Create new stock
- `PUT /api/stocks/{id}` - Update stock
- `POST /api/stocks/move` - Move stock (IN/OUT)
- `DELETE /api/stocks/{id}` - Delete stock

---

## 💾 Database Schema

```sql
CREATE TABLE warehouses (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL,
  capacity DOUBLE NOT NULL,
  used_capacity DOUBLE DEFAULT 0,
  description TEXT,
  active BOOLEAN DEFAULT true
);

CREATE TABLE products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sku VARCHAR(255) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  weight DOUBLE NOT NULL,
  unit_size DOUBLE NOT NULL,
  description TEXT
);

CREATE TABLE stocks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  warehouse_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity BIGINT NOT NULL,
  reserved_quantity BIGINT DEFAULT 0,
  minimal_stock BIGINT NOT NULL,
  maximal_stock BIGINT NOT NULL,
  location VARCHAR(255),
  UNIQUE (warehouse_id, product_id),
  FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE stock_movements (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  warehouse_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  movement_type VARCHAR(255) NOT NULL,
  quantity BIGINT NOT NULL,
  movement_date DATETIME NOT NULL,
  reference VARCHAR(255) NOT NULL,
  notes TEXT,
  FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE inventory_alerts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  warehouse_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  alert_type VARCHAR(255) NOT NULL,
  status VARCHAR(255) NOT NULL,
  created_date DATETIME NOT NULL,
  resolved_date DATETIME,
  message TEXT,
  FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## 📊 Test Data

**Warehouses:** 3 gudang (Jakarta, Surabaya, Bandung)  
**Products:** 4 produk (Laptop, Mouse, Keyboard, Monitor)  
**Stocks:** 5 stock entries dengan berbagai quantity

---

## 🧪 Testing Examples

### Create Warehouse
```bash
curl -X POST http://localhost:8086/api/warehouses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WH004",
    "name": "Warehouse Medan",
    "location": "Jl. Gatot No. 4, Medan",
    "capacity": 6000,
    "description": "Gudang regional di Medan"
  }'
```

### Create Product
```bash
curl -X POST http://localhost:8086/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "SKU005",
    "name": "Printer HP",
    "price": 3000000,
    "weight": 8.5,
    "unitSize": 0.2,
    "description": "Printer laser multifungsi"
  }'
```

### Create Stock
```bash
curl -X POST http://localhost:8086/api/stocks \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "productId": 1,
    "quantity": 50,
    "minimalStock": 10,
    "maximalStock": 100,
    "location": "A-01-01"
  }'
```

### Move Stock (IN)
```bash
curl -X POST http://localhost:8086/api/stocks/move \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "productId": 1,
    "movementType": "IN",
    "quantity": 25,
    "reference": "PO-2026-001",
    "notes": "Pembelian dari supplier"
  }'
```

### Move Stock (OUT)
```bash
curl -X POST http://localhost:8086/api/stocks/move \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "productId": 1,
    "movementType": "OUT",
    "quantity": 10,
    "reference": "SO-2026-001",
    "notes": "Penjualan ke customer"
  }'
```

### Get All Stocks
```bash
curl -X GET http://localhost:8086/api/stocks
```

---

## ⚠️ Business Logic

- **Available Quantity** = Quantity - Reserved Quantity
- **Low Stock Alert**: Triggered ketika quantity < minimalStock
- **Stock Movement**: OUT hanya bisa jika available quantity >= requested quantity
- **Automatic Alert**: Created saat stock creation atau movement jika kondisi alert terpenuhi

---

## 🎯 Error Handling

All errors return consistent format:
```json
{
  "timestamp": "2026-04-16T10:30:00",
  "status": 400,
  "errors": {
    "fieldName": "Error message"
  }
}
```

Contoh error:
```json
{
  "timestamp": "2026-04-16T10:30:00",
  "status": 500,
  "message": "Insufficient stock. Available: 5"
}
```

---

## 📌 Features

- ✅ Multi-warehouse inventory management
- ✅ Automatic stock movement tracking
- ✅ Low stock alerts
- ✅ Reserved quantity management
- ✅ Location-based stock tracking
- ✅ Transaction support for stock movements
- ✅ Input validation
- ✅ Auto-load test data
