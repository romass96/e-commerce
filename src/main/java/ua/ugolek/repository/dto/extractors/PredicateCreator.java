package ua.ugolek.repository.dto.extractors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PredicateCreator
{
    private final CriteriaBuilder criteriaBuilder;
    private final List<Predicate> predicates = new ArrayList<>();

    public PredicateCreator(CriteriaBuilder criteriaBuilder)
    {
        this.criteriaBuilder = criteriaBuilder;
    }

    protected Predicate createLikePredicate(Expression<String> likeExpression, String likeString) {
        String pattern = getLikePattern(likeString);
        return criteriaBuilder.like(likeExpression, pattern);
    }

    protected String getLikePattern(String input) {
        return "%" + input + "%";
    }

    public <Y extends Comparable> void addGreaterThanOrEqualToPredicate(Expression<Y> expression, Y comparingObject) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, comparingObject));
    }

    public <Y extends Comparable> void addLessThanOrEqualToPredicate(Expression<Y> expression, Y comparingObject) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, comparingObject));
    }

    public void addEqualToPredicate(Expression<?> expression, Object comparingObject) {
        predicates.add(criteriaBuilder.equal(expression, comparingObject));
    }

    public <Y extends Comparable> void addBetweenPredicate(Expression<Y> expression, Y lowerBound, Y upperBound) {
        predicates.add(criteriaBuilder.between(expression, lowerBound, upperBound));
    }

    public void addStringSearch(String stringForSearch, List<Expression<String>> expressions) {
        if (!expressions.isEmpty()) {
            Predicate[] stringSearchPredicates = expressions.stream()
                .map(expression -> createLikePredicate(expression, stringForSearch))
                .toArray(Predicate[]::new);
            predicates.add(criteriaBuilder.or(stringSearchPredicates));
        }
    }

    public void addPredicate(Predicate predicate) {
        this.predicates.add(predicate);
    }

    public Predicate[] getPredicatesAsArray() {
        return predicates.toArray(new Predicate[0]);
    }



}
