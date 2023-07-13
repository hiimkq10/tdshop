package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.mastercategory.AddMasterCategoryRequest;
import com.hcmute.tdshop.dto.mastercategory.UpdateMasterCategoryRequest;
import com.hcmute.tdshop.entity.MasterCategory;
import com.hcmute.tdshop.mapper.MasterCategoryMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.CategoryRepository;
import com.hcmute.tdshop.repository.MasterCategoryRepository;
import com.hcmute.tdshop.repository.VariationRepository;
import com.hcmute.tdshop.service.MasterCategoryService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MasterCategoryServiceImpl implements MasterCategoryService {

  @Autowired
  MasterCategoryRepository masterCategoryRepository;

  @Autowired
  MasterCategoryMapper masterCategoryMapper;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  VariationRepository variationRepository;

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<MasterCategory> masterCategories = masterCategoryRepository.findAll(pageable);
    return new DataResponse(masterCategories);
  }

  @Override
  public DataResponse getById(long id) {
    Optional<MasterCategory> optionalMasterCategory = masterCategoryRepository.findById(id);
    if (optionalMasterCategory.isPresent()) {
      return new DataResponse(optionalMasterCategory.get());
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse insertMasterCategory(AddMasterCategoryRequest request) {
    MasterCategory masterCategory = masterCategoryMapper.AddMasterCategoryRequestToMasterCategory(request);
    if (checkIfNameExisted(masterCategory.getName())) {
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NAME_EXISTED,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    masterCategory.setCreatedAt(LocalDateTime.now());
    masterCategory = masterCategoryRepository.save(masterCategory);
    return new DataResponse(ApplicationConstants.MASTER_CATEGORY_ADD_SUCCESSFULLY, masterCategory);
  }

  @Override
  public DataResponse updateMasterCategory(long id, UpdateMasterCategoryRequest request) {
    MasterCategory masterCategoryToUpdate = masterCategoryMapper.UpdateMasterCategoryRequestToMasterCategory(request);
    Optional<MasterCategory> optionalMasterCategory = masterCategoryRepository.findById(id);
    if (optionalMasterCategory.isPresent()) {
      MasterCategory currentMasterCategory = optionalMasterCategory.get();
      if (masterCategoryToUpdate.getName() != null && !(masterCategoryToUpdate.getName()
          .equals(currentMasterCategory.getName()))) {
        if (!checkIfNameExisted(masterCategoryToUpdate.getName())) {
          currentMasterCategory.setName(masterCategoryToUpdate.getName());
        } else {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NAME_EXISTED,
              ApplicationConstants.BAD_REQUEST_CODE);
        }
      }
      currentMasterCategory = masterCategoryRepository.save(currentMasterCategory);
      return new DataResponse(ApplicationConstants.MASTER_CATEGORY_UPDATE_SUCCESSFULLY, currentMasterCategory);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteMasterCategory(long id) {
    if (masterCategoryRepository.existsById(id)) {
      masterCategoryRepository.deleteById(id);
      return new DataResponse(ApplicationConstants.MASTER_CATEGORY_DELETE_SUCCESSFULLY, true);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  public boolean checkIfNameExisted(String name) {
    return masterCategoryRepository.existsByNameIgnoreCase(name);
  }
}
