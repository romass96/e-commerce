package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ugolek.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "newsletters")
@Data
@NoArgsConstructor
public class Newsletter extends Auditable<User>
{
    private String htmlContent;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    private String subject;

    @Enumerated(EnumType.STRING)
    private NewsletterStatus status = NewsletterStatus.PENDING;
}
