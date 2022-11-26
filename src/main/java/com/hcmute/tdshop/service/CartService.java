package com.hcmute.tdshop.service;

import com.hcmute.tdshop.dto.cart.AddProductToCartRequest;
import com.hcmute.tdshop.dto.cart.ChangeProductQuantityRequest;
import com.hcmute.tdshop.dto.cart.RemoveProductFromCartRequest;
import com.hcmute.tdshop.model.DataResponse;

public interface CartService {
  public DataResponse getCartByUserId(long id);
  public DataResponse addProductToCart(AddProductToCartRequest request);
  public DataResponse changeProductQuantity(ChangeProductQuantityRequest request);
  public DataResponse removeProductFromCart(RemoveProductFromCartRequest request);
}
