package com.hcmute.tdshop.model;

import com.hcmute.tdshop.utils.annotations.ExcelColumnIndex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeArea {
  @ExcelColumnIndex(index = 0)
  String provinceName;

  @ExcelColumnIndex(index = 1)
  Long provinceId;

  @ExcelColumnIndex(index = 2)
  String districtName;

  @ExcelColumnIndex(index = 3)
  Long districtId;

  @ExcelColumnIndex(index = 4)
  String wardName;

  @ExcelColumnIndex(index = 5)
  Long wardId;

  @ExcelColumnIndex(index = 6)
  String wardType;
}
