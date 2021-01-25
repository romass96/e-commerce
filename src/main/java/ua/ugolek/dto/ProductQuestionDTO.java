package ua.ugolek.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ua.ugolek.Constants;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductQuestionDTO implements DTO
{
    private Long id;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime createdDate;

    private Long clientId;
    private String clientFullName;
    private String productName;
    private String text;
}
