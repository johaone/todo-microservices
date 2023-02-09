package ru.javabegin.springms.todo.affairs.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javabegin.springms.todo.affairs.feign.UserFeignClient;
import ru.javabegin.springms.todo.affairs.search.CategorySearchValues;
import ru.javabegin.springms.todo.affairs.service.CategoryService;
import ru.javabegin.springms.todo.entity.Category;
import ru.javabegin.springms.todo.entity.UserData;
import ru.javabegin.springms.todo.utils.resttemplate.UserRestBuilder;
import ru.javabegin.springms.todo.utils.webclient.UserWebClientBuilder;

import java.util.List;
import java.util.Optional;

/**
 * КОНТРОЛЛЕР, ЭТО СЛОЙ, НА КОТОРОГО ПРИХОДИТ ЗАПРОС ОТ КЛИЕНТА, А САМ КОНТРОЛЛЕР ОБРАЩАЕТСЯ В СЕРВИС
 */

@RestController
@RequestMapping("/category") // базовый URI
public class CategoryController {
    // Через DI нужно создать ссылку на сервис - доступ к БД, ссылку на класс UserWebClientBuilder для "общения" мс между собой
    private final CategoryService categoryService;

    // ниже клиенты для вызова мс
    private final UserFeignClient userFeignClient;
//    private final UserRestBuilder userRestBuilder; // для синхронного вызова мс
//    private final UserWebClientBuilder userWebClientBuilder; // как для синхронного так и для асинхронного вызова мс


    // Чтобы DI работал нужен конструктор
    public CategoryController(CategoryService categoryService,
//                              UserRestBuilder userRestBuilder,
//                              UserWebClientBuilder userWebClientBuilder,
                              @Qualifier("ru.javabegin.springms.todo.affairs.feign.UserFeignClient") UserFeignClient userFeignClient) {

        this.categoryService = categoryService;
        this.userFeignClient = userFeignClient;
//        this.userRestBuilder = userRestBuilder;
//        this.userWebClientBuilder = userWebClientBuilder;
    }


    /**
     * Метод POST
     */
//https://javabegin.ru/courses/spring-restful/lessons/poluchenie-dannyh-metodom-post-2/
    // уязвимые данные получаются через POST метод с указанием каких либо данных в тело метода
    @PostMapping("/all")
    // POST - НЕиденпотентный, то есть повторный запрос меняет состояние сервера. (Повторный тот же запрос в банк спишет повторно деньги)
    public List<Category> findAll(@RequestBody Long id) { //  в параметры email также передается в формате json, указывается аннотация для считывания этого файла
        return categoryService.findAll(id);
    }

