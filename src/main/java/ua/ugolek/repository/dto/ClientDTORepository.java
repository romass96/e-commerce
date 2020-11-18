package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ClientDTO;
import ua.ugolek.model.Client;
import ua.ugolek.model.Order;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.ClientDTOExtractor;
import ua.ugolek.repository.dto.extractors.DTOExtractor;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ua.ugolek.dto.ClientDTO.*;

@Repository
public class ClientDTORepository extends FilterSupportDTORepository<Client, SearchFilter, ClientDTO> {

    @Override
    protected DTOExtractor<Client, SearchFilter, ClientDTO> createDtoExtractor(SearchFilter filter)
    {
        return new ClientDTOExtractor(filter, entityManager);
    }

}
