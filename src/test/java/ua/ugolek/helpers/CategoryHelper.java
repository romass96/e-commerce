package ua.ugolek.helpers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ugolek.model.Category;
import ua.ugolek.service.CategoryService;

import java.util.List;

import static ua.ugolek.utils.Utility.generateString;

@ExtendWith(SpringExtension.class)
public class CategoryHelper {

    @Autowired
    private CategoryService categoryService;

    public List<Category> createCategories(int categoriesCount) {
        for (int i = 1; i <= categoriesCount; i++) {
            Category category = new Category();
            category.setName(generateString(10));
            categoryService.add(category);
        }
        return  categoryService.getAll();
    }
}
