package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Feedback;

@Repository
public interface FeedbackRepository extends BaseEntityRepository<Feedback> {
    Long countByRatingBetween(double ratingFrom, double ratingTo);
    Long countByRatingGreaterThan(double ratingFrom);
    Long countByRatingLessThan(double ratingTo);
}
