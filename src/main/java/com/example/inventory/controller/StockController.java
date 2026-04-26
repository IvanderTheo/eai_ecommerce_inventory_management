package com.example.inventory.controller;

import com.example.inventory.dto.StockRequest;
import com.example.inventory.dto.StockResponse;
import com.example.inventory.dto.StockMovementRequest;
import com.example.inventory.dto.StockMovementResponse;
import com.example.inventory.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<StockResponse> getStockByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getTotalStockByProductId(productId));
    }

    @GetMapping("/product/{productId}/check/{quantity}")
    public ResponseEntity<Boolean> checkSufficientStock(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        // DI SINI SALAH KETIK: 'hassufficientStock' (s kecil)
        boolean hasSufficientStock = stockService.hasSufficientStock(productId, quantity);
        return ResponseEntity.ok(hasSufficientStock);
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@Valid @RequestBody StockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockService.createStock(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockResponse> updateStock(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(stockService.updateStock(id, request));
    }

    @PostMapping("/move")
    public ResponseEntity<StockMovementResponse> moveStock(@Valid @RequestBody StockMovementRequest request) {
        return ResponseEntity.ok(stockService.moveStock(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
