package com.hcmute.tdshop.dto.statistic;

import lombok.Data;

@Data
public class AccountStatisticDto {
  private Long roleId;
  private String roleName;
  private Long total;

  public AccountStatisticDto(Long roleId, String roleName, Long total) {
    this.roleId = roleId;
    this.roleName = roleName;
    this.total = total;
  }
}
