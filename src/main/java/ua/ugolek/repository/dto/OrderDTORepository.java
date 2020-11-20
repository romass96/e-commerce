package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.OrderDTO;
import ua.ugolek.dto.mappers.OrderDTOMapper;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.OrderFilter;
import ua.ugolek.repository.dto.extractors.DTOExtractor;
import ua.ugolek.repository.dto.extractors.OrderDTOExtractor;

@Repository
public class OrderDTORepository extends FilterSupportDTORepository<Order, OrderFilter, OrderDTO> {

    private final OrderDTOMapper dtoMapper = new OrderDTOMapper();

    @Override
    protected DTOExtractor<Order, OrderFilter, OrderDTO> createDtoExtractor(OrderFilter filter) {
        return new OrderDTOExtractor(filter, entityManager, dtoMapper);
    }
}
