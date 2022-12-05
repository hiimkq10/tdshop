package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.attribute.AttributeDto;
import com.hcmute.tdshop.dto.attributeset.AddAttributeSetRequest;
import com.hcmute.tdshop.dto.attributeset.UpdateAttributeSetRequest;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.AttributeSet;
import com.hcmute.tdshop.mapper.AttributeSetMapper;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AttributeSetRepository;
import com.hcmute.tdshop.service.AttributeSetService;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

  @Override
  public DataResponse getAll(Pageable pageable) {
    Page<AttributeSet> pageOfAttributeSets = attributeSetRepository.findAll(pageable);
    return new DataResponse(pageOfAttributeSets);
  }

  @Override
  public DataResponse insertAttributeSet(AddAttributeSetRequest request) {
    AttributeSet attributeSet = attributeSetMapper.AddAttributeSetRequestToAttributeSet(request);
    if (checkIfNameExisted(attributeSet.getName())) {
      attributeSetRepository.save(attributeSet);
      return DataResponse.SUCCESSFUL;
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ATTRIBUTE_NAME_EXISTED,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse updateAttributeSet(long id, UpdateAttributeSetRequest request) {
    AttributeSet attributeSetToUpdate = attributeSetMapper.UpdateAttributeSetRequestToAttributeSet(request);
    Optional<AttributeSet> optionalAttributeSet = attributeSetRepository.findById(id);
    if (optionalAttributeSet.isPresent()) {
      AttributeSet currentAttributeSet = optionalAttributeSet.get();
      if (attributeSetToUpdate.getName() != null) {
        if (!checkIfNameExisted(attributeSetToUpdate.getName())) {
          currentAttributeSet.setName(attributeSetToUpdate.getName());
        }
      }

      // handle attribute set
      Set<AttributeDto> setOfAttributes = request.getSetOfAttributes();
      Set<String> setOfNames = setOfAttributes.stream().map(AttributeDto::getName).collect(Collectors.toSet());
      HashMap<String, Integer> priorities = new HashMap<>();
      for (AttributeDto attributeDto : request.getSetOfAttributes()) {
        priorities.put(attributeDto.getName().toLowerCase(), attributeDto.getPriority());
      }
      Iterator<Attribute> attributeIterator = currentAttributeSet.getSetOfAttributes().iterator();
      Attribute attribute;
      Iterator<String> stringIterator;
      boolean temp = false;
      while (attributeIterator.hasNext()) {
        attribute = attributeIterator.next();
        temp = false;
        stringIterator = setOfNames.iterator();
        while (stringIterator.hasNext()) {
          if (stringIterator.next().toLowerCase().equals(attribute.getName())) {
            stringIterator.remove();
            temp = true;
            break;
          }
        }
        if (!temp) {
          attributeIterator.remove();
        }
      }
      for (String value : setOfNames) {
        currentAttributeSet.getSetOfAttributes()
            .add(new Attribute(null, value, priorities.get(value.toLowerCase()), currentAttributeSet, null));
      }

      attributeSetRepository.saveAndFlush(currentAttributeSet);
      return DataResponse.SUCCESSFUL;
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
