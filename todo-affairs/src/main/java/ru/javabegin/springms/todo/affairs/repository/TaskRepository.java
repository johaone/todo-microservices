package ru.javabegin.springms.todo.affairs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.springms.todo.entity.Task;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    // поиск всех задач конкретного пользователя
    List<Task> findByUserIdOrderByTitle(Long userId);

    // У одного пользователя может быть множество десятков задач
    // Нужно реализовать постраничный вывод, чтобы не выдать весь список задач одним разом

    // Используем JPQL - встроенный язык в Spring для написания ручного запроса

    @Query("SELECT t FROM Task t where " +
            "(:title is null or :title='' or lower(t.title) like lower(concat('%', :title,'%'))) and" +
            "(:completed is null or t.completed=:completed) and " +  // учитываем, что параметр может быть null или пустым
            "(:priorityId is null or t.priority.id=:priorityId) and " +
            "(:categoryId is null or t.category.id=:categoryId) and " +
            "(" +
            "(cast(:dateFrom as timestamp) is null or t.taskDate>=:dateFrom) and " +
            "(cast(:dateTo as timestamp) is null or t.taskDate<=:dateTo)" +
            ") and " +
            "(t.userId=:userId)" // показывать задачи только определенного пользователя, а не все
    )

    // Объект для постраничного вывода данных. Искать по всем переданным параметрам(Пустые параметры учитываться не будут)
    Page<Task> findByParam(@Param("title") String title,
                           @Param("completed") Boolean  completed,
                           @Param("priorityId") Long priorityId,
                           @Param("categoryId") Long categoryId,
                           @Param("userId") Long  userId,
                           @Param("dateFrom") Date dateFrom,
                           @Param("dateTo") Date  dateTo,
                           Pageable pageable // НАСТРОЙКА ПОСТРАНИЧНОСТИ - для того, сколько отобразить данные, начиная с какой страницы
                           );

    // Также в Page встроено, чтобы посмотреть информацию, о том сколько страниц вышла информация,
    // на какой странице мы сейчас находимся, сколько страниц нужно отобразить и
    // сколько элементов всего найдено независимо от количества страниц


}
