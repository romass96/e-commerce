package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners({AuditingEntityListener.class})
public abstract class Auditable<U> extends BaseEntity {

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected U createdBy;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "created_date")
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    protected U lastModifiedBy;

    @Column(name = "last_modified_date")
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    protected LocalDateTime lastModifiedDate;
}
