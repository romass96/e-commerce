package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Feedback;
import ua.ugolek.repository.BaseEntityRepository;
import ua.ugolek.repository.FeedbackRepository;

import java.util.HashMap;
import java.util.Map;

import static ua.ugolek.Constants.*;

@Service
public class FeedbackService extends CRUDService<Feedback>
{
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository baseEntityRepository)
    {
        super(baseEntityRepository);
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
