package com.epeins.factory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.epeins.factory.dao")
public class FactoryApplication{

	
	
	public static void main(String[] args) {
		SpringApplication.run(FactoryApplication.class, args);
	}
}
