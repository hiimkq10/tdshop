package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.entity.VariationOption;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.VariationOptionRepository;
import com.hcmute.tdshop.service.VariationOptionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariationOptionServiceImpl implements VariationOptionService {
  @Autowired
  VariationOptionRepository variationOptionRepository;

  @Override
  public DataResponse getVariationOptionByVariation(long id) {
    List<VariationOption> listOfVariationOptions = variationOptionRepository.findByVariation_Id(id);
    return new DataResponse(listOfVariationOptions);
  }
}
