package ru.javabegin.springms.todo.users.mq.functionalcode;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;


@Configuration // Spring считывает класс, создает соответсвующий, bean, работающий с каналами сообщений
@Getter

// Класс для описания всех каналов, по которым будут отправляться сообщения. Аналог интерфейса TodoOutBinding
// каналы описываются с помощью функционального кода
public class MessageOutChannel {

    // messageBus специальная шина-слушатель, который принимает и отправляет уведомления всем Consumer в каналы SCS, что добавлен новый пользователь
    // Many - указывает, что слушателей может быть много
    // many().multicast() - метод, для уведомления всех слушателей
    // onBackpressureBuffer() - метод устанавливающий параметры "трубе" - сколько сообщений может влезть
    // autoCancel = false - если все слушатели отвяжутся от "трубы", messageSender автоматически не уничтожится
    private Sinks.Many<Message<Long>> messageBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    // Создается bean - поставщик, который при считывании шины получает сообщение о добавленном user и указывает создать тестовые данные для него.
    // Supplier - встроенный интерфейс-поставщик, описывающий канал отправки
    // В реактивном коде Flux выступает в качестве коллекции или "трубы",
    // куда будут помещаться сообщения типа Message, считанные с шины, у которого тип Long, так как передаем id нового user

    @Bean
    public Supplier<Flux<Message<Long>>> initNewUser() { // Supplier подписывается на нашу шину messageBus
        // Flux указывается для того, чтобы сообщение отправлялось по требованию(после добавления нового user), без него будет автоматическое постоянное отправление
        return () -> messageBus.asFlux(); // будет считывать данные из потока Flux (как только в эту шину попадают сообщения - как они туда попадают описано в другом классе)
        // asFlux() - метод, преобразующий поток данных в stream типа Flux(понятный тип для Java), содержащий все данные из исходного потока

    }


}
