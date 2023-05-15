package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.cart.CartDto;
import com.hcmute.tdshop.dto.cart.CartItemDto;
import com.hcmute.tdshop.dto.cart.CartProductDto;
import com.hcmute.tdshop.dto.cart.CartProductPromotionDto;
import com.hcmute.tdshop.entity.Cart;
import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.ProductPromotion;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CartMapper {

  public CartDto CartToCartDto(Cart cart) {
    if ( cart == null ) {
      return null;
    }

    CartDto cartDto = new CartDto();

    cartDto.setId( cart.getId() );

    Set<CartItemDto> tempSet = new TreeSet<CartItemDto>(cartItemDtoComparatorASC);
    tempSet.addAll(cartItemSetToCartItemDtoSet( cart.getSetOfCartItems()));
    cartDto.setSetOfCartItems(tempSet);

    return cartDto;
  }
  public abstract Set<CartItemDto> cartItemSetToCartItemDtoSet(Set<CartItem> set);
  public abstract CartItemDto CartItemToCartItemDto(CartItem cartItem);
  public CartProductDto ProductToCartProductDto(Product product) {
    if ( product == null ) {
      return null;
    }

    CartProductDto cartProductDto = new CartProductDto();

    cartProductDto.setId( product.getId() );
    cartProductDto.setSku( product.getSku() );
    cartProductDto.setName( product.getName() );
    cartProductDto.setImageUrl( product.getImageUrl() );
    cartProductDto.setPrice( doubleToString( product.getPrice() ) );
    cartProductDto.setProductPromotion( ProductPromotionToCartProductPromotionDto( getCurrentPromotion(product)) );
    cartProductDto.setTotal(product.getTotal());

    return cartProductDto;
  }
  public abstract CartProductPromotionDto ProductPromotionToCartProductPromotionDto(ProductPromotion productPromotion);
  public String doubleToString(double d) {
    return new BigDecimal(d).toPlainString();
  }

  private ProductPromotion getCurrentPromotion(Product product) {
    ProductPromotion promotion = null;
    LocalDateTime now = LocalDateTime.now();
    for (ProductPromotion productPromotion : product.getSetOfProductPromotions()) {
      if (isBeforeOrEqual(productPromotion.getStartDate(), now) && isAfterOrEqual(productPromotion.getEndDate(), now)) {
        promotion = productPromotion;
        break;
      }
    }
    return promotion;
  }

  private boolean isBeforeOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isBefore(date2) || date1.isEqual(date2);
  }

  private boolean isAfterOrEqual(LocalDateTime date1, LocalDateTime date2){
    return date1.isAfter(date2) || date1.isEqual(date2);
  }

  Comparator<CartItemDto> cartItemDtoComparatorASC = new Comparator<CartItemDto>() {
    @Override
    public int compare(CartItemDto o1, CartItemDto o2) {
      long result = o1.getId() - o2.getId();
      if (result > 0) {
        return 1;
      }
      else if (result < 0) {
        return -1;
      }
      return  0;
    }
  };
}
