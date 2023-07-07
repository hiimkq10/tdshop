package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.cart.AddProductToCartRequest;
import com.hcmute.tdshop.dto.cart.ChangeProductQuantityRequest;
import com.hcmute.tdshop.dto.cart.CheckCartProductValidRequest;
import com.hcmute.tdshop.dto.cart.RemoveProductFromCartRequest;
import com.hcmute.tdshop.dto.order.OrderProductDto;
import com.hcmute.tdshop.entity.Cart;
import com.hcmute.tdshop.entity.CartItem;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.enums.ProductStatusEnum;
import com.hcmute.tdshop.mapper.CartMapper;
import com.hcmute.tdshop.mapper.ProductMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.CartItemRepository;
import com.hcmute.tdshop.repository.CartRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.CartService;
import com.hcmute.tdshop.utils.AuthenticationHelper;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private CartMapper cartMapper;

  @Autowired
  private Helper helper;

  @Autowired
  private ProductMapper productMapper;

  @Override
  public DataResponse getCartByUserId() {
    long id = AuthenticationHelper.getCurrentLoggedInUserId();
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      Optional<Cart> optionalCart = cartRepository.findByUser_Id(id);
      Cart cart = optionalCart.orElseGet(() -> cartRepository.save(new Cart(null, user, new HashSet<>())));
      return new DataResponse(cartMapper.CartToCartDto(cart));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse addProductToCart(AddProductToCartRequest request) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    long productId = request.getProductId();
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      Optional<Product> optionalProduct = productRepository.findByIdAndStatus_IdNotInAndDeletedAtNull(productId,
          new Long[]{ProductStatusEnum.HIDE.getId()});
      if (optionalProduct.isPresent()) {
        User user = optionalUser.get();
        Product product = optionalProduct.get();
        Optional<Cart> optionalCart = cartRepository.findByUser_Id(userId);
        Cart cart = optionalCart.orElseGet(() -> new Cart(null, user, new HashSet<>()));
        boolean found = false;
        for (CartItem item : cart.getSetOfCartItems()) {
          if (item.getProduct().getId() == productId) {
            found = true;
            item.setQuantity(item.getQuantity() + 1);
          }
        }
        if (!found) {
          CartItem item = new CartItem(null, 1, cart, product);
          cart.getSetOfCartItems().add(item);
        }
        cart = cartRepository.save(cart);
        return new DataResponse(ApplicationConstants.ADD_ITEM_SUCCESSFULLY, cartMapper.CartToCartDto(cart));
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse changeProductQuantity(ChangeProductQuantityRequest request) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    long productId = request.getProductId();
    int quantity = request.getQuantity();
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      if (productRepository.existsByIdAndStatus_IdNotInAndDeletedAtNull(productId,
          new Long[]{ProductStatusEnum.HIDE.getId()})) {
        User user = optionalUser.get();
        Optional<Cart> optionalCart = cartRepository.findByUser_Id(userId);
        Cart cart;
        if (optionalCart.isPresent()) {
          cart = optionalCart.get();
          boolean found = false;
          for (CartItem item : cart.getSetOfCartItems()) {
            if (item.getProduct().getId() == productId) {
              found = true;
              item.setQuantity(quantity);
            }
          }
          if (!found) {
            return new DataResponse(ApplicationConstants.UNEXPECTED_ERROR, cartMapper.CartToCartDto(cart));
          }
        } else {
          cart = new Cart(null, user, new HashSet<>());
          cart = cartRepository.save(cart);
          return new DataResponse(ApplicationConstants.UNEXPECTED_ERROR, cartMapper.CartToCartDto(cart));
        }
        cart = cartRepository.save(cart);
        return new DataResponse(ApplicationConstants.CHANGE_ITEM_QUANTITY_SUCCESSFULLY, cartMapper.CartToCartDto(cart));
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse removeProductFromCart(RemoveProductFromCartRequest request) {
    long userId = AuthenticationHelper.getCurrentLoggedInUserId();
    long productId = request.getProductId();
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      if (productRepository.existsByIdAndStatus_IdNotInAndDeletedAtNull(productId,
          new Long[]{ProductStatusEnum.HIDE.getId()})) {
        User user = optionalUser.get();
        Optional<Cart> optionalCart = cartRepository.findByUser_Id(userId);
        Cart cart;
        if (optionalCart.isPresent()) {
          cart = optionalCart.get();
          Iterator<CartItem> iterator = cart.getSetOfCartItems().iterator();
          CartItem item;
          long id = 0;
          while (iterator.hasNext()) {
            item = iterator.next();
            if (item.getProduct().getId() == productId) {
              id = item.getId();
              iterator.remove();
              break;
            }
          }
          if (id != 0) {
            cartItemRepository.deleteById(id);
          }
        } else {
          cart = cartRepository.save(new Cart(null, user, new HashSet<>()));
        }
        return new DataResponse(ApplicationConstants.REMOVE_ITEM_SUCCESSFULLY, cartMapper.CartToCartDto(cart));
      } else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_NOT_FOUND,
            ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse checkCartProductValid(CheckCartProductValidRequest request) {
    List<Product> products = productRepository.findAllById(
        request.getProducts().stream().map(OrderProductDto::getProductId).collect(Collectors.toList()));
    Map<Long, Integer> quantityMap = new HashMap<>();
    for (OrderProductDto dto : request.getProducts()) {
      quantityMap.put(dto.getProductId(), dto.getQuantity());
    }
    List<Product> invalidProducts = new ArrayList<>();
    for (Product product : products) {
      if (product.getDeletedAt() != null
          || product.getStatus().getId() != (ProductStatusEnum.ONSALE.getId())
          || product.getTotal() < quantityMap.get(product.getId())) {
        invalidProducts.add(product);
      }
    }
    if (invalidProducts.size() > 0) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.PRODUCT_BUY_INVALID,
          invalidProducts.stream().map(productMapper::ProductToSimpleProductDto),
          ApplicationConstants.PRODUCT_BUY_INVALID_CODE);
    }
    return DataResponse.SUCCESSFUL;
  }
}
