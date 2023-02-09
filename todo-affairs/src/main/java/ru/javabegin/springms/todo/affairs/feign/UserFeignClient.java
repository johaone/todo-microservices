package ru.javabegin.springms.todo.affairs.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.javabegin.springms.todo.entity.UserData;

// Указывается название модуля, который будет вызываться
@FeignClient(name = "todo-users", fallback = UserFeignClientFallback.class) // если не будет доступен указанный мс, fallback вызовет соответствующий метод из указанного класса (это доработка для работы Circuit Breaker)
public interface UserFeignClient { // Feign работает с интерфейсами, а не с сервисами напрямую. Все сервисы "оборачиваются" в интерфейсы.

    // Перечисляются все сервисы, которые нужно вызывать через Feign.
    // В нашем случае нужно вызвать только сервис, извлекающий user по указанному его id
    @PostMapping("/users/findById") // uri до нужного метода
    ResponseEntity<UserData> findUserById(@RequestBody Long id); // возвращаемый тип и параметры должны быть такими же, как и в контроллере

}

@Component
class UserFeignClientFallback implements UserFeignClient {

    @Override
    // этот метод будет вызывать fallback, если метод в интерфейсе не отработает
    public ResponseEntity<UserData> findUserById(Long id) {
        return null; // Можно реализовать любой другой метод, вывод сообщения и т.д. Зависит от ТЗ
    }
}
