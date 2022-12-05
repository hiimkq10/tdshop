package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.attribute.AttributeDto;
import com.hcmute.tdshop.dto.attributeset.AddAttributeSetRequest;
import com.hcmute.tdshop.dto.attributeset.UpdateAttributeSetRequest;
import com.hcmute.tdshop.entity.Attribute;
import com.hcmute.tdshop.entity.AttributeSet;
import java.util.HashSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class AttributeSetMapper {

  public AttributeSet AddAttributeSetRequestToAttributeSet(AddAttributeSetRequest request) {
    if (request == null) {
      return null;
    }
    AttributeSet attributeSet = new AttributeSet();
    attributeSet.setName(request.getName());
    attributeSet.setSetOfAttributes(new HashSet<>());
    for (AttributeDto attributeDto : request.getSetOfAttributes()) {
      attributeSet.getSetOfAttributes()
          .add(new Attribute(null, attributeDto.getName(), attributeDto.getPriority(), attributeSet, null));
    }

    return attributeSet;
  }

  public AttributeSet UpdateAttributeSetRequestToAttributeSet(UpdateAttributeSetRequest request) {
    if (request == null) {
      return null;
    }
    AttributeSet attributeSet = new AttributeSet();
    attributeSet.setName(request.getName());

    return attributeSet;
  }
}
