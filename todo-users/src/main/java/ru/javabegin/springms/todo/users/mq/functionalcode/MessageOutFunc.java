package ru.javabegin.springms.todo.users.mq.functionalcode;

import lombok.Getter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service // Это сервис, для вызова его из UserController
@Getter
// Класс работающий с каналами. Реализует отправку сообщений в шину (messageBus) по требованию
public class MessageOutFunc {

    private MessageOutChannel outChannel; // канал для отправки message

    public MessageOutFunc(MessageOutChannel outChannel) {
        this.outChannel = outChannel;
    }

    // метод реализующий отправку сообщения о добавлении нового user
    public void sendMessage(Long userId) {
        // добавляем в канал новое сообщение
        outChannel.getMessageBus().emitNext(MessageBuilder.withPayload(userId).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        // FAIL_FAST - при удалении сообщений с коллекции(очереди) выбросится исключение, так как там установлен счетчик.
        // Чтобы избежать FailFast, применяют FailSave

        System.out.println(" Message sent = " + userId);
    }
}
