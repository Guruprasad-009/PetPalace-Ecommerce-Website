package com.core.cloud.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.core.cloud.ecom.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByNameContainingIgnoreCase(String keyword);
	
	 List<Product> findTop5ByOrderByIdDesc();
	 
	 @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity = 0")
	    long countOutOfStockProducts();


}
