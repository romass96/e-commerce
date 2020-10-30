package ua.ugolek.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.FilterSupportRepository;

public abstract class FilterSupportService<T, F extends SearchFilter> extends CrudService<T> {
    private final FilterSupportRepository<T, F> filterSupportRepository;

    public FilterSupportService(FilterSupportRepository<T, F> filterSupportRepository) {
        this.filterSupportRepository = filterSupportRepository;
    }

    public ListResponse<T> queryByFilter(F filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, filter.getPerPage());
        Page<T> page = filterSupportRepository.filter(filter, pageable);

        ListResponse<T> response = new ListResponse<>();
        response.setItems(page.getContent());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
