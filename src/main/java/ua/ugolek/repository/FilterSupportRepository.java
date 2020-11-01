package ua.ugolek.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.function.Function;

public abstract class FilterSupportRepository<T,F extends SearchFilter> {
    private final Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    protected CriteriaBuilder criteriaBuilder;

    protected FilterSupportRepository() {
        this.entityClass = (Class<T>) ReflectionUtil.getGenericClass(getClass());
    }

    @PostConstruct
    public void setupCriteriaBuilder() {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<T> filter(F filter, Pageable pageable) {
        CriteriaQuery<Long> countQuery = getCountQuery(filter);
        long count = entityManager.createQuery(countQuery).getSingleResult();

        CriteriaQuery<T> query = getSelectQuery(filter);
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery, filter.getPageNumber(), filter.getPerPage());

        return new PageImpl<>(typedQuery.getResultList(), pageable, count);
    }

    private CriteriaQuery<T> getSelectQuery(F filter) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        populateQuery(filter, query, root);
        filter.getSortByOptional().ifPresent(sortBy -> {
            Function<Path<T>, Order> sortFunction = filter.isSortDesc() ?
                    criteriaBuilder::desc : criteriaBuilder::asc;
            query.orderBy(sortFunction.apply(root.get(sortBy)));
        });
        return query.select(root);
    }

    private CriteriaQuery<Long> getCountQuery(F filter) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(entityClass);
        populateQuery(filter, query, root);
        return query.select(criteriaBuilder.count(root));
    }

    private void setPaginationParameters(TypedQuery<?> query, int pageNumber, int perPage) {
        query.setFirstResult((pageNumber - 1) * perPage);
        query.setMaxResults(perPage);
    }

    protected abstract <P> void populateQuery(F filter, CriteriaQuery<P> query, Root<T> root);

    //TODO Case-insensitive search
    protected String getLikePattern(String input) {
        return "%" + input + "%";
    }

    protected Predicate createLikePredicate(Path<?> root, String fieldName, String likeString) {
        return criteriaBuilder.like(root.get(fieldName), getLikePattern(likeString));
    }

}
