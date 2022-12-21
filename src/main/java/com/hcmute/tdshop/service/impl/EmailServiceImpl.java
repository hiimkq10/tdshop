package com.hcmute.tdshop.service.impl;

import com.hcmute.tdshop.dto.auth.ForgotPasswordRequest;
import com.hcmute.tdshop.entity.Token;
import com.hcmute.tdshop.entity.User;
import com.hcmute.tdshop.model.DataResponse;
import com.hcmute.tdshop.repository.TokenRepository;
import com.hcmute.tdshop.repository.UserRepository;
import com.hcmute.tdshop.service.EmailService;
import com.hcmute.tdshop.utils.Helper;
import com.hcmute.tdshop.utils.constants.ApplicationConstants;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  @Value("${spring.mail.username}")
  private String sender;

  @Autowired
  private Configuration configuration;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public DataResponse sendForgotPasswordEmail(ForgotPasswordRequest request) {
    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
    if (optionalUser.isPresent()) {
      String code = generateRandomNumberString();
      User user = optionalUser.get();
      createToken(user, code);
      return sendEmail(user, code, "reset_password_email_template.ftl");
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);
  }

  @Override
  public DataResponse sendActivateAccountEmail(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (!user.getIsVerified()) {
        String code = getRandomString();
        createToken(user, code);
        return sendEmail(user, String.format("https://tdshop.herokuapp.com/api/v1/auth/activate/%d?token=%s", id, code),
            "activate_account_email_template.ftl");
      }
      else {
        return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.ACCOUNT_ACTIVATED, ApplicationConstants.BAD_REQUEST_CODE);
      }
    }
    return new DataResponse(ApplicationConstants.BAD_REQUEST, ApplicationConstants.USER_NOT_FOUND,
        ApplicationConstants.BAD_REQUEST_CODE);

  }

  private DataResponse sendEmail(User user, String code, String template) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
      mimeMessageHelper.setFrom(sender);
      mimeMessageHelper.setTo(user.getEmail());
      mimeMessageHelper.setText(getEmailContent(user, code, template), true);
      mimeMessageHelper.setSubject(ApplicationConstants.VERIFICATION_EMAIL_SUBJECT);
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          javaMailSender.send(mimeMessage);
        }
      });
      thread.start();
      return DataResponse.SUCCESSFUL;
    } catch (MessagingException | IOException | TemplateException e) {
      e.printStackTrace();
    }
    return DataResponse.FAILED;
  }

  String getEmailContent(User user, String code, String template)
      throws IOException, TemplateException, IOException, TemplateException {
    StringWriter stringWriter = new StringWriter();
    Map<String, Object> model = new HashMap<>();
    model.put("name", user.getFirstName() + " " + user.getLastName());
    model.put("code", code);
    configuration.getTemplate(template).process(model, stringWriter);
    return stringWriter.getBuffer().toString();
  }

  private void createToken(User user, String code) {
    Token token = tokenRepository.findByUser_Id(user.getId()).orElse(new Token());
    LocalDateTime created_at = LocalDateTime.now();
    LocalDateTime expired_at = created_at.plusMinutes(Helper.CONFIRM_TOKEN_DURATION);
    token.setCode(code);
    token.setCreatedAt(created_at);
    token.setExpiredAt(expired_at);
    token.setConfirmedAt(null);
    if (token.getId() == null) {
      token.setUser(user);
      tokenRepository.save(token);
    } else {
      tokenRepository.saveAndFlush(token);
    }
  }

  private String generateRandomNumberString() {
    Random rnd = new Random();
    int number = rnd.nextInt(999999);
    return String.format("%06d", number);
  }

  private String getRandomString() {
    return UUID.randomUUID().toString();
  }
}
