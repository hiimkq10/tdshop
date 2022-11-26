package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.cart.CartDto;
import com.hcmute.tdshop.dto.cart.CartItemDto;
import com.hcmute.tdshop.dto.cart.CartProductDto;
import com.hcmute.tdshop.dto.cart.CartProductPromotionDto;
import com.hcmute.tdshop.entity.Cart;
import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductPromotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CartMapper {
  public abstract CartDto CartToCartDto(Cart cart);
  public abstract CartItemDto CartItemToCartItemDto(CartItem cartItem);
  public abstract CartProductDto ProductToCartProductDto(Product product);
  public abstract CartProductPromotionDto ProductPromotionToCartProductPromotionDto(ProductPromotion productPromotion);
}
