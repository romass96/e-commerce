package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.Category;
import ua.ugolek.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Category.class.getSimpleName(), id));
    }

    public Category update(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            return categoryRepository.save(category);
        }
        throw new ObjectNotFoundException(Category.class.getSimpleName(), id);
    }

}
