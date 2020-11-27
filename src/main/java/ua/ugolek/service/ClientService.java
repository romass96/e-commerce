package ua.ugolek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ua.ugolek.model.Client;
import ua.ugolek.model.ClientCreationMode;
import ua.ugolek.projection.ClientsRegistrationProjection;
import ua.ugolek.repository.ClientRepository;
import ua.ugolek.util.DateUtils;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Validated
public class ClientService extends CRUDService<Client>
{
    private static final String DEFAULT_PASSWORD = "password";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Client create(@Valid Client client) {
        String encodedPassword = encoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        return clientRepository.save(client);
    }

    public Client registerClient(Client client) {
        client.setClientCreationMode(ClientCreationMode.THROUGH_REGISTRATION);
        return create(client);
    }

    public Client createClientFromOrderDetails(Client client) {
        client.setClientCreationMode(ClientCreationMode.THROUGH_ORDER);
        client.setPassword(DEFAULT_PASSWORD);
        return create(client);
    }

    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public Optional<Client> findByPhoneNumber(String phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    public Map<LocalDate, Long> countClientsByRegistrationDate(String periodCode) {
        Period period = DateUtils.getPeriodByCode(periodCode);
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minus(period);
        List<ClientsRegistrationProjection> items = clientRepository.countClientsByRegistrationDate(startDate);

        CountByDateStatistics<ClientsRegistrationProjection> statistics = new CountByDateStatistics<>();
        statistics.setCountMapper(ClientsRegistrationProjection::getClientsCount);
        statistics.setDateMapper(ClientsRegistrationProjection::getRegistrationDate);
        statistics.setStartDate(startDate.toLocalDate());
        statistics.setEndDate(endDate.toLocalDate());

        return statistics.getEveryDayData(items);
    }

    @Override
    protected JpaRepository<Client, Long> getRepository() {
        return clientRepository;
    }
}
