package ru.javabegin.springms.todo.affairs.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import ru.javabegin.springms.todo.affairs.service.TestDataService;


@Component
@EnableBinding(TodoInBindings.class) // связка текущего класса с интерфейсом(binding), реализующим принимающий канал
public class MessageConsumer { // класс получающий сообщений через binding по input каналу

    // после получения сообщения о добавлении нового пользователя - нужно создать для него тестовые данные по его задачам, категориям и приоритетам
    private TestDataService testDataService;

    public MessageConsumer(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    // метод создающий тестовые данные вызывается автоматически, как только по указанному каналу поступает сообщение о новом добавленном user
    @StreamListener(target = TodoInBindings.INPUT_CHANNEL) // Какую очередь считывать - то что попадет в указанный канал. Этот канал получает сообщение с новым добавленным userId


    public void initTestData(Long userId) { // это userId считывается и метод initTestData создает для него новые данные через вызов testDataService, где описывается как все это создается

//
        // Генерируем исключение, связанный с работой очереди, куда попадают ошибочные сообщения отправленные с users,
        // но не обрабатываем, чтобы увидеть это сообщение в DLQ. Если обработать, то будет выполнен init(userId)
        //https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/ochered-dead-letter-queue/
        try {
            throw new DeadLetterQueueException("test dead letter queue");
        } catch (DeadLetterQueueException e) {
            System.out.println("e = " + e.getMessage());
        }
//
        testDataService.init(userId);
    }

}
