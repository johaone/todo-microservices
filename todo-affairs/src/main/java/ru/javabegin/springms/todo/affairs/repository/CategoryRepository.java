package ru.javabegin.springms.todo.affairs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.springms.todo.entity.Category;

import java.util.List;

/**РЕПОЗИТОРИИ НУЖНЫ ДЛЯ ТОГО, ЧТОБЫ SPRING СОЗДАЛ НУЖНЫЕ SQL ЗАПРОСЫ*/

@Repository // Аннотация, указывающая, что это репозиторий для обращения к данным по Category
// JpaRepository<T, ID> - интерфейс, который описывает все доступные методы/способы для обращения к данным. Наследуется от ряда других интерфейсов,
// с расширенными методами для работы с данными
public interface CategoryRepository extends JpaRepository<Category, Long > {

    // Здесь можно сразу реализовать все свои методы CRUD, помимо доступных, по отношению именно к таблице Category

    //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    // В данной документации указано, что правильно указав название метода и поля, параметры, можно правильно создать

    // поиск категории пользователя по id
    List<Category> findByUserId(Long userId);

    // Почему UserId - потому что в Category.java есть объект userId, через которого получаем поле email
    // ОЧЕНЬ ВАЖНО НЕ ДОПУСКАТЬ ОШИБКИ В НАЗВАНИЯХ ПОЛЕЙ

    // Можно писать свои запросы в БД через JPQL - специальный язык запросов, включенный в спецификацию JPA
    @Query("select c from Category c where " +
            "(:title is null or :title='' " + // если название будет пустым, то будет поиск всех категорий для данного пользователя
            "or lower(c.title) like lower(concat('%', :title, '%')))" + // или же приведя название в нижний регистр будет сравнение любого введенного части слова, даже буквы для поиска схожести
            "and c.userId = :id " + // и также идет в сравнение email пользователя
            "order by c.title asc ") // и сортировка по алфавиту

    List<Category> findByTitleOrUserId(@Param("title") String title, @Param("id") Long id);

    // Можно написать любой запрос через Query, затем в методе в аргументы через @Param можно указать нужные параметры





}

