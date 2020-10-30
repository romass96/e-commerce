package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.Category;
import ua.ugolek.service.CategoryService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public Category add(@RequestBody Category category) {
        Optional.ofNullable(category.getParentCategory())
                .map(Category::getId)
                .map(id -> categoryService.getById(id))
                .ifPresent(category::setParentCategory);
        return categoryService.create(category);
    }

    @PutMapping("/{id}")
    public Category update(@RequestBody Category category, @PathVariable Long id) {
        return categoryService.update(id, category);
    }

    @DeleteMapping("/{id}")
    public List<Category> delete(@PathVariable Long id, @RequestParam Boolean returnAll) {
        categoryService.delete(id);
        if (returnAll)  {
            return categoryService.getAll();
        }
        return Collections.emptyList();
    }

    @GetMapping("")
    public List<Category> getAllCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }
}
