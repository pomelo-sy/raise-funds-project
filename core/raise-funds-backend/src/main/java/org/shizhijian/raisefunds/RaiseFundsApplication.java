package org.shizhijian.raisefunds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.shizhijian.raisefunds.dao")
@EnableScheduling
public class RaiseFundsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RaiseFundsApplication.class, args);
	}
}
