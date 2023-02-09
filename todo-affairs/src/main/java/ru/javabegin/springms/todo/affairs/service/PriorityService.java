package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.PriorityRepository;
import ru.javabegin.springms.todo.entity.Priority;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PriorityService {
    // Через механизм DI создается ссылка на репозиторий, который при старте приложения подставит в эту переменную нужные класс-реализацию
    private final PriorityRepository priorityRepository; // сервис имеет право обращаться к репозиторию (БД)

    // Но чтобы сработал DI, нужно создать конструктор в параметры которого передается объект репозитория(что нужно внедрить в ссылку)
    public PriorityService(PriorityRepository priorityRepository) { // Объект репозитория создается на лету, это берет на себя Spring
        this.priorityRepository = priorityRepository;
    }

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
