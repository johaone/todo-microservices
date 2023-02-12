package ru.javabegin.springms.todo.affairs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.springms.todo.affairs.feign.UserFeignClient;
import ru.javabegin.springms.todo.affairs.search.PrioritySearchValues;
import ru.javabegin.springms.todo.affairs.service.PriorityService;
import ru.javabegin.springms.todo.entity.Priority;
import ru.javabegin.springms.todo.entity.UserData;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;
    private final UserFeignClient userFeignClient;


    /**Метод POST*/
    @PostMapping("/all") // POST - НЕиденпотентный, то есть повторный запрос меняет состояние сервера. (Повторный тот же запрос в банк спишет повторно деньги)
    public List<Priority> findAll(@RequestBody Long userId) { //  в параметры email также передается в формате json, указывается аннотация для считывания этого файла
        return priorityService.findAll(userId);
    }

    /**Добавление приоритета методом POST*/
    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {

        // проверка на обязательные параметры
        if(priority.getId()!= null && priority.getId() != 0 ) {

            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }


        ResponseEntity<UserData> result = userFeignClient.findUserById(priority.getUserId());
        if (result == null) { // если мс недоступен, возвращается null
            return new ResponseEntity("система пользователей недоступна, попробуйте позднее!", HttpStatus.NOT_FOUND);
        }

        if (result.getBody() != null) {// если пользователь не пустой
            return ResponseEntity.ok(priorityService.add(priority));
        }

        return new ResponseEntity("user id = " + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }


    /**Обновление приоритета методом PUT*/
    @PutMapping("/update") // Метод идемпотентный - повторная отправка запроса не влияет на сервер
    public ResponseEntity update(@RequestBody Priority priority) { //Будет возвращать только статус, а не объект entity, как в POST

        // проверка на обязательные параметры
        if(priority.getId() == null && priority.getId() == 0 ) { // Значит такой категории нет в БД для его обновления

            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.update(priority);
        return new ResponseEntity(HttpStatus.OK); // возвращает только статус 200 - ОК
    }

    /**Удаление приоритета методом DELETE*/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            priorityService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // возвращает статус сервера

    }

    /** Поиск приоритета методом POST по названию и email пользователя*/
    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search (@RequestBody PrioritySearchValues prioritySearchValues) {

        if((prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId() == 0 )){
            return new ResponseEntity("missed param: userId ", HttpStatus.NOT_ACCEPTABLE);
        }

        // если title будет пустым, то будет поиск всех категорий без фильтрации названия

        List<Priority> list = priorityService.findByTitleOrUserId(prioritySearchValues.getTitle(), prioritySearchValues.getUserId());

        return  ResponseEntity.ok(list);
    }


    /**Поиск приоритета по ID методом POST*/
    @PostMapping("/searchById") // ID передаем в тело метода - безопасно
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        Optional<Priority> priority = priorityService.findById(id);
        try {
            if(priority.isPresent()) { // если объект найден
                return ResponseEntity.ok(priority.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);

    }

    /**Поиск приоритета по ID методом GET*/
    @GetMapping("/find/{id}") // id передаем в поисковую строку - это небезопасно
    public ResponseEntity<Priority> find (@PathVariable("id") Long id) {
        Optional<Priority> priority = priorityService.findById(id);
        try {
            if(priority.isPresent()) { // если объект найден
                return ResponseEntity.ok(priority.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);

    }

}
