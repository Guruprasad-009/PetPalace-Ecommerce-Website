package com.core.cloud.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.core.cloud.ecom.entity.Cart;

@Repository

public interface CartRepository extends JpaRepository<Cart, Integer>
{
	Optional<Cart> findByUserIdAndProductId (int userId, int productId);
	
	List<Cart> findByUserId (int userId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.userId=:userId")
	void deleteByUserId(@Param("userId") int userId);

}
