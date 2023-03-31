package com.hcmute.tdshop.security.model;

import com.hcmute.tdshop.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails {

  private User user;
  private Map<String, Object> attributes;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  public static CustomUserDetails create(User user, Map<String, Object> attributes) {
    CustomUserDetails customUserDetails = new CustomUserDetails(user);
    customUserDetails.setAttributes(attributes);
    return customUserDetails;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return this.attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(user.getRole().getName()));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.getIsActive();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getIsVerified();
  }

  @Override
  public String getName() {
    return String.valueOf(user.getId());
  }
}
