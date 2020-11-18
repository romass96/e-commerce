package ua.ugolek.repository.dto;

import org.springframework.stereotype.Repository;
import ua.ugolek.dto.ClientDTO;
import ua.ugolek.model.Client;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.extractors.ClientDTOExtractor;
import ua.ugolek.repository.dto.extractors.DTOExtractor;

@Repository
public class ClientDTORepository extends FilterSupportDTORepository<Client, SearchFilter, ClientDTO> {

    @Override
    protected DTOExtractor<Client, SearchFilter, ClientDTO> createDtoExtractor(SearchFilter filter) {
        return new ClientDTOExtractor(filter, entityManager);
    }

}
