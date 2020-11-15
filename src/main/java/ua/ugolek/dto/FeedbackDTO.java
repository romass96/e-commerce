package ua.ugolek.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackDTO implements DTO {
    private Long id;
    private String text;
    private String clientFullName;
    private String productName;
    private Double rating;
    private String advantages;
    private String disadvantages;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdDate;
}
