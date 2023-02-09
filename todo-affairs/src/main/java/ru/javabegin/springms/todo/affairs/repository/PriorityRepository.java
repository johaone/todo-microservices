package ru.javabegin.springms.todo.affairs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.springms.todo.entity.Priority;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    List<Priority> findByUserIdOrderByTitle(Long userId);

    // Можно писать свои запросы в БД через JPQL - специальный язык запросов, включенный в спецификацию JPA
    @Query("select p from Priority p where " +
            "(:title is null or :title='' " + // если название будет пустым, то будет поиск всех категорий для данного пользователя
            "or lower(p.title) like lower(concat('%', :title, '%')))" + // или же приведя название в нижний регистр будет сравнение любого введенного части слова, даже буквы для поиска схожести
            "and p.userId = :id " + // и также идет в сравнение email пользователя
            "order by p.title asc ") // и сортировка по алфавиту

    List<Priority> findByTitleOrUserId(@Param("title") String title, @Param("id") Long userId);

}
