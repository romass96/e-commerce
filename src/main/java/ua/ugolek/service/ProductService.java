package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.ugolek.model.OrderStatus;
import ua.ugolek.model.Product;
import ua.ugolek.model.PropertyDefinition;
import ua.ugolek.projection.ClientProductsByCategories;
import ua.ugolek.projection.ProductSoldProjection;
import ua.ugolek.repository.ProductRepository;
import ua.ugolek.repository.PropertyDefinitionRepository;

import java.util.List;

@Service
public class ProductService extends CRUDService<Product>
{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PropertyDefinitionRepository definitionRepository;

    @Autowired
    private ArchiveService archiveService;

    @Override
    public Product create(Product product) {
        product.getProperties().forEach(property -> {
            PropertyDefinition definition = definitionRepository.save(property.getDefinition());
            property.setDefinition(definition);
        });
        return productRepository.save(product);
    }

    public void archiveProduct(Long productId) {
        Product product = getById(productId);
        archiveService.moveToArchive(product);
    }

    public void unarchiveProduct(Long productId) {
        Product product = getById(productId);
        archiveService.restoreFromArchive(product);
    }

    public void decreaseProductQuantity(Product product, Integer amount) {

    }

    public void increaseProductQuantity(Product product, Integer amount) {

    }

    public List<ProductSoldProjection> countSoldProducts() {
        return productRepository.countProductsByOrderStatus(OrderStatus.COMPLETED);
    }

    @Override
    protected JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    public List<ClientProductsByCategories> countProductsForCategories(Long clientId) {
        return productRepository.countProductsForCategories(clientId);
    }
}
