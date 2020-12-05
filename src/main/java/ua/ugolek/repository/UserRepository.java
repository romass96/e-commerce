package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseEntityRepository<User> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
