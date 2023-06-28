package com.hcmute.tdshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto {
  boolean result;
  String message;
  int status;

  public static ResultDto SUCCESS = new ResultDto(true, "", 400);
}
