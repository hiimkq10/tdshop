package com.hcmute.tdshop.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
  public static final String ACCOUNT_SID = "ACfc94bd3f7e9407c72417a5d0c23dc4b7";
  public static final String AUTH_TOKEN = "51b6321c747b104a3e174f935862fd26";

  public static void send() {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message
        .creator(new PhoneNumber("+840386721056"),
            new PhoneNumber("+447700165739"),
            "Ahoy world! I love writing code in Java. Quang Ke")
        .create();
    System.out.println(message.getSid());
  }
}
