package com.hcmute.tdshop.controller;

import com.hcmute.tdshop.dto.attributeset.AddAttributeSetRequest;
import com.hcmute.tdshop.dto.attributeset.UpdateAttributeSetRequest;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.service.AttributeSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attribute-set")
public class AttributeSetController {
  @Autowired
  AttributeSetService attributeSetService;

  @GetMapping("/get-all")
  public DataResponse getAll(Pageable pageable) {
    return attributeSetService.getAll(pageable);
  }

  @PostMapping("/add")
  public DataResponse insertAttributeSet(AddAttributeSetRequest request) {
    return attributeSetService.insertAttributeSet(request);
  }

  @PutMapping("/update/{id}")
  public DataResponse updateAttributeSet(@PathVariable(name = "id") long id, UpdateAttributeSetRequest request) {
    return attributeSetService.updateAttributeSet(id, request);
  }

  @DeleteMapping("/delete/{id}")
  public DataResponse deleteAttributeSet(@PathVariable(name = "id") long id) {
    return attributeSetService.deleteAttributeSet(id);
  }
}
