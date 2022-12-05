package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.AttributeRepository;
import com.hcmute.tdshop.service.AttributeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeServiceImpl implements AttributeService {
  @Autowired
  AttributeRepository attributeRepository;

  @Override
  public DataResponse getAttributesByAttributeSet(long id) {
    List<Attribute> listOfAttribute = attributeRepository.findByAttributeSet_Id(id);
    return new DataResponse(listOfAttribute);
  }
}
