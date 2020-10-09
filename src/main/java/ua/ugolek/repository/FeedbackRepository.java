package ua.ugolek.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.ugolek.model.Feedback;

import java.time.LocalDateTime;
import java.util.Date;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query(value = "SELECT f FROM Feedback f INNER JOIN f.product p " +
            "WHERE (f.createdDate BETWEEN :fromDate AND :toDate) " +
            "AND (f.rating BETWEEN :fromRating AND :toRating) " +
            "AND (:subSequence IS NULL " +
                "OR f.text LIKE %:subSequence% " +
                "OR f.disadvantages LIKE %:subSequence% " +
                "OR f.advantages LIKE %:subSequence%)" +
            "AND p.category.id = :categoryId")
    Page<Feedback> filter(Pageable pageable,
                          @Param("fromDate") LocalDateTime fromDate,
                          @Param("toDate") LocalDateTime toDate,
                          @Param("categoryId") Long categoryId,
                          @Param("fromRating") double fromRating,
                          @Param("toRating") double toRating,
                          @Param("subSequence") String subSequence);

    @Query(value = "SELECT f FROM Feedback f " +
            "WHERE (f.createdDate BETWEEN :fromDate AND :toDate) " +
            "AND (:subSequence IS NULL " +
                "OR f.text LIKE %:subSequence% " +
                "OR f.disadvantages LIKE %:subSequence% " +
                "OR f.advantages LIKE %:subSequence%)" +
            "AND (f.rating BETWEEN :fromRating AND :toRating)")
    Page<Feedback> filter(Pageable pageable,
                          @Param("fromDate") LocalDateTime fromDate,
                          @Param("toDate") LocalDateTime toDate,
                          @Param("fromRating") double fromRating,
                          @Param("toRating") double toRating,
                          @Param("subSequence") String subSequence);

    Long countByRatingBetween(double ratingFrom, double ratingTo);
    Long countByRatingGreaterThan(double ratingFrom);
    Long countByRatingLessThan(double ratingTo);
}
