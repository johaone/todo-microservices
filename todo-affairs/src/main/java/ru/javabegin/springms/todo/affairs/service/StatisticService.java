package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.StatisticRepository;
import ru.javabegin.springms.todo.entity.Statistic;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticService {
    StatisticRepository statisticRepository;

    public Statistic findStatistic (Long userId) {
        return statisticRepository.findByUserId(userId);
    }
}
