package ua.ugolek.service.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ugolek.dto.ClientDTO;
import ua.ugolek.model.Client;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.repository.dto.ClientDTORepository;

@Service
public class ClientDTOService extends FilterSupportDTOService<Client, SearchFilter, ClientDTO> {

    @Autowired
    public ClientDTOService(ClientDTORepository clientDTORepository) {
        super(clientDTORepository);
    }
}
