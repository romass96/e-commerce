package ua.ugolek.payload.filters;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
public class ProductQuestionFilter extends SearchFilter
{
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;

    private Long categoryId;

    public Optional<Long> getCategoryIdOptional() {
        return Optional.ofNullable(categoryId);
    }

    public Optional<LocalDateTime> getFromDateOptional() {
        return Optional.ofNullable(fromDate);
    }

    public Optional<LocalDateTime> getToDateOptional() {
        return Optional.ofNullable(toDate);
    }
}
