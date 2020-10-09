package ua.ugolek.helpers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ugolek.model.Category;
import ua.ugolek.model.Product;
import ua.ugolek.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ua.ugolek.utils.Utility.generateString;

@ExtendWith(SpringExtension.class)
public class ProductHelper {

    @Autowired
    private ProductService productService;

    public Product createProduct(Category category) {
        Product product = new Product();
        product.setName(generateString(15));
        product.setDescription(generateString(250));
        product.setCategory(category);
        product.setPrice(BigDecimal.valueOf(getRandomDouble(1.0, 35598.50)));
        product.setQuantity(ThreadLocalRandom.current().nextInt(0, 1500));

        return productService.add(product);
    }

    public List<Product> createProducts(int productsCount, Category category) {
        for (int i = 1; i <= productsCount; i++) {
            Product product = new Product();
            product.setName(generateString(15));
            product.setDescription(generateString(250));
            product.setCategory(category);
            product.setPrice(BigDecimal.valueOf(getRandomDouble(1.0, 35598.50)));
            product.setQuantity(ThreadLocalRandom.current().nextInt(0, 1500));

            productService.add(product);
        }
        return productService.getAll();
    }

    public int getRandomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    public long getRandomLong(int start, int end) {
        return ThreadLocalRandom.current().nextLong(start, end);
    }

    public double getRandomDouble(double start, double end) {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }
}
