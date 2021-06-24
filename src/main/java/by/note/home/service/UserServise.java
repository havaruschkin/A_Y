package by.note.home.service;

import by.note.home.model.User;
import by.note.home.service.dto.UserDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserServise {

    User registrationUser(UserDTO userDTO, String password);

    Optional<User> activateRegistration(@RequestParam(value = "key") String key);

    void updateUser(UserDTO userDTO);

    List<User> findAllUsers();

    void lockUsers(List<Long> userIds);

    void unlockUsers(List<Long> userIds);

    void deleteUsers(List<Long> userIds);

    User findCurrentUser(String login);

    Optional<User> findUser(Long id);

    Optional<User> findUserByLogin(String name);

    void deleteUser(Long id);
}
