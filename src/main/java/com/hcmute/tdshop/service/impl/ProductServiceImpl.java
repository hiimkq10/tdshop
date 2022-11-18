package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.product.SimpleProductDto;
import com.hcmute.tdshop.entity.Product;
import com.hcmute.tdshop.mapper.ProductMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.ProductService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductMapper productMapper;

  @Override
  public DataResponse getAllProducts(int page) {
    Page<Product> pageOfProducts = productRepository.findAll(
        PageRequest.of(page, ApplicationConstants.PRODUCT_PAGE_SIZE));
    Page<SimpleProductDto> pageOfSimpleProduct = pageOfProducts.map(productMapper::ProductToSimpleProductDto);
    return new DataResponse(pageOfSimpleProduct);
  }
}
