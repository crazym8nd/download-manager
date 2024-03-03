package com.vitaly.dlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DlManagerApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		SpringApplication.run(DlManagerApplication.class, args);
	}
	//TODO сделать доступ в зависимости от роли
	//TODO сделать тесты
	//TODO упаковать в докер
}
