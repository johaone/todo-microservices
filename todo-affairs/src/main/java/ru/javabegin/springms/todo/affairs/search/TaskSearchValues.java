package ru.javabegin.springms.todo.affairs.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchValues {

    // Поля по которым будет поиск задач. Все типы - объектные, чтобы можно было передать null

    private String title;
    private Integer completed;
    private Long priorityId;
    private Long categoryId;
    private Long userId;
    private Date dateFrom; // Для периода задачи. С какого по какую дату будет поиск задачи
    private Date dateTo;

    // поля для настройки постраничности
    private Integer pageNum;
    private Integer pageSize;

    // сортировка
    private String sortColumn; // столбца
    private String sortDirection; // направления

    // Такие же названия должны быть на фронтенде



}
