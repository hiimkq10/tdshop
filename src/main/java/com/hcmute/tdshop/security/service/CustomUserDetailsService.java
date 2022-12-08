package com.hcmute.tdshop.security.service;

import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.security.model.CustomUserDetails;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      return new CustomUserDetails(user);
    }
    throw new UsernameNotFoundException(username + " not found");
  }
}
