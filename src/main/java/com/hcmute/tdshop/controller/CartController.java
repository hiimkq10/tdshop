package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.cart.AddProductToCartRequest;
import com.hcmute.tdshop.dto.cart.ChangeProductQuantityRequest;
import com.hcmute.tdshop.dto.cart.CheckCartProductValidRequest;
import com.hcmute.tdshop.dto.cart.RemoveProductFromCartRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.CartService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @GetMapping("/my-cart")
  public DataResponse getCartByUserId() {
    return cartService.getCartByUserId();
  }

  @PostMapping("/add")
  public DataResponse addProductToCart(@RequestBody @Valid AddProductToCartRequest request) {
    return cartService.addProductToCart(request);
  }

  @PostMapping("/change-quantity")
  public DataResponse changeProductQuantity(@RequestBody @Valid ChangeProductQuantityRequest request) {
    return cartService.changeProductQuantity(request);
  }

  @PostMapping("/remove")
  public DataResponse removeProductFromCart(@RequestBody @Valid RemoveProductFromCartRequest request) {
    return cartService.removeProductFromCart(request);
  }

  @PostMapping("/check-product")
  public DataResponse checkCartProductValid(@RequestBody @Valid CheckCartProductValidRequest request) {
    return cartService.checkCartProductValid(request);
  }
}
