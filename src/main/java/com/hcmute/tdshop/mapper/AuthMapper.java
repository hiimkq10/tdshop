package com.hcmute.tdshop.mapper;

import com.hcmute.tdshop.dto.auth.RegisterRequest;
import com.hcmute.tdshop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthMapper {

  public User RegisterRequestToUser(RegisterRequest request) {
    if (request == null) {
      return null;
    }
    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPhone(request.getPhone());
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());

    return user;
  }
}
