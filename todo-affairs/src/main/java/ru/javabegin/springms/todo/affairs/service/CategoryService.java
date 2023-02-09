package ru.javabegin.springms.todo.affairs.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.affairs.repository.CategoryRepository;
import ru.javabegin.springms.todo.entity.Category;

import java.util.List;
import java.util.Optional;

/**СЕРВИСЫ НУЖНЫ ДЛЯ СОЗДАНИЯ API BACKEND ДЛЯ ВЫЗОВА ИЗ КОНТРОЛЛЕРА*/
// Сервисы это посредник, стоящий между контроллером(а к нему обращается браузер), для доступа к репозиториям,
// чтобы оттуда получить нужные SQL запросы для получения данных из БД. Даже если методов мало и можно обращаться к контроллерам напрямую,
    // лучше использовать отдельный класс Сервисов

    // Если в методе несколько запросов нужно указать @Transactional, и если в одном из запросов будет ошибка, то
    // все остальные запросы откатятся назад (Rollback)

@Transactional
@Service
public class CategoryService {
    // Через механизм DI создается ссылка на репозиторий, который при старте приложения подставит в эту переменную нужные класс-реализацию
    private final CategoryRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    // Но чтобы сработал DI, нужно создать конструктор в параметры которого передается объект репозитория(что нужно внедрить в ссылку)
    public CategoryService(CategoryRepository repository) { // Объект репозитория создается на лету, это берет на себя Spring
        this.repository = repository;
    }

    // Помимо ссылки на репозиторий, можно также реализовать методы работы с данными, а вызывать его уже из контроллера
    public Optional<Category> findById(Long id) { // этот метод не из интерфейса, его создали сами
        return repository.findById(id); // А этот метод из интерфейса репозитории. Сначала извлечь, затем вызвать
    }

    public  List<Category> findAll(Long userId) {
        return repository.findByUserId(userId);
    }

    public Category add(Category category) {
        return repository.save(category); // Может как создать, так и обновить объект. В данном случае, создает новый объект, так как вызывается для добавления новой категории
    }

    public Category update(Category category) {
        return repository.save(category); // Тут он обновляет уже существующую категорию
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<Category> findByTitleOrUserId(String text, Long userId) {
        return repository.findByTitleOrUserId(text, userId);
    }
}
