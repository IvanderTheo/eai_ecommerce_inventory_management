package com.example.inventory.service;

import com.example.inventory.dto.StockRequest;
import com.example.inventory.dto.StockResponse;
import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.entity.*;
import com.example.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private InventoryAlertRepository inventoryAlertRepository;

    private StockResponse convertToResponse(Stock stock) {
        return new StockResponse(
            stock.getId(),
            stock.getWarehouse().getId(),
            stock.getWarehouse().getName(),
            stock.getProduct().getId(),
            stock.getProduct().getName(),
            stock.getQuantity(),
            stock.getReservedQuantity(),
            stock.getAvailableQuantity(),
            stock.getMinimalStock(),
            stock.getMaximalStock(),
            stock.getLocation()
        );
    }

    private StockMovementResponse convertMovementToResponse(StockMovement movement) {
        return new StockMovementResponse(
            movement.getId(),
            movement.getWarehouse().getId(),
            movement.getWarehouse().getName(),
            movement.getProduct().getId(),
            movement.getProduct().getName(),
            movement.getMovementType(),
            movement.getQuantity(),
            movement.getMovementDate(),
            movement.getReference(),
            movement.getNotes()
        );
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    public StockResponse getStockById(Long id) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
        return convertToResponse(stock);
    }

    public StockResponse createStock(StockRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
            .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + request.getWarehouseId()));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        Stock stock = new Stock(
            warehouse,
            product,
            request.getQuantity(),
            request.getMinimalStock(),
            request.getMaximalStock()
        );
        stock.setLocation(request.getLocation());
        Stock savedStock = stockRepository.save(stock);

        if (request.getQuantity() < request.getMinimalStock()) {
            InventoryAlert alert = new InventoryAlert(
                warehouse,
                product,
                "LOW_STOCK",
                "Stock level " + request.getQuantity() + " is below minimal stock " + request.getMinimalStock()
            );
            inventoryAlertRepository.save(alert);
        }

        return convertToResponse(savedStock);
    }

    public StockResponse updateStock(Long id, StockRequest request) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
        stock.setQuantity(request.getQuantity());
        stock.setMinimalStock(request.getMinimalStock());
        stock.setMaximalStock(request.getMaximalStock());
        stock.setLocation(request.getLocation());
        Stock updatedStock = stockRepository.save(stock);
        return convertToResponse(updatedStock);
    }

    @Transactional
    public StockMovementResponse moveStock(StockMovementRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
            .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + request.getWarehouseId()));

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        Stock stock = stockRepository.findAll().stream()
            .filter(s -> s.getWarehouse().getId().equals(request.getWarehouseId()) &&
                         s.getProduct().getId().equals(request.getProductId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Stock not found for warehouse and product"));

        if (request.getMovementType().equals("OUT")) {
            if (stock.getAvailableQuantity() < request.getQuantity()) {
                throw new RuntimeException("Insufficient stock. Available: " + stock.getAvailableQuantity());
            }
            stock.setQuantity(stock.getQuantity() - request.getQuantity());
        } else if (request.getMovementType().equals("IN")) {
            stock.setQuantity(stock.getQuantity() + request.getQuantity());
        }

        Stock updatedStock = stockRepository.save(stock);

        StockMovement movement = new StockMovement(
            warehouse,
            product,
            request.getMovementType(),
            request.getQuantity(),
            request.getReference()
        );
        movement.setNotes(request.getNotes());
        StockMovement savedMovement = stockMovementRepository.save(movement);

        if (updatedStock.getQuantity() < updatedStock.getMinimalStock()) {
            InventoryAlert alert = new InventoryAlert(
                warehouse,
                product,
                "LOW_STOCK",
                "Stock level " + updatedStock.getQuantity() + " is below minimal stock " + updatedStock.getMinimalStock()
            );
            inventoryAlertRepository.save(alert);
        }

        return convertMovementToResponse(savedMovement);
    }

    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found with id: " + id));
        stockRepository.delete(stock);
    }

    /**
     * Get total stock for a product across all warehouses
     * Used for order validation
     */
    public StockResponse getTotalStockByProductId(Long productId) {
        List<Stock> stocks = stockRepository.findByProductId(productId);
        
        if (stocks.isEmpty()) {
            throw new RuntimeException("No stock found for product with id: " + productId);
        }

        // Return the first stock found (or aggregate if needed)
        // For simplicity, returning first warehouse stock
        return convertToResponse(stocks.get(0));
    }

    /**
     * Check if product has sufficient stock in any warehouse
     */
    public boolean hasSufficientStock(Long productId, Integer requiredQuantity) {
        List<Stock> stocks = stockRepository.findByProductId(productId);
        
        return stocks.stream()
            .anyMatch(stock -> stock.getAvailableQuantity() >= requiredQuantity);
    }
}
