package by.note.home.controller;

import by.note.home.exeption.AccountResourceException;
import by.note.home.exeption.InvalidPasswordException;
import by.note.home.model.User;
import by.note.home.service.MailService;
import by.note.home.service.UserServise;
import by.note.home.service.dto.UserDTO;
import by.note.home.service.mapper.UserMapper;
import by.note.home.service.vm.UserVM;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountResourceController {

    private final UserServise userService;
    private final MailService mailService;
    private final UserMapper userMapper;

    public AccountResourceController(UserServise userService, MailService mailService, UserMapper userMapper) {
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@RequestBody UserVM userVM) {
        if (!checkPasswordLength(userVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registrationUser(userVM, userVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException();
        }
    }

    @GetMapping("/users")
    public List<UserDTO> allUsers() {
        return userMapper.toUserDTOs(userService.findAllUsers());
    }

    @GetMapping("/users/user/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userMapper.toUserDTO(userService.findCurrentUser(username));
    }

    @PostMapping("/users/lock")
    public void lock(@RequestBody List<Long> userIds) {
        userService.lockUsers(userIds);
    }

    @PostMapping("/users/unlock")
    public void unlock(@RequestBody List<Long> userIds) {
        userService.unlockUsers(userIds);
    }

    @PostMapping("/users/delete")
    public void delete(@RequestBody List<Long> userIds) {
        userService.deleteUsers(userIds);
    }

    @PostMapping("/users/updateUser")
    public void updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= UserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= UserVM.PASSWORD_MAX_LENGTH;
    }
}
