package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.TaskRepository;
import ru.javabegin.springms.todo.entity.Task;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository; // DI - ссылка для внедрения объекта

    public TaskService(TaskRepository taskRepository) { // конструктор для запуска DI
        this.taskRepository = taskRepository;
    }

    // Помимо ссылки на репозиторий, можно также реализовать методы работы с данными, а вызывать его уже из контроллера
    public Optional<Task> findById(Long id) { // этот метод не из интерфейса, его создали сами
        return taskRepository.findById(id); // А этот метод из интерфейса репозитории. Сначала извлечь, затем вызвать
    }

    public List<Task> findAll(Long userId) {
        return taskRepository.findByUserIdOrderByTitle(userId);
    }

    public Task add(Task task) {
        return taskRepository.save(task); // Может как создать, так и обновить объект. В данном случае, создает новый объект, так как вызывается для добавления новой категории
    }

    public Task update(Task task) {
        return taskRepository.save(task); // Тут он обновляет уже существующую категорию
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Page<Task> findByParam(String title, Boolean completed, Long priorityId, Long categoryId, Long userId, Date dateFrom, Date dateTo, PageRequest paging) {
        return taskRepository.findByParam(title, completed, priorityId, categoryId, userId, dateFrom, dateTo, paging);
    }
}




