package ua.ugolek.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.ugolek.Constants.*;

@Slf4j
public class DefaultSettingsConfiguration
{
    public List<UserSetting> getSettings() {
        List<UserSetting> settings = new ArrayList<>();

        createSetting(Integer.class, FEEDBACK_ITEMS_ON_PAGE_PROPERTY, ITEMS_ON_PAGE)
            .ifPresent(settings::add);
        createSetting(Integer[].class, FEEDBACK_ITEMS_ON_PAGE_OPTIONS_PROPERTY, ITEMS_ON_PAGE_OPTIONS)
            .ifPresent(settings::add);
        createSetting(Integer.class, CLIENTS_ITEMS_ON_PAGE_PROPERTY, ITEMS_ON_PAGE)
            .ifPresent(settings::add);
        createSetting(Integer[].class, CLIENTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY, ITEMS_ON_PAGE_OPTIONS)
            .ifPresent(settings::add);
        createSetting(Integer.class, PRODUCTS_ITEMS_ON_PAGE_PROPERTY, ITEMS_ON_PAGE)
            .ifPresent(settings::add);
        createSetting(Integer[].class, PRODUCTS_ITEMS_ON_PAGE_OPTIONS_PROPERTY, ITEMS_ON_PAGE_OPTIONS)
            .ifPresent(settings::add);

        return settings;
    }

    private <T> Optional<UserSetting> createSetting(Class<T> valueClass, String name, T value) {
        try
        {
            UserSetting userSetting = new UserSetting();
            userSetting.setClass(valueClass);
            userSetting.setName(name);
            userSetting.setValue(value);

            return Optional.of(userSetting);
        } catch (  JsonProcessingException e) {
            log.error("Error when creating setting", e);
            return Optional.empty();
        }
    }
}
