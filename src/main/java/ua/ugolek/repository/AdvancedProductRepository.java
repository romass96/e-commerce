package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Product;

@Repository
public class AdvancedProductRepository extends StringSearchRepository<Product> {

    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";

    protected AdvancedProductRepository()
    {
        super(new String[] {NAME_FIELD, DESCRIPTION_FIELD});
    }
    
}
