package ru.javabegin.springms.todo.affairs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.springms.todo.affairs.feign.UserFeignClient;
import ru.javabegin.springms.todo.affairs.search.TaskSearchValues;
import ru.javabegin.springms.todo.affairs.service.TaskService;
import ru.javabegin.springms.todo.entity.Task;
import ru.javabegin.springms.todo.entity.UserData;
import ru.javabegin.springms.todo.utils.webclient.UserWebClientBuilder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private static final String ID_COLUMN = "id"; // имя столбца
    private final TaskService taskService;

    private final UserFeignClient userFeignClient;

    /**Метод POST*/
    @PostMapping("/all")
    public List<Task> findAll(@RequestBody Long userId) { //  в параметры email также передается в формате json, указывается аннотация для считывания этого файла
        return taskService.findAll(userId);
    }


    /**Добавление задачи методом POST*/

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        // проверка на обязательные параметры
        if(task.getId()!= null && task.getId() != 0 ) {

            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        ResponseEntity<UserData> result = userFeignClient.findUserById(task.getUserId());
        if (result == null) { // если мс недоступен, возвращается null
            return new ResponseEntity("система пользователей недоступна, попробуйте позднее!", HttpStatus.NOT_FOUND);
        }

        if (result.getBody() != null) {// если пользователь не пустой
            return ResponseEntity.ok(taskService.add(task));
        }

        // Если выполнить после асинхронного метода проверки на наличие пользователя, то независимо есть такой user или нет,
        // будет выходить "id не найден". Поэтому асинхронный метод не подходит
        // А синхронный дожидается ответа и далее добавляет, либо выводит, что не нашел пользователя
        return new ResponseEntity("user id = " + task.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }


    /**Обновление задачи методом PUT*/
    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Task task) { //
        // проверка на обязательные параметры
        if(task.getId() == null && task.getId() == 0 ) { //

            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        taskService.update(task);
        return new ResponseEntity(HttpStatus.OK); // возвращает только статус 200 - ОК
    }

    /**Удаление задачи методом DELETE*/
    @DeleteMapping("/delete/{id}") //
    public ResponseEntity delete(@PathVariable("id") Long id) {

        try {
            taskService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // возвращает статус сервера

    }

    /** Поиск задачи методом POST указанным параметрам и userId пользователя*/
    @PostMapping("/search")
    // НЕ List, а Page - для вывода коллекции постранично
    public ResponseEntity<Page<Task>> search (@RequestBody TaskSearchValues taskSearchValues) { // Создали отдельный класс с параметрами title и email. См пакет search

        // исключить NullPointerException
        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        // конвертируем Integer в Boolean
        Boolean completed = taskSearchValues.getCompleted() != null && taskSearchValues.getCompleted() == 0 ? false : true;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNum() != null ? taskSearchValues.getPageNum() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

        Long userId = taskSearchValues.getUserId() != null ? taskSearchValues.getUserId() : null; // для показа задач только этого пользователя


        // проверка на обязательные параметры
        if(userId == null || userId == 0 ){
            return new ResponseEntity("missed param: userId ", HttpStatus.NOT_ACCEPTABLE);
        }

        // Чтобы отобрать в выборке все задачи по датам, независимо от времени. Можно выставить время от 00:00 до 23:59

        Date dateFrom = null;
        Date dateTo = null;

        // выставить время с 00:00 для начальной даты (если она указана)

        if(taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance(); // Calendar - специальный объект для работы с датой и времен с учетом часового пояса автоматически
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 1);
            calendarFrom.set(Calendar.SECOND, 1);
            calendarFrom.set(Calendar.MILLISECOND, 1);

            dateFrom = calendarFrom.getTime();
        }

        // выставить время с 23:59 для начальной даты (если она указана)
        if(taskSearchValues.getDateTo() != null) {
            Calendar calendarTo = Calendar.getInstance(); // Calendar - специальный объект для работы с датой и времен с учетом часового пояса автоматически
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime();
        }

        // Далее считываем направление сортировки

        // НАПРАВЛЕНИЕ
        //Direction - enum, содержащий два параметра - ASC и DESC - прямой и обратный порядок
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        /*
        Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
        Например, если у двух задач одинаковое значение приоритета, тогда сортировка будет по полю.
        Порядок следования этих дух записей может каждый раз меняться после выполнения запроса, т.к. не указано второе поле сортировки.
        Поэтому и используем ID_COLUMN, чтобы если попадутся несколько одинаковых названий задач, или задач с одинаковыми приоритетами или категориями, сортировка была по id*/

        // СОРТИРОВКА, которая содержит столбец и направление
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);// сортируется направление сперва по полю столбца, если одинаковые столбцы, то сортировка будет по полю ID

        // Объект постраничности
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //pageRequest передаем в result в качестве параметра для метода findByParam, чтобы
        // springboot понимал какой запрос выполнить, что сортировать и т.д.

        // Результат запроса с постраничным выводом
        Page<Task> result = taskService.findByParam(title, completed, categoryId, priorityId, userId, dateFrom, dateTo, pageRequest);
        return ResponseEntity.ok(result);
    }

    /**Поиск задачи по ID методом POST*/
    @PostMapping("/searchById")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        Optional<Task> task = taskService.findById(id);
        try {
            if(task.isPresent()) { // если объект найден
                return ResponseEntity.ok(task.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);

    }

}
