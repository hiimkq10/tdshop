package com.hcmute.tdshop.security;

import com.hcmute.tdshop.security.filter.CustomAuthorizationFilter;
import com.hcmute.tdshop.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.hcmute.tdshop.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  // url that ignored by spring security
  private final String[] publicUrlPatterns = {"/v3/api-docs/**", "/swagger-ui/**", "/auth/reset-password**",
      "/auth/reset-password-verification**", "/email/send-reset-password-verification**", "/auth/register**",
      "/email/send-forgot-password-email**", "/email/send-activate-account-email/**",
      "/auth/activate/**", "/product/**", "/province/**", "/district/**", "/wards/**,",
      "/master-category/get-all**", "/master-category/get/**",
      "/category/get**", "/variation/get**", "/ship/**", "/payment-method/**"};
  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean(); // Dùng để xác thực người dùng
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors();
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests().antMatchers("/login").permitAll();
    http.authorizeRequests().antMatchers("/token/refresh").permitAll();
    http.authorizeRequests().anyRequest().authenticated();
    http.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManagerBean()));
    http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(publicUrlPatterns);
  }
}
