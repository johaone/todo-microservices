package ru.javabegin.springms.todo.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.javabegin.springms.todo.entity.UserData;
import ru.javabegin.springms.todo.users.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserData add(UserData user) {
        return userRepository.save(user);
    }

    public UserData update(UserData user){
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserData> findById(Long id) {
        return userRepository.findById(id);
    }

    public UserData findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }

    public void deleteByEmail(String email){
        userRepository.deleteByEmail(email);
    }

    public Page<UserData> findByParam(String email, String username, Long id, PageRequest paging) {
        return userRepository.findByParam(email, username, id, paging);
    }
}
