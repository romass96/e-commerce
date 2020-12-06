package ua.ugolek.service;

import org.springframework.transaction.annotation.Transactional;
import ua.ugolek.exception.ObjectNotFoundException;
import ua.ugolek.model.BaseEntity;
import ua.ugolek.repository.BaseEntityRepository;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.function.Consumer;

public abstract class CRUDService<T extends BaseEntity> {
    private final static int ENTITY_CLASS_INDEX = 0;
    private String objectType;

    private BaseEntityRepository<T> baseEntityRepository;

    public CRUDService(BaseEntityRepository<T> baseEntityRepository) {
        this.baseEntityRepository = baseEntityRepository;
    }

    @PostConstruct
    public void init() {
        this.objectType = ReflectionUtil.getGenericClass(getClass(), ENTITY_CLASS_INDEX).getSimpleName();
    }

    public List<T> getAll() {
        return baseEntityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void processAllEntities(Consumer<T> consumer) {
        baseEntityRepository.findAllAsStream().forEach(consumer);
    }

    public long countAll() {
        return baseEntityRepository.count();
    }

    public T create(@Valid T object) {
        return baseEntityRepository.save(object);
    }

    public T update(T object) {
        if (!existsById(object.getId())) {
            throw new ObjectNotFoundException(object.toString() + " is not persisted.");
        }
        return baseEntityRepository.save(object);
    }

    public void delete(Long id) {
        baseEntityRepository.deleteById(id);
    }

    public void delete(T object) {
        baseEntityRepository.delete(object);
    }

    public T getById(Long id) {
        return baseEntityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(objectType, id));
    }

    public boolean existsById(Long id) {
        return baseEntityRepository.existsById(id);
    }

}
