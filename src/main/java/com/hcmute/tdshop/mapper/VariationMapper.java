package com.hcmute.tdshop.mapper;
import com.hcmute.tdshop.dto.variation.AddVariationRequest;
import com.hcmute.tdshop.dto.variation.UpdateVariationRequest;
import com.hcmute.tdshop.entity.Variation;
import com.hcmute.tdshop.entity.VariationOption;
import java.util.HashSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VariationMapper {
  public Variation AddVariationRequestToVariation(AddVariationRequest request) {
    if (request == null) {
      return null;
    }
    Variation variation = new Variation();
    variation.setName(request.getName());
    variation.setSetOfVariationOptions(new HashSet<>());
    for (String value : request.getSetOfVarirationOptionValues()) {
      variation.getSetOfVariationOptions().add(new VariationOption(null, value, variation));
    }

    return variation;
  }

  public Variation UpdateVariationRequestToVariation(UpdateVariationRequest request) {
    if (request == null) {
      return null;
    }
    Variation variation = new Variation();
    variation.setName(request.getName());
//    variation.setSetOfVariationOptions(new HashSet<>());
//    for (String value : request.getSetOfVarirationOptionValues()) {
//      variation.getSetOfVariationOptions().add(new VariationOption(null, value, variation));
//    }

    return variation;
  }
}
