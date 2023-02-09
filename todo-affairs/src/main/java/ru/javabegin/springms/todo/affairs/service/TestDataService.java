package ru.javabegin.springms.todo.affairs.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.javabegin.springms.todo.entity.Category;
import ru.javabegin.springms.todo.entity.Priority;
import ru.javabegin.springms.todo.entity.Task;

import java.util.Calendar;
import java.util.Date;

@Service
public class TestDataService {

    private final CategoryService categoryService;
    private final PriorityService priorityService;
    private final TaskService taskService;

    public TestDataService(CategoryService categoryService, PriorityService priorityService, TaskService taskService) {
        this.categoryService = categoryService;
        this.priorityService = priorityService;
        this.taskService = taskService;
    }

    public ResponseEntity<Boolean> init(@RequestBody Long userId) {
        Priority priority = new Priority();
        priority.setColor("ccc");
        priority.setTitle("Средний");
        priority.setUserId(userId);

        Priority priority2 = new Priority();
        priority2.setColor("fff");
        priority2.setTitle("Низкий");
        priority2.setUserId(userId);

        priorityService.add(priority);
        priorityService.add(priority2);

        Category category = new Category();
        category.setTitle("Семья");
        category.setUserId(userId);

        Category category2 = new Category();
        category2.setTitle("Отдых");
        category2.setUserId(userId);

        categoryService.add(category);
        categoryService.add(category2);

        // завтра
        Date tomorrow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tomorrow);
        c.add(Calendar.DATE, 1);
        tomorrow = c.getTime();

        // неделя
        Date oneWeek = new Date();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(oneWeek);
        c2.add(Calendar.DATE, 7);
        oneWeek = c2.getTime();

        Task task = new Task();
        task.setTitle("Сходить в кино");
        task.setCategory(category);
        task.setPriority(priority);
        task.setCompleted(true);
        task.setTaskDate(tomorrow);
        task.setUserId(userId);

        Task task2 = new Task();
        task2.setTitle("Поспать");
        task2.setCategory(category2);
        task2.setCompleted(false);
        task2.setPriority(priority2);
        task2.setTaskDate(oneWeek);
        task2.setUserId(userId);


        taskService.add(task);
        taskService.add(task2);

        // если пользователя НЕ существует
        return ResponseEntity.ok(true);

    }

}
