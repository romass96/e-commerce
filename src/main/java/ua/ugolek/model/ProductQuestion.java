package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ua.ugolek.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_questions")
@Data
@NoArgsConstructor
@EntityListeners({ AuditingEntityListener.class})
public class ProductQuestion extends BaseEntity
{
    private String text;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_date", updatable = false)
    @CreatedDate
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime createdDate;
}
