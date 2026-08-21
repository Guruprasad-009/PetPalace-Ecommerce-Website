package com.core.cloud.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.core.cloud.ecom.entity.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer>{

    Optional<Wishlist> findByUserIdAndProductId(int userId,int productId);

    List<Wishlist> findByUserId(int userId);

}