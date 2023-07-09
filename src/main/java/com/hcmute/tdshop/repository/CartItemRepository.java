package com.hcmute.tdshop.repository;

import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  @Query(
      value = "SELECT DISTINCT "
          + "c.user "
          + "FROM CartItem ci INNER JOIN Cart c ON ci.cart.id = c.id "
          + "WHERE ci.product.id = :productId AND c.user.id != :userId "
  )
  List<User> getUserByCartProductId(Long productId, Long userId);
}
