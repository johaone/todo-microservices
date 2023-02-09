package ru.javabegin.springms.todo.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/*

общая статистика по задачам (незвисимо от категорий задач)

 */

@Entity
@Table(name = "statistic", schema = "affairs", catalog = "todo_affairs")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Statistic { // в этой таблице всего 1 запись, которая обновляется (но никогда не удаляется)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "completed_total", updatable = false)
    private Long completedTotal; // значение задается в триггере в БД

    @Column(name = "uncompleted_total", updatable = false)
    private Long uncompletedTotal; // значение задается в триггере в БД


    @Column(name="user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return id.equals(statistic.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
