package ua.ugolek.repository.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.ugolek.dto.DTO;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.util.ReflectionUtil;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FilterSupportDTORepository<T,F extends SearchFilter, U extends DTO>  {
    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract DTOExtractor<T, F, U> createDtoExtractor(F filter);

    public Page<U> filter(F filter, Pageable pageable) {
        DTOExtractor<T, F, U> dtoExtractor = createDtoExtractor(filter);
        long count = dtoExtractor.queryDTOCount();
        List<U> dtoList = dtoExtractor.queryDTOList();
        return new PageImpl<>(dtoList, pageable, count);
    }

}
