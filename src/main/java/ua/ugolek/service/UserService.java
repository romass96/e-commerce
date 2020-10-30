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
import ua.ugolek.model.Role;
import ua.ugolek.model.User;
import ua.ugolek.model.UserSetting;
import ua.ugolek.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends CrudService<User> implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public User createAdmin(User user) {
        user.setRole(Role.ADMIN);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public User createManager(User user) {
        user.setRole(Role.MANAGER);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
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
