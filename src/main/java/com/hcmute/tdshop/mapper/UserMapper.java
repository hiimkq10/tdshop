package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.user.UpdateUserInfoRequest;
import com.hcmute.tdshop.dto.user.UserResponse;
import com.hcmute.tdshop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
  public abstract UserResponse UserToUserResponse(User user);
  public abstract User UpdateUserInfoRequestToUser(UpdateUserInfoRequest request);
}
