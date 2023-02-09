package ru.javabegin.springms.todo.affairs.controller;

// класс, предназначенный для заполнения тестовыми данными таблиц

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.springms.todo.affairs.service.CategoryService;
import ru.javabegin.springms.todo.affairs.service.PriorityService;
import ru.javabegin.springms.todo.affairs.service.TaskService;
import ru.javabegin.springms.todo.affairs.service.TestDataService;
import ru.javabegin.springms.todo.entity.Category;
import ru.javabegin.springms.todo.entity.Priority;
import ru.javabegin.springms.todo.entity.Task;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/testData")
public class TestDataController {

    private final TestDataService testDataService;

    public TestDataController(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @PostMapping("/initTestData")
    public ResponseEntity<Boolean> init(@RequestBody Long userId) {

        testDataService.init(userId);

        return ResponseEntity.ok(true);

    }
}
