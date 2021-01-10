package ua.ugolek.repository.dto.extractors;

import ua.ugolek.dto.ProductQuestionDTO;
import ua.ugolek.dto.mappers.ProductQuestionDTOMapper;
import ua.ugolek.model.Product;
import ua.ugolek.model.ProductQuestion;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ProductQuestionDTOExtractor extends DTOExtractor<ProductQuestion, SearchFilter, ProductQuestionDTO>
{
    private static final String PRODUCT_FIELD = "product";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String TEXT_FIELD = "text";

    public ProductQuestionDTOExtractor(SearchFilter filter, EntityManager entityManager,
        ProductQuestionDTOMapper dtoMapper)
    {
        super(filter, entityManager, dtoMapper);
    }

    @Override
    protected <P> void populateQuery(CriteriaQuery<P> query, From<?, ProductQuestion> root)
    {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Join<ProductQuestion, Product> product = root.join(PRODUCT_FIELD);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(createLikePredicate(product, PRODUCT_NAME_FIELD, stringForSearch));
            predicates.add(createLikePredicate(root, TEXT_FIELD, stringForSearch));

            query.where(criteriaBuilder.or(predicates.toArray(Predicate[]::new)));
        });
    }
}
