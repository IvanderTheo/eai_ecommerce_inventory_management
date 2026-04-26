package com.example.inventory.repository;

import com.example.inventory.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    
    /**
     * Find all stocks for a specific product across all warehouses
     */
    @Query("SELECT s FROM Stock s WHERE s.product.id = :productId")
    List<Stock> findByProductId(@Param("productId") Long productId);
    
    /**
     * Find stock for a specific product in a specific warehouse
     */
    @Query("SELECT s FROM Stock s WHERE s.product.id = :productId AND s.warehouse.id = :warehouseId")
    Optional<Stock> findByProductIdAndWarehouseId(@Param("productId") Long productId, @Param("warehouseId") Long warehouseId);
}
