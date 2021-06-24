package by.note.home.repository;

import by.note.home.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationKey(String key);
}
