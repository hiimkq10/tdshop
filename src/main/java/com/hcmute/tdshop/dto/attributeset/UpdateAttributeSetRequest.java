package com.hcmute.tdshop.dto.attributeset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.tdshop.dto.attribute.AttributeDto;
import com.hcmute.tdshop.dto.attribute.SimpleAttribute;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAttributeSetRequest {
  @JsonProperty("Name")
  @Size(max = 100, message = ApplicationConstants.ATTRIBUTE_SET_NAME_SIZE_INVALID)
  private String name;

  @JsonProperty("Attributes")
  private Set<@Valid SimpleAttribute> setOfAttributes;
}
