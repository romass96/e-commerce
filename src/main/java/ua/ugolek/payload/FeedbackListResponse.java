package ua.ugolek.payload;

import lombok.Getter;
import lombok.Setter;
import ua.ugolek.model.Feedback;

import java.util.List;

@Getter
@Setter
public class FeedbackListResponse {
    private long totalItems;
    private List<Feedback> feedbacks;
    private int totalPages;
    private int currentPage;
}
