package com.hcmute.tdshop;

import com.hcmute.tdshop.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties(AppProperties.class)
public class TdshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TdshopApplication.class, args);
	}

}
