package com.vitaly.dlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DlManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlManagerApplication.class, args);
	}
	//TODO сделать доступ в зависимости от роли
	//TODO сделать тесты
}
