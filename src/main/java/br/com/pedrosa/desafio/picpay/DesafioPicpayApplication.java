package br.com.pedrosa.desafio.picpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DesafioPicpayApplication {

    public static void main(String[] args) {
		SpringApplication.run(DesafioPicpayApplication.class, args);
	}

}
