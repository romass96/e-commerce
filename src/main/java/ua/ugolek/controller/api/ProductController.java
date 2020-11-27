package ua.ugolek.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.dto.ProductDTO;
import ua.ugolek.model.Product;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.ProductFilter;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.projection.ClientProductsByCategories;
import ua.ugolek.projection.ProductSoldProjection;
import ua.ugolek.service.ProductService;
import ua.ugolek.service.PropertyService;
import ua.ugolek.service.dto.ProductDTOService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDTOService productDTOService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping("")
    public Product add(@RequestBody Product product) {
        return productService.create(product);
    }

    @PutMapping("")
    public Product update(@RequestBody Product product) {
        return productService.update(product);
    }

    @PostMapping("/archive")
    public HttpStatus archive(@RequestParam Long id) {
        productService.archiveProduct(id);
        return HttpStatus.OK;
    }

    @PostMapping("/unarchive")
    public HttpStatus unarchive(@RequestParam Long id) {
        productService.unarchiveProduct(id);
        return HttpStatus.OK;
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
    public ListResponse<ProductDTO> getProductsByFilter(@RequestBody ProductFilter filter) {
        return productDTOService.queryByFilter(filter);
    }

    @GetMapping("/clientProductsForCategories")
    public List<ClientProductsByCategories> getProductStatisticsForClient(@RequestParam Long clientId) {
        return productService.countProductsForCategories(clientId);
    }

    @GetMapping("/productSoldStatistics")
    public List<ProductSoldProjection> getSoldProductsStatistics() {
        return productService.countSoldProducts();
    }
}
