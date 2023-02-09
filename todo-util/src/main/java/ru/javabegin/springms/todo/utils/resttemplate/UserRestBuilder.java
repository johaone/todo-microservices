package ru.javabegin.springms.todo.utils.resttemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.javabegin.springms.todo.entity.UserData;


@Component
public class UserRestBuilder {

    private static final String baseUrl = "http://localhost:8778/todo-users/users"; // это url сервиса внутри модуля users, 8778 - порт api/gateway

    // метод для проверки наличия user
    public boolean userExist(Long userId) {

        //RestTemplate - через него мы делаем синхронный запрос из одного микросервиса,
        //в данном случае из модуля affairs обращаемся в модуль users для проверки наличия пользователя.

        // Пример применения RestTemplate - но он уже deprecated. Вместо него применяется WebClient
        RestTemplate restTemplate = new RestTemplate(); // RestTemplate – это HTTP клиент – т.е. библиотека, которая помогает вызывать сервисы,
        // получать ответы, обрабатывать ошибки и пр.

        // запрос с указанием userId
        HttpEntity<Long> request = new HttpEntity(userId);

        // ответ со статусом ответа
        ResponseEntity<UserData> response; // специальный контейнер, в котором указывается статус ответа, сам ответ (может быть это объект) и другая служебная информация
        // В данном методе нам нужен ответ в виде сообщения (есть или нет), а не объект.
        // Если нужен объект, то можно через response.getBody() получить объект.
        // Происходит автоматическая конвертация JSON в POJO

        try {
            // вызов сервиса
            response = restTemplate.exchange(baseUrl+"/findById", HttpMethod.POST, request, UserData.class);
            if (response.getStatusCode() == HttpStatus.OK) // должен быть статус 200
                return true; // то есть существует такой пользователь
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false; // если статус НЕ 200

    }
}
