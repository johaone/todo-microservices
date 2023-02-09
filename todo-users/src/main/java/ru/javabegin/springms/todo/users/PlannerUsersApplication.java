package ru.javabegin.springms.todo.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"ru.javabegin.springms.todo"})
@EnableJpaRepositories(basePackages = {"ru.javabegin.springms.todo.users"})
@RefreshScope // Позволяет динамически изменять состав настроек бинов (к примеру изменили настройки логирования в properties, поменяли значение переменной и т.д.)
// Это позволяет избежать повторного перезапуска config, всех проектов, потом api-gateway
// Но если изменения связаны с БД, с RabbitMQ, то желательно перезапускать все заново, так как настройки с GitHub считываются при старте
// Для правильной работы RefreshScope должны быть добавлены настройки actuator(должен быть включен и включены все его endpoints)
public class PlannerUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlannerUsersApplication.class, args);
	}

}
