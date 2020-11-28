package ua.ugolek.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsletterDTO implements DTO
{
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dispatchDate;

    private String createdBy;

    private boolean dispatched;

    private String subject;

}
