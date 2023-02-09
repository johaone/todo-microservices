package ru.javabegin.springms.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;


/*

Вся активность пользователя (активация аккаунта, другие действия по необходимости)

*/


@Entity
@Table(name = "activity", schema = "users", catalog = "todo_users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Activity { // название таблицы будет браться автоматически по названию класса с маленькой буквы: activity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "org.hibernate.type.NumericBooleanType") // для автоматической конвертации числа в true/false
    private Boolean activated; // становится true только после подтверждения активации пользователем (обратно false уже стать не может по логике)

    @Column(updatable = false)
    private String uuid; // создается только один раз с помощью триггера в БД

    // Ссылки на таблицу пользователей уже не будет, так как БД была разделена

    @OneToOne// LAZY для связки OneToOne не работает. Все равно выведет данные об активности при вызове пользователя
    @MapsId
    // Аннотация, работает в связке optional = false для полей связанных по типу OneToOne - это делается для работы ленивой загрузки, так как у типов связи OneToOne она не работает
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserData userDataId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
