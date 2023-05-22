package com.hcmute.tdshop.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
  Long userId = 0L;
  Long productId = 0L;
}
