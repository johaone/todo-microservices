package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.StatisticRepository;
import ru.javabegin.springms.todo.entity.Statistic;

@Service
@Transactional
public class StatisticService {
    StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public Statistic findStatistic (Long userId) {
        return statisticRepository.findByUserId(userId);
    }
}
