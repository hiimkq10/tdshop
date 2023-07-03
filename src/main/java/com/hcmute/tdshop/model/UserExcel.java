package com.hcmute.tdshop.model;

import com.hcmute.tdshop.utils.annotations.ExcelColumnIndex;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExcel {
  @ExcelColumnIndex(index = 0)
  private String firstName;

  @ExcelColumnIndex(index = 1)
  private String lastName;

  @ExcelColumnIndex(index = 2)
  private String email;

  @ExcelColumnIndex(index = 3)
  private String phone;

  @ExcelColumnIndex(index = 4)
  private LocalDate birthdate;

  @ExcelColumnIndex(index = 5)
  private String username;

  @ExcelColumnIndex(index = 6)
  private String password;

  @ExcelColumnIndex(index = 7)
  private Boolean gender;
}
