package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Feedback;
import ua.ugolek.payload.FeedbackFilter;
import ua.ugolek.payload.FeedbackListResponse;
import ua.ugolek.repository.FeedbackRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.ugolek.Constants.*;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAll() {
        return feedbackRepository.findAll();
    }

    public Feedback create(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public FeedbackListResponse queryByFilter(FeedbackFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPerPage());
        Page<Feedback> feedbackPage;
        if (filter.getCategoryId() != null) {
            feedbackPage = feedbackRepository.filter(pageable,
                    filter.getFromDate(),
                    filter.getToDate(),
                    filter.getCategoryId(),
                    filter.getFromRating(),
                    filter.getToRating(),
                    filter.getSubSequence());
        } else {
            feedbackPage = feedbackRepository.filter(pageable,
                    filter.getFromDate(),
                    filter.getToDate(),
                    filter.getFromRating(),
                    filter.getToRating(),
                    filter.getSubSequence());
        }

        FeedbackListResponse response = new FeedbackListResponse();
        response.setFeedbacks(feedbackPage.getContent());
        response.setTotalItems(feedbackPage.getTotalElements());
        response.setTotalPages(feedbackPage.getTotalPages());
        return response;
    }

    public Map<String, Long> getFeedbacksCount() {
        Map<String, Long> map = new HashMap<>();
        map.put(ALL_FEEDBACKS, feedbackRepository.count());
        map.put(POSITIVE_FEEDBACK_TYPE, feedbackRepository.countByRatingGreaterThan(POSITIVE_LOWER_BOUND));
        map.put(NEGATIVE_FEEDBACK_TYPE, feedbackRepository.countByRatingLessThan(NEGATIVE_HIGHER_BOUND));
        map.put(NORMAL_FEEDBACK_TYPE, feedbackRepository.countByRatingBetween(NEGATIVE_HIGHER_BOUND,POSITIVE_LOWER_BOUND));
        return map;
    }
}
