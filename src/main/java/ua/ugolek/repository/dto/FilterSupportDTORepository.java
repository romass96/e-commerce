package ua.ugolek.repository.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.ugolek.dto.DTO;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FilterSupportDTORepository<T,F extends SearchFilter, U extends DTO>  {
    private final static int ENTITY_CLASS_INDEX = 0;
    protected final Class<T> entityClass;
    protected Function<T, U> dtoMapper;

    @PersistenceContext
    protected EntityManager entityManager;

    protected CriteriaBuilder criteriaBuilder;

    protected FilterSupportDTORepository() {
        this.entityClass = (Class<T>) ReflectionUtil.getGenericClass(getClass(), ENTITY_CLASS_INDEX);
    }

    @PostConstruct
    public void setupCriteriaBuilder() {
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<U> filter(F filter, Pageable pageable) {
        CriteriaQuery<Long> countQuery = getCountQuery(filter);
        long count = entityManager.createQuery(countQuery).getSingleResult();
        List<U> dtoList = queryDTOList(filter);
        return new PageImpl<>(dtoList, pageable, count);
    }

    protected List<U> queryDTOList(F filter) {
        CriteriaQuery<T> query = getSelectQuery(filter);
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery, filter.getPageNumber(), filter.getPerPage());

        return typedQuery.getResultList().stream().map(dtoMapper).collect(Collectors.toList());
    }

    private CriteriaQuery<T> getSelectQuery(F filter) {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        populateQuery(filter, query, root);
        applySorting(filter, query, root);
        return query.select(root);
    }

    protected void applySorting(F filter, CriteriaQuery<?> query, From<?, T> root) {
        filter.getSortByOptional().ifPresent(sortBy -> {
            Function<Path<T>, Order> sortFunction = filter.isSortDesc() ?
                    criteriaBuilder::desc : criteriaBuilder::asc;
            query.orderBy(sortFunction.apply(root.get(sortBy)));
        });
    }

    private CriteriaQuery<Long> getCountQuery(F filter) {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(entityClass);
        populateQuery(filter, query, root);
        return query.select(criteriaBuilder.count(root));
    }

    protected void setPaginationParameters(TypedQuery<?> query, int pageNumber, int perPage) {
        query.setFirstResult((pageNumber - 1) * perPage);
        query.setMaxResults(perPage);
    }

    protected abstract <P> void populateQuery(F filter, CriteriaQuery<P> query, From<?, T> root);

    //TODO Case-insensitive search
    protected String getLikePattern(String input) {
        return "%" + input + "%";
    }

    protected Predicate createLikePredicate(Path<?> root, String fieldName, String likeString) {
        return criteriaBuilder.like(root.get(fieldName), getLikePattern(likeString));
    }

}
