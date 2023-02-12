package ru.javabegin.springms.todo.affairs.mq.functioncode;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import ru.javabegin.springms.todo.affairs.service.TestDataService;

import java.util.function.Consumer;

// Класс реализующий работу с каналами SCS для принятия сообщений от отправителя
@RequiredArgsConstructor
public class MessageInputFunc {
    private TestDataService testDataService;

    @Bean // Создается новый бин-сообщение. Название должно совпадать с названиями definition и bindings в properties
    public Consumer<Message<Long>> newUserConsume() {
        // Возвращаем message, полученный из Message<Long> и передаем его в сервис для создания тестовых данных
        return message -> testDataService.init(message.getPayload());
    }
}
