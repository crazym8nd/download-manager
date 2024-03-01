package com.vitaly.dlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DlManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DlManagerApplication.class, args);
	}
	//TODO псделать слой репозитория реактивным
	//TODO понять какие функции в каких интерфейсах нужны
	//TODO сделать слой сервисов с необходимыми функциями, отдельный сервис для работы с S3
	//TODO контроллеры необходимые для работы
	//TODO сделать доступ в зависимости от роли
	//TODO сделать тесты
}