    /**
     * Добавление категории методом POST
     */
//https://javabegin.ru/courses/spring-restful/lessons/dobavlenie-kategorii-metodom-post/
    @PostMapping("/add") //https://www.guru99.com/put-vs-post.html
    // @RequestBody в postman в body нужно отправить именно в формате JSON
    public ResponseEntity<Category> add(@RequestBody Category category) { // ResponseEntity - специальный объект, содержащий статус ответа(ок, ошибка и т.д., и объекты, указанные с помощью женерикс(как в данном случае))
        // проверка на обязательные параметры
        if (category.getId() != null && category.getId() != 0) { // Это означает, что id заполнено. Значит такая категория уже существует в БД
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно

            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) { // trim() - удаление пробелов по краям текста. Т.е. учитывается длина текста без учёта пробелов
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        // так как БД разделены, foreign key на user нет, то может случиться добавление записи(задачи, категории) для несуществующего user
        // проверка на наличие user: через RestTemplate и WebClient(предпочтительнее)

        //RESTTEMPLATE https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/sinhronnyj-vyzov-cherez-resttemplate/
       /* if (userRestBuilder.userExist(category.getUserId())) { // вызов МС  из другого модуля (todo-users)
            return ResponseEntity.ok(categoryService.add(category)); // Так как add возвращает тип ResponseEntity<Category>, то результат - добавленный объект с заполненным ID
        } */

        //WEBCLIENT(sync) - https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/asinhronnyj-vyzov-cherez-webclient/
        /*if (userWebClientBuilder.userExistSync(category.getUserId())) {
            return ResponseEntity.ok(categoryService.add(category));
        }*/

        //WEBCLIENT(async) - https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/asinhronnyj-vyzov-cherez-webclient-2/
//        userWebClientBuilder.userExistAsync(category.getUserId()).subscribe(user -> System.out.println("user = " + user));

        // FEIGN interface (без Circuit Breaker) https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/dobavlenie-feign-v-proekt/
        /*if (userFeignClient.findUserById(category.getUserId()) != null)  {
            return ResponseEntity.ok(categoryService.add(category));
        }*/

        // https://javabegin.ru/courses/mikroservisy-na-spring-cloud-java-kotlin/lessons/dobavlenie-vykljuchatelej-v-proekt/
        ResponseEntity<UserData> result = userFeignClient.findUserById(category.getUserId());
        if (result == null) { // если мс недоступен, возвращается null
            return new ResponseEntity("система пользователей недоступна, попробуйте позднее!", HttpStatus.NOT_FOUND);
        }

        if (result.getBody() != null) {// если пользователь не пустой
            return ResponseEntity.ok(categoryService.add(category));
        }

        // Если выполнить после асинхронного метода проверки на наличие пользователя, то независимо есть такой user или нет, будет выходить "id не найден". Поэтому асинхронный метод не подходит
        // А синхронный дожидается ответа и далее добавляет, либо выводит, что не нашел пользователя
        return new ResponseEntity("user id = " + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }


    /**
     * Обновление категории методом PUT
     */
//https://javabegin.ru/courses/spring-restful/lessons/obnovlenie-kategorii-metodom-put/
    @PutMapping("/update") // Метод идемпотентный - повторная отправка запроса не влияет на сервер
    public ResponseEntity update(@RequestBody Category category) { //Будет возвращать только статус, а не объект entity, как в POST
        // проверка на обязательные параметры
        if (category.getId() == null && category.getId() == 0) { // Значит такой категории нет в БД для его обновления

            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) { // trim() - удаление пробелов по краям текста. Т.е. учитывается длина текста без учёта пробелов
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE); // Вызванная категория не может быть без названия
        }

        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK); // возвращает только статус 200 - ОК
    }

    /**
     * Удаление категории методом DELETE
     */
//https://javabegin.ru/courses/spring-restful/lessons/udalenie-kategorii-metodom-delete-2/
    // DELETE - идемпотентный метод. Удаление можно также производить через POST, причем id категории для удаления передается в body
    @DeleteMapping("/delete/{id}") // id категории, которую надо удалить, предается в адресной строке.
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // Применим исключение ошибки stacktrace. Через try-catch можно обработать исключение в статус
        try {
            categoryService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // возвращает статус сервера

    }

    /**
     * Поиск категории методом POST по названию и email пользователя
     */
//https://javabegin.ru/courses/spring-restful/lessons/poisk-kategorii-metodom-post-2/
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) { // Создали отдельный класс с параметрами title и email. См пакет search

        if ((categorySearchValues.getUserId() == null || categorySearchValues.getUserId() == 0)) {
            return new ResponseEntity("missed param: userId ", HttpStatus.NOT_ACCEPTABLE);
        }

        // если title будет пустым, то будет поиск всех категорий без фильтрации названия

        List<Category> list = categoryService.findByTitleOrUserId(categorySearchValues.getTitle(), categorySearchValues.getUserId());

        return ResponseEntity.ok(list);
    }

//https://javabegin.ru/courses/spring-restful/lessons/poisk-kategorii-po-id-metodom-post-2/

    /**
     * Поиск категории по ID методом POST
     */
    @PostMapping("/searchById") // ID передаем в тело метода - безопасно
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Optional<Category> category = categoryService.findById(id);
        try {
            if (category.isPresent()) { // если объект найден
                return ResponseEntity.ok(category.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Поиск категорий по ID методом GET
     */
    @GetMapping("/find/{id}") // id передаем в поисковую строку - это небезопасно
    public ResponseEntity<Category> find(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        try {
            if (category.isPresent()) { // если объект найден
                return ResponseEntity.ok(category.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);


    }
}

