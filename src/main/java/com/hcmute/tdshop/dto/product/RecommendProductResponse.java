package com.hcmute.tdshop.dto.product;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendProductResponse {
  int status;
  List<String> ids;
  String message;
}
