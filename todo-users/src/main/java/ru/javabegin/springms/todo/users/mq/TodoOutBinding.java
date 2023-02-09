package ru.javabegin.springms.todo.users.mq;

//https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/dorabotka-koda-dlya-rabbit-mq/
//mq - massage queue (massage broker)
// Реализован интерфейс с нужными отправляющим каналом (канал соединяет RabbitMQ с нашим модулем)

// СВЯЗЫВАНИЕ ОТПРАВЛЯЮЩЕГО КАНАЛА И ПРИНИМАЮЩЕГО КАНАЛА ПРОИСХОДИТ В ОДНОМ EXCHANGE КАНАЛЕ,
// настройки для связки каналов описываются в properties(github) отправляющего модуля(users) и принимающего(affairs)
// через spring.cloud.stream.bindings.(todoInputChannel)todoOutputChannel.destination=exchange-channel

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;

public interface TodoOutBinding { // есть встроенный interface Source

   /* public interface Source {

        String OUTPUT = "output";

        @Output(org.springframework.cloud.stream.messaging.Source.OUTPUT)
        MessageChannel output();

    }*/

    // интерфейс не может содержать переменные, только константы
    String OUTPUT_CHANNEL = "todoOutputChannel"; // по этому названию будут ссылаться на канал

    // создание самого исходящего канала - users по этому каналу будет оправлять сообщение в affairs, что новый пользователь добавлен
    @Output(OUTPUT_CHANNEL) // такой подход связки модуля с mq устарел
    MessageChannel todoOutputChannel(); // через встроенный объект MessageChannel происходит отправка
}


