package ua.ugolek.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
public class ArchivingDetails
{
    @Column(name = "archived", nullable = true)
    private boolean archived = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime archivedDate;

    @ManyToOne
    @JoinColumn(name = "archived_by")
    private User archivedBy;
}
