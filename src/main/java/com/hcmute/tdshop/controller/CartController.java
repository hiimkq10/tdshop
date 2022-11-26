package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.cart.AddProductToCartRequest;
import com.hcmute.tdshop.dto.cart.ChangeProductQuantityRequest;
import com.hcmute.tdshop.dto.cart.RemoveProductFromCartRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {
  @Autowired
  private CartService cartService;

  @GetMapping("/{id}")
  public DataResponse getCartByUserId(@PathVariable(name = "id") long id) {
    return cartService.getCartByUserId(id);
  }

  @PostMapping("/add")
  public DataResponse addProductToCart(@RequestBody AddProductToCartRequest request) {
    return cartService.addProductToCart(request);
  }

  @PostMapping("/change-quantity")
  public DataResponse changeProductQuantity(@RequestBody ChangeProductQuantityRequest request) {
    return cartService.changeProductQuantity(request);
  }

  @PostMapping("/remove")
  public DataResponse removeProductFromCart(@RequestBody RemoveProductFromCartRequest request) {
    return cartService.removeProductFromCart(request);
  }
}
