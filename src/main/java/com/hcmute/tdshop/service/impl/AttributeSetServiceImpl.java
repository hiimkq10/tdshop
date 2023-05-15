package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.attribute.AttributeDto;
import com.hcmute.tdshop.dto.attribute.SimpleAttribute;
import com.hcmute.tdshop.dto.attributeset.AddAttributeSetRequest;
import com.hcmute.tdshop.dto.attributeset.UpdateAttributeSetRequest;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.AttributeSet;
import com.hcmute.tdshop.mapper.AttributeSetMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.repository.AttributeSetRepository;
import com.hcmute.tdshop.service.AttributeSetService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AttributeSetServiceImpl implements AttributeSetService {

  @Autowired
  private AttributeSetRepository attributeSetRepository;

  @Autowired
  private AttributeSetMapper attributeSetMapper;

  @Autowired
  private AttributeRepository attributeRepository;

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<AttributeSet> pageOfAttributeSets = attributeSetRepository.findAll(pageable);
    return new DataResponse(pageOfAttributeSets);
  }

  @Override
  public DataResponse insertAttributeSet(AddAttributeSetRequest request) {
    AttributeSet attributeSet = attributeSetMapper.AddAttributeSetRequestToAttributeSet(request);
    if (!checkIfNameExisted(attributeSet.getName())) {
      Set<String> names = attributeSet.getSetOfAttributes().stream().map(attribute -> attribute.getName().toLowerCase().trim()).collect(
          Collectors.toSet());
      if (names.size() < attributeSet.getSetOfAttributes().size()) {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_NAME_EXISTED, ApplicationConstants.ATTRIBUTE_NAME_EXISTED_CODE);
      }
      attributeSet = attributeSetRepository.save(attributeSet);
      return new DataResponse(ApplicationConstants.ATTRIBUTE_SET_ADD_SUCCESSFULLY, attributeSet);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_SET_NAME_EXISTED,
        ApplicationConstants.ATTRIBUTE_SET_NAME_EXISTED_CODE);
  }

  @Override
  public DataResponse updateAttributeSet(long id, UpdateAttributeSetRequest request) {
    AttributeSet attributeSetToUpdate = attributeSetMapper.UpdateAttributeSetRequestToAttributeSet(request);
    Optional<AttributeSet> optionalAttributeSet = attributeSetRepository.findById(id);
    if (optionalAttributeSet.isPresent()) {
      AttributeSet currentAttributeSet = optionalAttributeSet.get();
      if (attributeSetToUpdate.getName() != null && (!attributeSetToUpdate.getName().equals(currentAttributeSet.getName()))) {
        if (checkIfNameExisted(attributeSetToUpdate.getName())) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_SET_NAME_EXISTED,
              ApplicationConstants.ATTRIBUTE_SET_NAME_EXISTED_CODE);
        }
        currentAttributeSet.setName(attributeSetToUpdate.getName());
      }

      // handle attribute set
      if (request.getSetOfAttributes() != null) {
        Set<SimpleAttribute> attributeSet = request.getSetOfAttributes();
        Set<String> names = attributeSet.stream().map(attribute -> attribute.getName().toLowerCase().trim()).collect(
            Collectors.toSet());
        if (names.size() < attributeSet.size()) {
          return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_NAME_EXISTED, ApplicationConstants.ATTRIBUTE_NAME_EXISTED_CODE);
        }
        Map<Long, SimpleAttribute> attributesWithId = new HashMap<>();
        List<SimpleAttribute> attributesWithOutId = new ArrayList<>();
        attributeSet.forEach(attribute -> {
          if (attribute.getId() == null || attribute.getId() == 0) {
//            attributesWithOutId.add(new Attribute(null, attribute.getName(), attribute.getPriority(), currentAttributeSet, null));
            attributesWithOutId.add(attribute);
          }
          else {
            attributesWithId.put(attribute.getId(), attribute);
          }
        });
        Iterator<Attribute> iterator = currentAttributeSet.getSetOfAttributes().iterator();
        Attribute attribute;
        SimpleAttribute simpleAttribute;
        List<Attribute> deletedAttributes = new ArrayList<>();
        while (iterator.hasNext()) {
          attribute = iterator.next();
          simpleAttribute = attributesWithId.get(attribute.getId());
          if (simpleAttribute == null) {
            deletedAttributes.add(attribute);
            iterator.remove();
          }
          else {
            if (Strings.isNotBlank(simpleAttribute.getName())) {
              attribute.setName(simpleAttribute.getName());
            }
            attribute.setPriority(simpleAttribute.getPriority());
          }
        }
        int size = attributesWithOutId.size();
        for (int i = 0; i < size; i++) {
          currentAttributeSet.getSetOfAttributes().add(new Attribute(null, attributesWithOutId.get(i).getName(), attributesWithOutId.get(i).getPriority(), currentAttributeSet, null));
        }
        attributeRepository.deleteAll(deletedAttributes);
      }
//      if (request.getSetOfAttributes() != null) {
//        Set<AttributeDto> setOfAttributes = request.getSetOfAttributes();
//        Set<String> setOfNames = setOfAttributes.stream().map(AttributeDto::getName).collect(Collectors.toSet());
//        HashMap<String, Integer> priorities = new HashMap<>();
//        for (AttributeDto attributeDto : request.getSetOfAttributes()) {
//          priorities.put(attributeDto.getName().toLowerCase(), attributeDto.getPriority());
//        }
//        Set<Attribute> deletedAttributes = new HashSet<>();
//        Iterator<Attribute> attributeIterator = currentAttributeSet.getSetOfAttributes().iterator();
//        Attribute attribute;
//        Iterator<String> stringIterator;
//        boolean temp = false;
//        String value;
//        while (attributeIterator.hasNext()) {
//          attribute = attributeIterator.next();
//          temp = false;
//          stringIterator = setOfNames.iterator();
//          while (stringIterator.hasNext()) {
//            value = stringIterator.next();
//            if (value.equalsIgnoreCase(attribute.getName())) {
//              attribute.setName(value);
//              attribute.setPriority(priorities.get(attribute.getName().toLowerCase()));
//              stringIterator.remove();
//              temp = true;
//              break;
//            }
//          }
//          if (!temp) {
//            deletedAttributes.add(attribute);
//            attributeIterator.remove();
//          }
//        }
//        attributeRepository.deleteAll(deletedAttributes);
//        for (String value1 : setOfNames) {
//          currentAttributeSet.getSetOfAttributes()
//              .add(new Attribute(null, value1, priorities.get(value1.toLowerCase()), currentAttributeSet, null));
//        }
//      }

      currentAttributeSet = attributeSetRepository.saveAndFlush(currentAttributeSet);
      return new DataResponse(ApplicationConstants.ATTRIBUTE_SET_UPDATE_SUCCESSFULLY, currentAttributeSet);
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_SET_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse deleteAttributeSet(long id) {
    attributeSetRepository.deleteById(id);
    return DataResponse.SUCCESSFUL;
  }

  private boolean checkIfNameExisted(String name) {
    return attributeSetRepository.existsByNameIgnoreCase(name);
  }

//  private boolean checkIfAttributeNameExisted(String name) {
//
//  }
}
