package ru.javabegin.springms.todo.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.springms.todo.entity.UserData;
import ru.javabegin.springms.todo.users.mq.functionalcode.MessageOutFunc;
//import ru.javabegin.springms.todo.users.mq.legacy.MessagePublisher;
import ru.javabegin.springms.todo.users.search.UserSearchValues;
import ru.javabegin.springms.todo.users.service.UserService;
import ru.javabegin.springms.todo.utils.webclient.UserWebClientBuilder;


import java.text.ParseException;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
//    private final UserWebClientBuilder userWebClientBuilder;

//    private MessagePublisher messagePublisher; // для применения legacy
    private final MessageOutFunc messagePublisher;

    private static final String ID_COLUMN = "id"; // имя столбца

    /**
     * Удаление user методом DELETE
     */
    // DELETE - идемпотентный метод. Удаление можно также производить через POST, причем id категории для удаления передается в body
    @DeleteMapping("/delete/{id}") // id категории, которую надо удалить, предается в адресной строке.
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // Применим исключение ошибки stacktrace. Через try-catch можно обработать исключение в статус
        try {
            userService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // возвращает статус сервера
    }

    /**
     * Добавление user методом POST
     */

    @PostMapping("/add")
    public ResponseEntity<UserData> add(@RequestBody UserData user) {

        // проверка на обязательные параметры
        if (user.getId() != null && user.getId() != 0) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно,
            // иначе может быть конфликт уникальности значения
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передали пустое значение
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        // добавляем пользователя
        user = userService.add(user);


        // создание тестовых данных для нового user через асинхронный вызов другого мс

        /* if (user != null) {
           // заполняем начальные данные пользователя (в параллельном потоке)
            userWebClientBuilder.initUserData(user.getId()).subscribe(result -> {
                        System.out.println("user populated: " + result);
                    }
            );*/

        // создание тестовых данных для нового user через отправку сообщения в message broker(mq) - это асинхронный вызов
        messagePublisher.sendMessage(user.getId()); // отправка сообщения по каналу отправки, что user создан

        return ResponseEntity.ok(user); // возвращаем созданный объект со сгенерированным id

    }


    /**
     * Обновление user методом PUT
     */
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody UserData user) {

        // проверка на обязательные параметры
        if (user.getId() == null && user.getId() == 0) {

            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение email, username, password
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: email MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }
        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: email MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        userService.update(user);
        return new ResponseEntity(HttpStatus.OK); // возвращает только статус 200 - ОК
    }

    /**
     * Поиск user по ID методом POST
     */
    @PostMapping("/findById")
    public ResponseEntity<UserData> findById(@RequestBody Long id) {
        Optional<UserData> user = userService.findById(id);
        try {
            if (user.isPresent()) { // если объект найден
                return ResponseEntity.ok(user.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Поиск user по email и username методом POST
     */
    @PostMapping("/searchByEmailOrUsername")
    public ResponseEntity<UserData> findByEmailOrUsername(@RequestBody UserSearchValues userSearchValues) {
        if ((userSearchValues.getEmail() == null || userSearchValues.getEmail().trim().length() == 0)
                && (userSearchValues.getUsername() == null || userSearchValues.getUsername().trim().length() == 0)) {
            return new ResponseEntity("missed param: email or username", HttpStatus.NOT_ACCEPTABLE);
        }

        UserData user;
        try {
            user = userService.findByEmailOrUsername(userSearchValues.getEmail(), userSearchValues.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("email = " + userSearchValues.getEmail() + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(user);
    }


    /**
     * Удаление user по email методом POST
     */
    @PostMapping("/deleteByEmail")
    public ResponseEntity<UserData> deleteByEmail(@RequestBody String email) {

        try {
            userService.deleteByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("email = " + email + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * Поиск категории методом POST указанным параметрам и userId пользователя
     */
    @PostMapping("/searchByParam")
    // НЕ List, а Page - для вывода коллекции постранично
    public ResponseEntity<Page<UserData>> search(@RequestBody UserSearchValues userSearchValues) { // ParseException? // Создали отдельный класс с параметрами title и email. См пакет search

        // исключить NullPointerException
        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;
        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNum() != null ? userSearchValues.getPageNum() : null;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

        Long id = userSearchValues.getId() != null ? userSearchValues.getId() : null;

        // Далее считываем направление сортировки

        // НАПРАВЛЕНИЕ
        //Direction - enum, содержащий два параметра - ASC и DESC - прямой и обратный порядок
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // СОРТИРОВКА, которая содержит столбец и направление
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);// сортируется направление сперва по полю столбца, если одинаковые столбцы, то сортировка будет по полю ID

        // Объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<UserData> result = userService.findByParam(username, email, id, pageRequest);
        return ResponseEntity.ok(result);
    }

}
