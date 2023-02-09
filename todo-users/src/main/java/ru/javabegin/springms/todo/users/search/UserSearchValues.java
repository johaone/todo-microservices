package ru.javabegin.springms.todo.users.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchValues {

    private Long id;
    private String email;
    private String username;

    // поля для настройки постраничности
    private Integer pageNum;
    private Integer pageSize;

    // сортировка
    private String sortColumn; // столбца
    private String sortDirection; // направления
}
