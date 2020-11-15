package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.dto.FeedbackDTO;
import ua.ugolek.model.Feedback;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.FeedbackFilter;
import ua.ugolek.service.FeedbackService;
import ua.ugolek.service.dto.FeedbackDTOService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedbacks")
@Slf4j
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackDTOService feedbackDTOService;

    @GetMapping("")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAll();
    }

    @PostMapping("/filter")
    public ListResponse<FeedbackDTO> getFeedbacksByFilter(@RequestBody FeedbackFilter filter) {
        return feedbackDTOService.queryByFilter(filter);
    }

    @GetMapping("/count")
    public Map<String, Long> getFeedbacksCount() {
        return feedbackService.getFeedbacksCount();
    }

}
