package ua.ugolek.repository.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.ugolek.dto.DTO;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class FilterSupportDTORepository<T, F extends SearchFilter, U extends DTO> {
    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract DTOExtractor<T, F, U> createDtoExtractor(F filter);

    public Page<U> doFilter(F filter, Pageable pageable) {
        DTOExtractor<T, F, U> dtoExtractor = createDtoExtractor(filter);
        long count = dtoExtractor.queryDTOCount();
        List<U> dtoList = dtoExtractor.queryDTOList();
        return new PageImpl<>(dtoList, pageable, count);
    }

}
