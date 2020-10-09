package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.Category;
import ua.ugolek.model.Product;
import ua.ugolek.model.PropertyDefinition;
import ua.ugolek.repository.ProductRepository;
import ua.ugolek.repository.PropertyDefinitionRepository;
import ua.ugolek.repository.PropertyRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PropertyDefinitionRepository definitionRepository;


    public Product add(Product product) {
        product.getProperties().forEach(property -> {
            PropertyDefinition definition = definitionRepository.save(property.getDefinition());
            property.setDefinition(definition);
        });
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Product.class.getSimpleName(), id));
    }

    public Product update(Long id, Product product) {
        if (productRepository.existsById(id)) {
            return productRepository.save(product);
        }
        throw new ObjectNotFoundException(Product.class.getSimpleName(), id);
    }
}
