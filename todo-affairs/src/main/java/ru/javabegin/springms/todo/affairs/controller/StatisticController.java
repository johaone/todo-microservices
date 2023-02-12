package ru.javabegin.springms.todo.affairs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.springms.todo.affairs.service.StatisticService;
import ru.javabegin.springms.todo.entity.Statistic;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;
    @PostMapping("/find")
    public ResponseEntity<Statistic> find(@RequestBody Long userId) {
        return ResponseEntity.ok(statisticService.findStatistic(userId));
    }

}
