package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.category.AddCategoryRequest;
import com.hcmute.tdshop.dto.category.CategoryResponse;
import com.hcmute.tdshop.dto.category.UpdateCategoryRequest;
import com.hcmute.tdshop.entity.Category;
import com.hcmute.tdshop.entity.MasterCategory;
import com.hcmute.tdshop.mapper.CategoryMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.MasterCategoryRepository;
import com.hcmute.tdshop.repository.ProductRepository;
import com.hcmute.tdshop.service.CategoryService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  MasterCategoryRepository masterCategoryRepository;

  @Autowired
  CategoryMapper categoryMapper;

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<Category> pageOfCategories = categoryRepository.findAll(pageable);
    Page<CategoryResponse> pageOfCategoryResponses = new PageImpl<>(
        categoryMapper.ListCategoriesToListCategoryResponses(pageOfCategories.getContent()),
        pageable,
        pageOfCategories.getTotalElements()
    );
    return new DataResponse(pageOfCategoryResponses);
  }

  @Override
  public DataResponse getById(long id) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if (optionalCategory.isPresent()) {
      Category category = optionalCategory.get();
      return new DataResponse(categoryMapper.CategoryToCategoryResponse(category));
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_NOT_FOUND, ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse insertCategory(AddCategoryRequest request) {
    Category category = categoryMapper.AddCategoryRequestToCategory(request);
    if (checkIfNameExisted(category.getName())) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_NAME_EXISTED,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    Optional<MasterCategory> optionalMasterCategory = masterCategoryRepository.findById(request.getMasterCategoryId());
    if (optionalMasterCategory.isPresent()) {
      category.setMasterCategory(optionalMasterCategory.get());
      if (request.getParentCategoryId() > 0) {
        Optional<Category> optionalParentCategory = categoryRepository.findById(request.getParentCategoryId());
        if (optionalParentCategory.isPresent()) {
          Category parentCategory = optionalParentCategory.get();
          if (parentCategory.getParent() != null) {
            category.setParent(optionalParentCategory.get());
          } else {
            return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_TWO_LEVEL_ERROR,
                ApplicationConstants.BAD_REQUEST_CODE);
          }
        }
      }
      categoryRepository.save(category);
      return DataResponse.SUCCESSFUL;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse updateCategory(long id, UpdateCategoryRequest request) {
    Category categoryToUpdate = categoryMapper.UpdateCategoryRequestToCategory(request);
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if (optionalCategory.isPresent()) {
      Category currentCategory = optionalCategory.get();
      if (categoryToUpdate.getName() != null) {
        if (checkIfNameExisted(categoryToUpdate.getName())) {
          currentCategory.setName(categoryToUpdate.getName());
        }
      }
      if (request.getParentCategoryId() > 0) {
        Optional<Category> optionalParentCategory = categoryRepository.findById(request.getParentCategoryId());
        if (optionalParentCategory.isPresent()) {
          Category parentCategory = optionalParentCategory.get();
          if (parentCategory.getParent() != null) {
            currentCategory.setParent(optionalParentCategory.get());
          } else {
            return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_TWO_LEVEL_ERROR,
                ApplicationConstants.BAD_REQUEST_CODE);
          }
        }
      }
      categoryRepository.saveAndFlush(currentCategory);
      return DataResponse.SUCCESSFUL;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteCategory(long id) {
    if (productRepository.existsByCategory_Id(id) || categoryRepository.existsByParent_Id(id)) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.CATEGORY_RELATED_EXIST,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    categoryRepository.deleteById(id);
    return new DataResponse(ApplicationConstants.CATEGORY_DELETE_SUCCESSFULLY, true);
  }

  private boolean checkIfNameExisted(String name) {
    return categoryRepository.existsByNameIgnoreCase(name);
  }
}
