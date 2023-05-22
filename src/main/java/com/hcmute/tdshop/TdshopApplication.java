package com.hcmute.tdshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableConfigurationProperties(AppProperties.class)
@EnableScheduling
public class TdshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TdshopApplication.class, args);
	}

}
