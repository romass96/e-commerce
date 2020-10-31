package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Product;
import ua.ugolek.payload.filters.SearchFilter;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Repository
public class AdvancedProductRepository extends StringSearchRepository<Product> {

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";

    protected AdvancedProductRepository()
    {
        super(new String[] {NAME_FIELD, DESCRIPTION_FIELD});
    }
    
}
