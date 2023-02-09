package ru.javabegin.springms.todo.utils.webclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.javabegin.springms.todo.entity.UserData;

@Component
// специальный класс для вызова мс пользователей с помощью WebClient - замена RestTemplate
public class UserWebClientBuilder {


    // url мс, куда будет обращение
    private static final String baseUrlUser = "http://localhost:8778/todo-users/users/"; // это url сервиса внутри модуля users, 8778 - порт api/gateway

    private static final String baseUrlData = "http://localhost:8778/todo-affairs/testData/";

    // синхронный метод для проверки наличия user
    public boolean userExistSync(Long userId) {

        try {
            UserData user = WebClient.create(baseUrlUser)
                    .post()// тип метода findById
                    .uri("/findById")
                    .bodyValue(userId)
                    .retrieve()// вызов мс и проверка статуса 200
                    .bodyToMono(UserData.class) // Упаковка в специальный поток (выдает асинхронно 0 или 1 элементов). Оппонент Flux (для обработки коллекции)
                    .block(); // Блокируется поток, до получения результата первой записи. Этим мы сделали метод СИНХРОННЫМ запрос.
            // Если убрать, то метод станет асинхронным, но тип должен быть не user а Flux
            if (user != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // асинхронный метод для проверки наличия user

    public Flux<UserData> userExistAsync(Long userId) {

        Flux<UserData> userFlux = WebClient.create(baseUrlUser)
                .post()
                .uri("findById")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(UserData.class);
        return userFlux;
    }

    public Flux<Boolean> initUserData(Long userId) {

        Flux<Boolean> userInit = WebClient.create(baseUrlData)
                .post()
                .uri("init")
                .bodyValue(userId)
                .retrieve()
                .bodyToFlux(Boolean.class);

        return userInit;

    }
}
