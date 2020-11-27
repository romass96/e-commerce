package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_settings")
@NoArgsConstructor
@Data
public class UserSetting extends BaseEntity {

    private String name;
    private String jsonValue;
    private String className;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @JsonIgnore
    public <T> T getValue() throws ClassNotFoundException, JsonProcessingException {
        Class<T> valueClass = (Class<T>) Class.forName(className);
        return new ObjectMapper().readValue(jsonValue, valueClass);
    }

    public void setClass(Class<?> valueClass) {
        this.className = valueClass.getName();
    }

    public <T> void setValue(T value) throws JsonProcessingException {
        this.jsonValue = new ObjectMapper().writeValueAsString(value);
    }

}
