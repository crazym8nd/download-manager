package com.vitaly.dlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DlManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlManagerApplication.class, args);
    }
    //TODO сделать доступ в зависимости от роли
    //TODO сделать тесты интеграционный с реальными хождениями в бд через тест контейнеры
    //TODO упаковать в докер
    //TODO логика по ролям в зависимости от роли на уровне сервисе функции
    //TODO webflux s3
}
