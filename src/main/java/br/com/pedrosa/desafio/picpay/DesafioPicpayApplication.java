package br.com.pedrosa.desafio.picpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
@EnableRetry
public class DesafioPicpayApplication {

    public static void main(String[] args) {
		SpringApplication.run(DesafioPicpayApplication.class, args);
	}

}
