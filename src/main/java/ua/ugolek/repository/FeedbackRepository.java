package ua.ugolek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Long countByRatingBetween(double ratingFrom, double ratingTo);
    Long countByRatingGreaterThan(double ratingFrom);
    Long countByRatingLessThan(double ratingTo);
}
