package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends BaseEntity {

    @NotBlank
    @Size(max = 30)
    private String firstName;

    @NotBlank
    @Size(max = 30)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @NotBlank
    private String phoneNumber;

    @Column(name = "registration_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private ClientCreationMode clientCreationMode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime birthday;

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
