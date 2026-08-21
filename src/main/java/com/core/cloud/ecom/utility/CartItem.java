package com.core.cloud.ecom.utility;

import com.core.cloud.ecom.entity.Product;

public class CartItem {
	private int cardId;
	private Product product;
	private int quantity;

	public CartItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CartItem(int cardId, Product product, int quantity) {
		super();
		this.cardId = cardId;
		this.product = product;
		this.quantity = quantity;
	}

	public int getCartId() {
	    return cardId;
	}

	public void setCartId(int cartId) {
	    this.cardId = cartId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getTotalPrice() {
		return product.getPrice() * quantity ;
	}
	

}
