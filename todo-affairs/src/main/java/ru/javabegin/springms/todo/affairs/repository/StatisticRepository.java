package ru.javabegin.springms.todo.affairs.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javabegin.springms.todo.entity.Statistic;

@Repository
public interface StatisticRepository extends CrudRepository<Statistic, Long> { // Наследуемся не от JpaRepository, а от CRUDRepository - есть все нужные методы для OneToOne связи
    Statistic findByUserId(Long userId); // Получаем только один объект - связь один к одному
}
