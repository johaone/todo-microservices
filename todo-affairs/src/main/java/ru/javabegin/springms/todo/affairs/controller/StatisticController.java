package ru.javabegin.springms.todo.affairs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javabegin.springms.todo.affairs.service.StatisticService;
import ru.javabegin.springms.todo.entity.Statistic;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping("/find") // POST - НЕиденпотентный, то есть повторный запрос меняет состояние сервера. (Повторный тот же запрос в банк спишет повторно деньги)
    public ResponseEntity<Statistic> find(@RequestBody Long userId) { //  в параметры email также передается в формате json, указывается аннотация для считывания этого файла
        return ResponseEntity.ok(statisticService.findStatistic(userId));
    }

}
