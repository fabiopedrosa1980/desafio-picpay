package br.com.pedrosa.desafio.picpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@SpringBootApplication
public class DesafioPicpayApplication {

    public static void main(String[] args) {
		SpringApplication.run(DesafioPicpayApplication.class, args);
	}

}
