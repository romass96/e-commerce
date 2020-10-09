package ua.ugolek.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ua.ugolek.util.DateUtils;

import java.time.LocalDateTime;

@Setter
@Getter
public class FeedbackFilter {
    private int pageNumber;
    private int perPage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDate;
    private Long categoryId;
    private double fromRating;
    private double toRating;
    private String subSequence;

    public LocalDateTime getToDate() {
        return toDate == null ? LocalDateTime.now() : toDate;
    }

    public LocalDateTime getFromDate() {
        return fromDate == null ? DateUtils.getInitialDate() : fromDate;
    }
}
