package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ExpiredTokenException;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.*;
import ua.ugolek.repository.BaseEntityRepository;
import ua.ugolek.repository.PasswordResetTokenRepository;
import ua.ugolek.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService extends CRUDService<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final DefaultSettingsConfiguration defaultConfiguration = new DefaultSettingsConfiguration();

    @Autowired
    public UserService(UserRepository baseEntityRepository)
    {
        super(baseEntityRepository);
    }

    public User createAdmin(User user) {
        user.setRole(Role.ADMIN);
        return create(user);
    }

    public User createManager(User user) {
        user.setRole(Role.MANAGER);
        return create(user);
    }

    @Override
    public User create(User user)
    {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        List<UserSetting> settings = defaultConfiguration.getSettings();
        user.setSettings(settings);
        return userRepository.save(user);
    }

    public void updateUserPassword(User user, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public void lockManager(Long userId) {
        changeLockedManager(userId, true);
    }

    public void unlockManager(Long userId) {
        changeLockedManager(userId, false);
    }

    private void changeLockedManager(Long userId, boolean locked) {
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getRole() == Role.MANAGER) {
                user.setLocked(locked);
                userRepository.save(user);
            }
        });
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public PasswordResetToken createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    public void changePasswordForUserByToken(String password, String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
            .orElseThrow(() -> new ObjectNotFoundException("Token " + token + " is not found"));
        if (passwordResetToken.isExpired()) {
            throw new ExpiredTokenException();
        }
        User user = passwordResetToken.getUser();
        updateUserPassword(user, password);
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    }

}
