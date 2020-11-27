package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.Category;
import ua.ugolek.repository.CategoryRepository;

@Service
public class CategoryService extends CRUDService<Category>
{

    @Autowired
    private CategoryRepository categoryRepository;

    public Category update(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            return categoryRepository.save(category);
        }
        throw new ObjectNotFoundException(Category.class.getSimpleName(), id);
    }

    @Override
    protected JpaRepository<Category, Long> getRepository() {
        return categoryRepository;
    }

}
