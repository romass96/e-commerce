package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.DTO;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.util.ReflectionUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class DTOExtractor<T, F extends SearchFilter, U extends DTO> {
    private final static int ENTITY_CLASS_INDEX = 0;
    protected final F filter;
    protected final EntityManager entityManager;
    protected final CriteriaBuilder criteriaBuilder;
    private final Class<T> entityClass;
    private final Function<T, U> dtoMapper;

    public DTOExtractor(F filter, EntityManager entityManager, Function<T, U> dtoMapper) {
        this.filter = filter;
        this.entityManager = entityManager;
        this.dtoMapper = dtoMapper;
        this.entityClass = (Class<T>) ReflectionUtil.getGenericClass(getClass(), ENTITY_CLASS_INDEX);
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public List<U> queryDTOList() {
        CriteriaQuery<T> query = getSelectQuery();
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        setPaginationParameters(typedQuery);

        return typedQuery.getResultList().stream().map(dtoMapper).collect(Collectors.toList());
    }

    public long queryDTOCount() {
        CriteriaQuery<Long> countQuery = getCountQuery();
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private CriteriaQuery<Long> getCountQuery() {
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<T> root = query.from(entityClass);
        populateQuery(query, root);
        return query.select(criteriaBuilder.count(root));
    }

    protected abstract <P> void populateQuery(CriteriaQuery<P> query, From<?, T> root);

    protected void applySorting(CriteriaQuery<?> query, From<?, T> root) {
        filter.getSortByOptional().ifPresent(sortBy -> {
            Function<Path<T>, Order> sortFunction = filter.isSortDesc() ?
                    criteriaBuilder::desc : criteriaBuilder::asc;
            query.orderBy(sortFunction.apply(root.get(sortBy)));
        });
    }

    protected void setPaginationParameters(TypedQuery<?> query) {
        query.setFirstResult((filter.getPageNumber() - 1) * filter.getPerPage());
        query.setMaxResults(filter.getPerPage());
    }

    //TODO Case-insensitive search
    protected String getLikePattern(String input) {
        return "%" + input + "%";
    }

    protected Predicate createLikePredicate(Path<?> root, String fieldName, String likeString) {
        return criteriaBuilder.like(root.get(fieldName), getLikePattern(likeString));
    }

    private CriteriaQuery<T> getSelectQuery() {
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        populateQuery(query, root);
        applySorting(query, root);
        return query.select(root);
    }

}
