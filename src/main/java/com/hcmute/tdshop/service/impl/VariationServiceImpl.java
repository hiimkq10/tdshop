package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.variation.AddVariationRequest;
import com.hcmute.tdshop.dto.variation.UpdateVariationRequest;
import com.hcmute.tdshop.dto.variationoption.VariationOptionDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
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
        Set<String> names = variation.getSetOfVariationOptions().stream()
            .map(variationOption -> variationOption.getValue().toLowerCase().trim()).collect(
                Collectors.toSet());
        if (names.size() < variation.getSetOfVariationOptions().size()) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_OPTION_NAME_EXISTED,
              ApplicationConstants.VARIATION_OPTION_NAME_EXISTED_CODE);
        }

        variation = variationRepository.save(variation);
        return new DataResponse(ApplicationConstants.VARIATION_ADD_SUCCESSFULLY, variation);
      }
      return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_NAME_EXISTED,
          ApplicationConstants.VARIATION_NAME_EXISTED_CODE);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.MASTER_CATEGORY_NOT_FOUND,
        ApplicationConstants.MASTER_CATEGORY_NOT_FOUND_CODE);
  }

  @Override
  public DataResponse updateVariation(long id, UpdateVariationRequest request) {
    Variation variationToUpdate = variationMapper.UpdateVariationRequestToVariation(request);
    Optional<Variation> optionalVariation = variationRepository.findById(id);
    if (optionalVariation.isPresent()) {
      Variation currentVariation = optionalVariation.get();
      if (Strings.isNotBlank(variationToUpdate.getName()) && (!variationToUpdate.getName()
          .equals(currentVariation.getName()))) {
        if (checkIfNameExisted(variationToUpdate.getName(), currentVariation.getMasterCategory().getId())) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_NAME_EXISTED,
              ApplicationConstants.BAD_REQUEST_CODE);
        }
        currentVariation.setName(variationToUpdate.getName());
      }

      if (request.getSetOfVarirationOptionValues() != null) {
        Set<VariationOptionDto> variationOptionSet = request.getSetOfVarirationOptionValues();
        Set<String> names = variationOptionSet.stream()
            .map(variationOption -> variationOption.getValue().toLowerCase().trim()).collect(
                Collectors.toSet());
        if (names.size() < variationOptionSet.size()) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_OPTION_NAME_EXISTED,
              ApplicationConstants.VARIATION_OPTION_NAME_EXISTED_CODE);
        }
        Map<Long, VariationOptionDto> variationOptionWithId = new HashMap<>();
        List<VariationOptionDto> variationOptionWithOutId = new ArrayList<>();
        variationOptionSet.forEach(variationOptionDto -> {
          if (variationOptionDto.getId() == null || variationOptionDto.getId() == 0) {
            variationOptionWithOutId.add(variationOptionDto);
          } else {
            variationOptionWithId.put(variationOptionDto.getId(), variationOptionDto);
          }
        });
        Iterator<VariationOption> iterator = currentVariation.getSetOfVariationOptions().iterator();
        VariationOption variationOption;
        VariationOptionDto variationOptionDto;
        List<VariationOption> deletedVariationOption = new ArrayList<>();
        while (iterator.hasNext()) {
          variationOption = iterator.next();
          variationOptionDto = variationOptionWithId.get(variationOption.getId());
          if (variationOptionDto == null) {
            deletedVariationOption.add(variationOption);
            iterator.remove();
          } else {
            if (Strings.isNotBlank(variationOptionDto.getValue())) {
              variationOption.setValue(variationOptionDto.getValue());
            }
          }
        }
        int size = variationOptionWithOutId.size();
        for (int i = 0; i < size; i++) {
          currentVariation.getSetOfVariationOptions()
              .add(new VariationOption(null, variationOptionWithOutId.get(i).getValue(), currentVariation, null));
        }
        variationOptionRepository.deleteAll(deletedVariationOption);
      }

//      if (request.getSetOfVarirationOptionValues() != null) {
//        Set<String> tempsetOfVariationOptionValues = request.getSetOfVarirationOptionValues();
//        Set<VariationOption> setOfVariationOption = currentVariation.getSetOfVariationOptions();
//        Set<VariationOption> setOfDeletedVariationOption = new HashSet<>();
//        String value;
//        boolean isDeleted = true;
//        for (VariationOption variationOption : setOfVariationOption) {
//          Iterator<String> stringIterator = tempsetOfVariationOptionValues.iterator();
//          while (stringIterator.hasNext()) {
//            value = stringIterator.next();
//            if (variationOption.getValue().toLowerCase().equals(value.toLowerCase())) {
//              variationOption.setValue(value);
//              stringIterator.remove();
//              isDeleted = false;
//              break;
//            }
//          }
//          if (isDeleted) {
//            setOfDeletedVariationOption.add(variationOption);
//          }
//        }
//        for (String value1 : tempsetOfVariationOptionValues) {
//          currentVariation.getSetOfVariationOptions().add(new VariationOption(null, value1, currentVariation, null));
//        }
//        currentVariation.getSetOfVariationOptions().removeAll(setOfDeletedVariationOption);
//        variationOptionRepository.deleteAll(setOfDeletedVariationOption);
//      }
      currentVariation = variationRepository.saveAndFlush(currentVariation);
      return new DataResponse(ApplicationConstants.VARIATION_UPDATE_SUCCESSFULLY, currentVariation);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.VARIATION_NOT_FOUND,
        ApplicationConstants.VARIATION_NOT_FOUND_CODE);
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
