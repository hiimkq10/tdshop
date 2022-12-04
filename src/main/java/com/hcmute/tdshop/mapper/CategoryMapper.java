package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.category.AddCategoryRequest;
import com.hcmute.tdshop.dto.category.CategoryDto;
import com.hcmute.tdshop.dto.category.CategoryResponse;
import com.hcmute.tdshop.dto.category.UpdateCategoryRequest;
import com.hcmute.tdshop.entity.Category;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

  public abstract Category AddCategoryRequestToCategory(AddCategoryRequest request);

  public abstract Category UpdateCategoryRequestToCategory(UpdateCategoryRequest request);

  public List<CategoryResponse> ListCategoriesToListCategoryResponses(List<Category> listOfCategories) {
    if (listOfCategories == null) {
      return null;
    }
    List<CategoryResponse> listOfCategoryResponses = new ArrayList<>();
    for (Category category : listOfCategories) {
      listOfCategoryResponses.add(CategoryToCategoryResponse(category));
    }
    return listOfCategoryResponses;
  }

  public CategoryResponse CategoryToCategoryResponse(Category category) {
    if (category == null) {
      return null;
    }
    CategoryResponse categoryResponse = new CategoryResponse();
    categoryResponse.setId(category.getId());
    categoryResponse.setName(category.getName());
    categoryResponse.setListOfCategories(ListCategoriesToListCategoryDtos(category.getChildren()));
    return categoryResponse;
  }

  public CategoryDto CategoryToCategoryDto(Category category) {
    if (category == null) {
      return null;
    }
    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setId(category.getId());
    categoryDto.setName(category.getName());
    return categoryDto;
  }

  public List<CategoryDto> ListCategoriesToListCategoryDtos(List<Category> listOfCategories) {
    if (listOfCategories == null) {
      return null;
    }
    List<CategoryDto> listOfCategoryDtos = new ArrayList<>();
    for (Category category : listOfCategories) {
      listOfCategoryDtos.add(CategoryToCategoryDto(category));
    }

    return listOfCategoryDtos;
  }
}
