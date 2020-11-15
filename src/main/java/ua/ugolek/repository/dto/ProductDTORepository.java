package ua.ugolek.repository.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ProductDTO;
import ua.ugolek.dto.mappers.ProductDTOMapper;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.stream.Stream;

@Repository
public class ProductDTORepository extends FilterSupportDTORepository<Product, SearchFilter, ProductDTO> {
    private static final String[] fieldNamesForSearch = new String[] {"name", "description"};

    @PostConstruct
    public void initDTOMapper() {
        this.dtoMapper = new ProductDTOMapper();
    }

    @Override
    protected <P> void populateQuery(SearchFilter filter, CriteriaQuery<P> query, From<?, Product> root) {
        filter.getStringForSearchOptional().ifPresent(stringForSearch -> {
            Predicate[] predicates = Stream.of(fieldNamesForSearch)
                    .map(field -> createLikePredicate(root, field, stringForSearch))
                    .toArray(Predicate[]::new);
            query.where(criteriaBuilder.or(predicates));
        });
    }
}
