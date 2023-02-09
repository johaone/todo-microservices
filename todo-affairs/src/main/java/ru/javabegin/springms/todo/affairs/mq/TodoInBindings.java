package ru.javabegin.springms.todo.affairs.mq;

// https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/dorabotka-koda-dlya-rabbit-mq/
//mq - massage queue (massage broker)
// Реализован интерфейс с нужными принимающим каналом (канал соединяет RabbitMQ с нашим модулем)

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface TodoInBindings {  //В Spring Cloud Stream есть встроенный interface Sink

    /*public interface Sink {
    String INPUT = "input";
    @Input(Sink.INPUT)
    SubscribableChannel input()*/

    String INPUT_CHANNEL = "todoInputChannel"; // по этому названию будут ссылаться на канал

    // создание самого исходящего канала - users по этому каналу будет оправлять сообщение в affairs, что новый пользователь добавлен
    @Input(INPUT_CHANNEL) // такой подход связки модуля с mq устарел
    MessageChannel todoInputChannel(); // через встроенный объект MessageChannel происходит приемка

}
