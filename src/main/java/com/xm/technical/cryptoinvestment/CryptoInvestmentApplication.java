package com.xm.technical.cryptoinvestment;

import com.xm.technical.cryptoinvestment.filter.RateLimitingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class CryptoInvestmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoInvestmentApplication.class, args);
	}
}
