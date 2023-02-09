package ru.javabegin.springms.todo.affairs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"ru.javabegin.springms.todo"})
@EnableJpaRepositories(basePackages = {"ru.javabegin.springms.todo.affairs"}) // для включения файла репозиторий, чтобы определялись бины entity
@EnableFeignClients //для вызова мс будет использоваться клиент feign(предпочтительна для синхронных вызовов)
@RefreshScope // Позволяет динамически изменять состав настроек бинов (к примеру изменили настройки логирования в properties, поменяли значение переменной и т.д.)
// Это позволяет избежать повторного перезапуска config, всех проектов, потом api-gateway
// Но если изменения связаны с БД, с RabbitMQ, то желательно перезапускать все заново, так как настройки с GitHub считываются при старте
// Для правильной работы RefreshScope должны быть добавлены настройки actuator(должен быть включен и включены все его endpoints)
public class PlannerTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerTodoApplication.class, args);
    }

}
