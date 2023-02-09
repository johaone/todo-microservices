package ru.javabegin.springms.todo.affairs.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrioritySearchValues {
    private String title;
    private Long userId;
}
