package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.model.Product;
import ua.ugolek.model.PropertyDefinition;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.projection.ProductSoldProjection;
import ua.ugolek.repository.AdvancedProductRepository;
import ua.ugolek.repository.ProductRepository;
import ua.ugolek.repository.PropertyDefinitionRepository;

import java.util.List;
import java.util.Map;

@Service
public class ProductService extends FilterSupportService<Product, SearchFilter> {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PropertyDefinitionRepository definitionRepository;

    @Autowired
    public ProductService(AdvancedProductRepository filterSupportRepository) {
        super(filterSupportRepository);
    }

    @Override
    public Product create(Product product) {
        product.getProperties().forEach(property -> {
            PropertyDefinition definition = definitionRepository.save(property.getDefinition());
            property.setDefinition(definition);
        });
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        if (productRepository.existsById(id)) {
            return productRepository.save(product);
        }
        throw new ObjectNotFoundException(Product.class.getSimpleName(), id);
    }

    public List<ProductSoldProjection> countSoldProducts() {
        return productRepository.countProductsByOrderStatus(OrderStatus.COMPLETED);
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }
}
