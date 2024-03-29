package com.vitaly.dlmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DlManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlManagerApplication.class, args);
    }
    //TODO сделать тесты интеграционный с реальными хождениями в бд через тест контейнеры
    //TODO упаковать в докер
    //TODO теперь надо добавить логигу в контролер в зависимости от роли делает чтото
}
