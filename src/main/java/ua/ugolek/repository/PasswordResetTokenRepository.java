package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.PasswordResetToken;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends BaseEntityRepository<PasswordResetToken>
{
    Optional<PasswordResetToken> findByToken(String token);
}
