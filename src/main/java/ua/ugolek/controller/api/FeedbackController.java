package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.Feedback;
import ua.ugolek.payload.FeedbackFilter;
import ua.ugolek.payload.FeedbackListResponse;
import ua.ugolek.service.FeedbackService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedbacks")
@Slf4j
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAll();
    }

    @PostMapping("/filter")
    public FeedbackListResponse getFeedbacksByFilter(@RequestBody FeedbackFilter filter) {
        return feedbackService.queryByFilter(filter);
    }

    @GetMapping("/count")
    public Map<String, Long> getFeedbacksCount() {
        return feedbackService.getFeedbacksCount();
    }


}
