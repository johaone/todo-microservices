package ru.javabegin.springms.todo.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.javabegin.springms.todo.entity.UserData;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {

    UserData findByEmailOrUsername(String email, String username ); // возвращение единичного пользователя по его email

    void deleteByEmail(String email);

    @Query("SELECT u FROM UserData u where " +
            "(:email is null or :email='' or lower(u.email) like lower(concat('%', :email,'%'))) or " +
            "(:username is null or :username='' or lower(u.username) like lower(concat('%', :username, '%')))" +
            "and (u.id=:id)"
    )
    Page<UserData> findByParam(@Param("email") String email,
                               @Param("username") String username,
                               @Param("id") Long  id,
                               Pageable pageable);

}
