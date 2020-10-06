package br.com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankApp {

	public static void main(String[] args) {
		SpringApplication.run(BankApp.class, args);
	}

}

