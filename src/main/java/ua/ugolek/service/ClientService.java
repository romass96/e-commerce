package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ugolek.model.Client;
import ua.ugolek.projection.ClientsRegistrationProjection;
import ua.ugolek.repository.ClientRepository;
import ua.ugolek.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ClientService extends CrudService<Client> {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Client create(Client client) {
        String encodedPassword = encoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        return clientRepository.save(client);
    }

    public Map<LocalDate, Long> countClientsByRegistrationDate(String periodCode) {
        Period period = DateUtils.getPeriodByCode(periodCode);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(period);
        List<ClientsRegistrationProjection> statistics = clientRepository.countClientsByRegistrationDate(startDate);
        Map<LocalDate, Long> map = statistics.stream().collect(Collectors.toMap(
                ClientsRegistrationProjection::getRegistrationDate,
                ClientsRegistrationProjection::getClientsCount, (prev, next) -> next, TreeMap::new));
        startDate.toLocalDate().datesUntil(endDate.toLocalDate()).forEach(date -> map.putIfAbsent(date, 0L));

        return map;
    }

    @Override
    protected JpaRepository<Client, Long> getRepository() {
        return clientRepository;
    }
}
