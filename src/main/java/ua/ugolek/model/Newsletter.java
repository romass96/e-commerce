package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "newsletters")
@Data
@NoArgsConstructor
public class Newsletter extends Auditable<User>
{
    private String htmlContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    private boolean dispatched;

    private String subject;
}
