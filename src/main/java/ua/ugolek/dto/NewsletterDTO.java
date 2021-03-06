package ua.ugolek.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ua.ugolek.Constants;
import ua.ugolek.model.NewsletterStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsletterDTO implements DTO
{
    private Long id;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime createdDate;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime dispatchDate;

    private String createdBy;

    private String subject;

    private NewsletterStatus status;

}
