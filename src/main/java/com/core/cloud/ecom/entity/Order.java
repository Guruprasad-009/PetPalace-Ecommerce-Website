package com.core.cloud.ecom.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Order Number (Example: ORD-20260730-1001)
    private String orderNumber;

    // Customer Name
    private String customer;

    // Product Name
    private String product;

    // Quantity
    private int quantity;

    // Total Price
    private double total;

    // Payment Method
    private String payment;

    // Order Date
    private LocalDate date;

    public Order() {
    }

    public Order(int id, String orderNumber, String customer, String product,
                 int quantity, double total, String payment, LocalDate date) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.total = total;
        this.payment = payment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}