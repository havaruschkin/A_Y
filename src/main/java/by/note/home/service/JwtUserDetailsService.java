package by.note.home.service;

import by.note.home.exeption.UserBlockedException;
import by.note.home.exeption.UserNotActivatedException;
import by.note.home.model.Status;
import by.note.home.repository.UserRepository;
import by.note.home.model.User;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        String userLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findByLogin(userLogin)
                .map(user -> createSpringSecurityUser(userLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + userLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String userLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + userLogin + " was not activated");
        }
        if (user.getStatus() == Status.BLOCKED) {
            throw new UserBlockedException("User " + userLogin + " blocked");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                grantedAuthorities);
    }
}
