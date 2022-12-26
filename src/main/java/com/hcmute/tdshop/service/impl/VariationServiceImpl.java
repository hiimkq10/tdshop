package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.variation.AddVariationRequest;
import com.hcmute.tdshop.dto.variation.UpdateVariationRequest;
import com.hcmute.tdshop.entity.MasterCategory;
import com.hcmute.tdshop.entity.Variation;
import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.mapper.VariationMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.MasterCategoryRepository;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.repository.VariationRepository;
import com.hcmute.tdshop.service.VariationService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VariationServiceImpl implements VariationService {

  @Autowired
  private VariationRepository variationRepository;

  @Autowired
  private VariationOptionRepository variationOptionRepository;

  @Autowired
  private VariationMapper variationMapper;

  @Autowired
  private MasterCategoryRepository masterCategoryRepository;

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<Variation> pageOfVariations = variationRepository.findAll(pageable);
    return new DataResponse(pageOfVariations);
  }

  @Override
  public DataResponse getByMasterCategory(long id) {
    List<Variation> listOfVariations = variationRepository.findByMasterCategory_Id(id);
    return new DataResponse(listOfVariations);
  }

  @Override
  public DataResponse insertVariation(AddVariationRequest request) {
    Variation variation = variationMapper.AddVariationRequestToVariation(request);
    Optional<MasterCategory> optionalMasterCategory = masterCategoryRepository.findById(request.getMasterCategoryId());
    if (optionalMasterCategory.isPresent()) {
      variation.setMasterCategory(optionalMasterCategory.get());
      if (!checkIfNameExisted(variation.getName(), variation.getMasterCategory().getId())) {
        variation = variationRepository.save(variation);
        return DataResponse.SUCCESSFUL;
      }
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_NAME_EXISTED,
          ApplicationConstants.BAD_REQUEST_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse updateVariation(long id, UpdateVariationRequest request) {
    Variation variationToUpdate = variationMapper.UpdateVariationRequestToVariation(request);
    Optional<Variation> optionalVariation = variationRepository.findById(id);
    if (optionalVariation.isPresent()) {
      Variation currentVariation = optionalVariation.get();
      if (variationToUpdate.getName() != null) {
        if (!checkIfNameExisted(variationToUpdate.getName(), currentVariation.getMasterCategory().getId())) {
          currentVariation.setName(variationToUpdate.getName());
        }
      }
      Set<String> tempsetOfVariationOptionValues = request.getSetOfVarirationOptionValues();
      Set<String> setOfVariationOptionValues = request.getSetOfVarirationOptionValues().stream()
          .map(String::toLowerCase).collect(
              Collectors.toSet());
      Iterator<VariationOption> variationOptionIterator = currentVariation.getSetOfVariationOptions().iterator();
      VariationOption variationOption;
      while (variationOptionIterator.hasNext()) {
        variationOption = variationOptionIterator.next();
        if (setOfVariationOptionValues.contains(variationOption.getValue().toLowerCase())) {
          setOfVariationOptionValues.remove(variationOption.getValue().toLowerCase());
        } else {
          variationOptionIterator.remove();
        }
      }
      for (String value : tempsetOfVariationOptionValues) {
        if (setOfVariationOptionValues.contains(value.toLowerCase())) {
          currentVariation.getSetOfVariationOptions().add(new VariationOption(null, value, currentVariation, null));
        }
      }
      variationRepository.saveAndFlush(currentVariation);
      return DataResponse.SUCCESSFUL;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteVariation(long id) {
    variationRepository.deleteById(id);
    return new DataResponse(ApplicationConstants.VARIATION_DELETE_SUCCESSFULLY, true);
  }

  private boolean checkIfNameExisted(String name, long masterCategoryId) {
    return variationRepository.existsByNameIgnoreCaseAndMasterCategory_Id(name, masterCategoryId);
  }
}
