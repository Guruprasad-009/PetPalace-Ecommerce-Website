package com.core.cloud.ecom.utility;

import com.core.cloud.ecom.entity.Product;

public class WishlistItem {

    private int wishlistId;
    private Product product;

    public WishlistItem() {
    }

    public WishlistItem(int wishlistId, Product product) {
        this.wishlistId = wishlistId;
        this.product = product;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}