package com.sh_38.coinhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com/sh_38/coinhub/feign"})
//@ImportAutoConfiguration

public class CoinhubApplication {

	public static void main(String[] args) {

		SpringApplication.run(CoinhubApplication.class, args);
	}

}
