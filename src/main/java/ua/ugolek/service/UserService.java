package ua.ugolek.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ugolek.Constants;
import ua.ugolek.exception.ExpiredTokenException;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.Role;
import ua.ugolek.model.User;
import ua.ugolek.model.UserSetting;
import ua.ugolek.repository.PasswordResetTokenRepository;
import ua.ugolek.repository.UserRepository;
import ua.ugolek.model.PasswordResetToken;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService extends CrudService<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User createAdmin(User user) {
        user.setRole(Role.ADMIN);
        return createUserWithEncodedPassword(user);
    }

    public User createManager(User user) {
        user.setRole(Role.MANAGER);
        return createUserWithEncodedPassword(user);
    }

    private User createUserWithEncodedPassword(User user) {
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
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

    private List<UserSetting<?>> getDefaultSettings() throws JsonProcessingException {
        List<UserSetting<?>> settings = new ArrayList<>();

        UserSetting<Integer> itemsOnFeedbacksPage = new UserSetting<>(Integer.class);
        itemsOnFeedbacksPage.setName(Constants.FEEDBACK_ITEMS_ON_PAGE_PROPERTY);
        itemsOnFeedbacksPage.setValue(Constants.ITEMS_ON_PAGE);
        settings.add(itemsOnFeedbacksPage);

        UserSetting<Integer[]> itemsOnFeedbacksPageOptions = new UserSetting<>(Integer[].class);
        itemsOnFeedbacksPageOptions.setName(Constants.FEEDBACK_ITEMS_ON_PAGE_OPTIONS_PROPERTY);
        itemsOnFeedbacksPageOptions.setValue(Constants.ITEMS_ON_PAGE_OPTIONS);
        settings.add(itemsOnFeedbacksPageOptions);

        UserSetting<Integer> itemsOnClientsPage = new UserSetting<>(Integer.class);
        itemsOnClientsPage.setName(Constants.CLIENTS_ITEMS_ON_PAGE_PROPERTY);
        itemsOnClientsPage.setValue(Constants.ITEMS_ON_PAGE);
        settings.add(itemsOnClientsPage);

        UserSetting<Integer[]> itemsOnClientsPageOptions = new UserSetting<>(Integer[].class);
        itemsOnClientsPageOptions.setName(Constants.CLIENTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY);
        itemsOnClientsPageOptions.setValue(Constants.ITEMS_ON_PAGE_OPTIONS);
        settings.add(itemsOnClientsPageOptions);

        UserSetting<Integer> itemsOnProductsPage = new UserSetting<>(Integer.class);
        itemsOnProductsPage.setName(Constants.PRODUCTS_ITEMS_ON_PAGE_PROPERTY);
        itemsOnProductsPage.setValue(Constants.ITEMS_ON_PAGE);
        settings.add(itemsOnProductsPage);

        UserSetting<Integer[]> itemsOnProductsPageOptions = new UserSetting<>(Integer[].class);
        itemsOnProductsPageOptions.setName(Constants.PRODUCTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY);
        itemsOnProductsPageOptions.setValue(Constants.ITEMS_ON_PAGE_OPTIONS);
        settings.add(itemsOnProductsPageOptions);

        return settings;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }
}
