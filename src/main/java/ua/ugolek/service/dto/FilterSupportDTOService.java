package ua.ugolek.service.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.ugolek.dto.DTO;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.FilterSupportDTORepository;

public abstract class FilterSupportDTOService<T, F extends SearchFilter, U extends DTO> {
    private final FilterSupportDTORepository<T, F, U> filterSupportRepository;

    protected FilterSupportDTOService(FilterSupportDTORepository<T, F, U> filterSupportRepository) {
        this.filterSupportRepository = filterSupportRepository;
    }

    public ListResponse<U> queryByFilter(F filter) {
        Pageable pageable = PageRequest.of(filter.getPageNumber() - 1, filter.getPerPage());
        Page<U> page = filterSupportRepository.filter(filter, pageable);
        return generateResponse(page);
    }

    private ListResponse<U> generateResponse(Page<U> page) {
        ListResponse<U> response = new ListResponse<>();
        response.setItems(page.getContent());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }

}
