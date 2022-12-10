package com.hcmute.tdshop.utils;

import com.hcmute.tdshop.dto.security.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationHelper {

  public static long getCurrentLoggedInUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return 0;
    }
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      return 0;
    }
    return ((UserInfo) principal).getId();
  }

  public static String getCurrentLoggedInUserRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      return null;
    }
    return ((UserInfo) principal).getRole();
  }
}
