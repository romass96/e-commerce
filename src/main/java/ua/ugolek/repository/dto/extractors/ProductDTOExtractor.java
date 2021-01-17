package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Category;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.ProductFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductDTOExtractor extends DTOExtractor<Product, ProductFilter, ProductDTO> {
    private static final String[] fieldNamesForSearch = new String[]{"name", "description"};

    private static final String ARCHIVING_DETAILS_FIELD = "archivingDetails";
    private static final String ARCHIVED_FIELD = "archived";
    private static final String PRICE_FIELD = "price";
    private static final String CATEGORY_FIELD = "category";

    public ProductDTOExtractor(ProductFilter filter, EntityManager entityManager,
                               Function<Product, ProductDTO> dtoMapper) {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, Product> root) {
        List<Predicate> wherePredicates = new ArrayList<>();

        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            List<Expression<String>> expressions = Stream.of(fieldNamesForSearch)
                .map(fieldName -> getFieldExpression(root, fieldName))
                .collect(Collectors.toList());
            Predicate predicate = getStringSearchPredicate(stringForSearch, expressions);
            wherePredicates.add(predicate);
        });

        filter.getCategoryIdOptional().ifPresent(categoryId -> {
            Join<Product, Category> category = root.join(CATEGORY_FIELD);
            wherePredicates.add(criteriaBuilder.equal(category.get(ID_FIELD), categoryId));
        });

        filter.getToPriceOptional().ifPresent(toPrice ->
            wherePredicates.add(criteriaBuilder.lessThanOrEqualTo(
                root.get(PRICE_FIELD), toPrice)));

        filter.getFromPriceOptional().ifPresent(fromPrice ->
            wherePredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                root.get(PRICE_FIELD), fromPrice)));

        wherePredicates.add(criteriaBuilder.equal(
            root.get(ARCHIVING_DETAILS_FIELD).get(ARCHIVED_FIELD), filter.isArchived()));

        Predicate[] wherePredicatesArray = wherePredicates.toArray(new Predicate[0]);
        query.where(wherePredicatesArray);
    }
}
