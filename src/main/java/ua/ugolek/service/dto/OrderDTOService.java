package ua.ugolek.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.dto.OrderDTO;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.OrderFilter;
import ua.ugolek.repository.dto.OrderDTORepository;

@Service
public class OrderDTOService extends FilterSupportDTOService<Order, OrderFilter, OrderDTO> {

    @Autowired
    public OrderDTOService(OrderDTORepository orderDTORepository) {
        super(orderDTORepository);
    }
}
