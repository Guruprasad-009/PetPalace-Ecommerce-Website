package com.core.cloud.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.core.cloud.ecom.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByOrderByIdDesc();
    
    List<Order> findByCustomerOrderByIdDesc(String customer);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.date = CURRENT_DATE")
    long countTodaySales();

    @Query("SELECT COALESCE(SUM(o.total),0) FROM Order o WHERE o.date = CURRENT_DATE")
    Double getTotalAmountToday();
}