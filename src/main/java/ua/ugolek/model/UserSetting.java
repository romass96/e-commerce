package ua.ugolek.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
@Setter
@Getter
@NoArgsConstructor
public class UserSetting<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String jsonValue;
    private String className;

    public UserSetting(Class<T> valueClass) {
        this.className = valueClass.getName();
    }

    public T getValue() throws ClassNotFoundException, JsonProcessingException {
        Class<T> valueClass = (Class<T>) Class.forName(className);
        return new ObjectMapper().readValue(jsonValue, valueClass);
    }

    public void setValue(T value) throws JsonProcessingException {
        this.jsonValue = new ObjectMapper().writeValueAsString(value);
    }
}
