/*
package ru.javabegin.springms.todo.users.mq.legacy;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(TodoOutBinding.class) // связка текущего класса с интерфейсом(binding), писывающим отправляющий канал
public class MessagePublisher {

    // DI
    private TodoOutBinding todoOutBinding; // содержит все описанные каналы

    public MessagePublisher(TodoOutBinding todoOutBinding) {
        this.todoOutBinding = todoOutBinding;
    }

    // метод, отправляющий при создании нового пользователя
    public void newUserInit(Long userId) { // этот метод вызывается из контроллера user при добавлении нового user

        // Специальный интерфейс Message - в тело которого через payload можно передать json, string, объекты и тд. Также другие служебные информации headers - используются для маршрутизации, выборок и многих других вещей
        Message message = MessageBuilder.withPayload(userId).build(); // withPayload(userId) - создает сообщение с userId, build() - создает message

        todoOutBinding.todoOutputChannel().send(message); // выбираем канал и по нему отправляем сообщение, которое принимает уже Consumer(модуль affairs)
    }
}
*/
