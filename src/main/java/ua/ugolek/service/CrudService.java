package ua.ugolek.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import java.util.List;

public abstract class CrudService<T> {
    private String objectType;

    @PostConstruct
    public void init() {
        this.objectType = ReflectionUtil.getGenericClass(getClass()).getSimpleName();
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public T create(T object) {
        return getRepository().save(object);
    }

    public T update(T object) {
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

    protected abstract JpaRepository<T, Long> getRepository();
}
