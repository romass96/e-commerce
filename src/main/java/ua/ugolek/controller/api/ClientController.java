package ua.ugolek.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.ugolek.model.Client;
import ua.ugolek.payload.ListResponse;
import ua.ugolek.payload.filters.SearchFilter;
import ua.ugolek.service.ClientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("")
    public List<Client> getAllClients() {
        return clientService.getAll();
    }

    @GetMapping("/registrationStatistics")
    public Map<LocalDate, Long> getRegistrationStatistics(@RequestParam String period) {
        return clientService.countClientsByRegistrationDate(period);
    }

    @PostMapping("/filter")
    public ListResponse<Client> getClientsByFilter(@RequestBody SearchFilter filter) {
        return clientService.queryByFilter(filter);
    }

}
