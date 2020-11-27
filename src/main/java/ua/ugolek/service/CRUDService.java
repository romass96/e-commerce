package ua.ugolek.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.BaseEntity;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;

public abstract class CRUDService<T extends BaseEntity> {
    private final static int ENTITY_CLASS_INDEX = 0;
    private String objectType;

    @PostConstruct
    public void init() {
        this.objectType = ReflectionUtil.getGenericClass(getClass(), ENTITY_CLASS_INDEX).getSimpleName();
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public T create(@Valid T object) {
        return getRepository().save(object);
    }

    public T update(T object) {
        if (!existsById(object.getId())) {
            throw new ObjectNotFoundException(object.toString() + " is not persisted.");
        }
        return getRepository().save(object);
    }

    public void delete(Long id) {
        getRepository().deleteById(id);
    }

    public void delete(T object) {
        getRepository().delete(object);
    }

    public T getById(Long id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(objectType, id));
    }

    public boolean existsById(Long id) {
        return getRepository().existsById(id);
    }

    protected abstract JpaRepository<T, Long> getRepository();
}
