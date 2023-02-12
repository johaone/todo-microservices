package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.PriorityRepository;
import ru.javabegin.springms.todo.entity.Priority;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository; // сервис имеет право обращаться к репозиторию (БД)


    // Помимо ссылки на репозиторий, можно также реализовать методы работы с данными, а вызывать его уже из контроллера
    public Optional<Priority> findById(Long id) { // этот метод не из интерфейса, его создали сами
        return priorityRepository.findById(id); // А этот метод из интерфейса репозитории. Сначала извлечь, затем вызвать
    }

    public List<Priority> findAll(Long userId) {
        return priorityRepository.findByUserIdOrderByTitle(userId);
    }

    public Priority add(Priority priority) {
        return priorityRepository.save(priority); // Может как создать, так и обновить объект. В данном случае, создает новый объект, так как вызывается для добавления новой категории
    }

    public Priority update(Priority priority) {
        return priorityRepository.save(priority); // Тут он обновляет уже существующую категорию
    }

    public void deleteById(Long id) {
        priorityRepository.deleteById(id);
    }

    public List<Priority> findByTitleOrUserId(String text, Long userId) {
        return priorityRepository.findByTitleOrUserId(text, userId);
    }
}
