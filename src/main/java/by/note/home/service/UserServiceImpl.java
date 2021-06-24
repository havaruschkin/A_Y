package by.note.home.service;

import by.note.home.exeption.EmailAlreadyUsedException;
import by.note.home.exeption.UsernameAlreadyUsedException;
import by.note.home.model.Authority;
import by.note.home.model.Status;
import by.note.home.model.User;
import by.note.home.repository.AuthorityRepository;
import by.note.home.repository.UserRepository;
import by.note.home.security.AuthoritiesConstants;
import by.note.home.service.dto.UserDTO;
import by.note.home.service.util.RandomUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional
public class UserServiceImpl implements UserServise {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrationUser(UserDTO userDTO, String password) {
        userRepository.findByLogin(userDTO.getLogin().toLowerCase()).ifPresent(user -> {
            if (user.isActivated()) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            if (user.isActivated()) {
                throw new EmailAlreadyUsedException();
            }
        });
        String encryptedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setStatus(Status.ACTIVE);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findByName(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        return newUser;
    }

    public Optional<User> activateRegistration(@RequestParam(value = "key") String key) {
        return userRepository.findByActivationKey(key).map(user -> {
            user.setActivationKey(null);
            user.setActivated(true);
            return user;
        });
    }

    public void updateUser(UserDTO userDTO) {
        userRepository.findById(userDTO.getId())
                .map(user -> {
                    user.setEmail(userDTO.getEmail());
                    user.setAuthorities(authorityRepository.findByNameIn(userDTO.getAuthorities()));
                    return user;
                });
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void lockUsers(List<Long> userIds) {
        for (Long id : userIds) {
            findUser(id).ifPresent(user -> user.setStatus(Status.BLOCKED));
        }
    }

    public void unlockUsers(List<Long> userIds) {
        for (Long id : userIds) {
            findUser(id).ifPresent(user -> user.setStatus(Status.ACTIVE));
        }
    }

    public void deleteUsers(List<Long> userIds) {
        for (Long id : userIds) {
            deleteUser(id);
        }
    }

    public User findCurrentUser(String login) {
        return userRepository.findByLogin(login).orElseThrow(IllegalArgumentException::new);
    }

    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByLogin(String name) {
        return userRepository.findByLogin(name);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
