package br.com.xpto.inboundai;

import io.swagger.v3.oas.models.annotations.OpenAPI30;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPI30
@EnableFeignClients
public class InboundAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InboundAiApplication.class, args);
	}

}
