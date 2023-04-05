package com.hcmute.tdshop.security;

import com.hcmute.tdshop.security.filter.CustomAuthorizationFilter;
import com.hcmute.tdshop.security.filter.CustomUsernamePasswordAuthenticationFilter;
import com.hcmute.tdshop.security.jwt.JwtTokenProvider;
import com.hcmute.tdshop.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.hcmute.tdshop.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.hcmute.tdshop.security.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.hcmute.tdshop.security.service.CustomOAuth2UserService;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  // url that ignored by spring security
  private final String[] publicUrlPatterns = {"/v3/api-docs/**", "/swagger-ui/**", "/auth/reset-password**",
      "/auth/reset-password-verification**", "/email/send-reset-password-verification**", "/auth/register**",
      "/email/send-forgot-password-email**", "/email/send-activate-account-email/**",
      "/auth/activate/**", "/product/get-all", "/product/search", "/product/get/**",
      "/province/**", "/district/**", "/wards/**",
      "/master-category/get-all**", "/master-category/get/**",
      "/category/get**", "/category/get**/**",
      "/variation/get**", "/ship/**",
      "/promotion/get**", "/promotion/get**/**", "/promotion/search",
      "/payment/success**", "/payment-method/**",
      "/review/get-all", "/review/search", "/review/product-avg",
      "/token/refresh", "/"};
  @Autowired
  CustomUserDetailsService customUserDetailsService;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

//  @Autowired
//  private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//
//  @Autowired
//  private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Autowired
  private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  public JwtTokenProvider tokenAuthenticationFilter() {
    return new JwtTokenProvider();
  }

  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

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
    http.authorizeRequests().antMatchers("/**").permitAll();
    http.authorizeRequests().anyRequest().authenticated();
    http.oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler((AuthenticationSuccessHandler) new OAuth2AuthenticationSuccessHandler());
//        .failureHandler(oAuth2AuthenticationFailureHandler);
    http.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManagerBean()));
    http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(publicUrlPatterns);
  }
}
