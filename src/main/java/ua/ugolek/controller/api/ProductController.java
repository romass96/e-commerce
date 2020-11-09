package ua.ugolek.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.Product;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.projection.ProductSoldProjection;
import ua.ugolek.service.ProductService;
import ua.ugolek.service.PropertyService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping("")
    public Product add(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("/{id}")
    public Product update(@RequestBody Product product, @PathVariable Long id) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @GetMapping("")
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping("/filter")
    public ListResponse<Product> getProductsByFilter(@RequestBody SearchFilter filter) {
        return productService.queryByFilter(filter);
    }

    @GetMapping("/productSoldStatistics")
    public List<ProductSoldProjection> getSoldProductsStatistics() {
        return productService.countSoldProducts();
    }
}
