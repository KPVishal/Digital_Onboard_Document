package com.bijlipay;

import com.bijlipay.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
@ComponentScan(basePackages = "com.bijlipay.*")
public class DigitalOnboardDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalOnboardDocumentApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
