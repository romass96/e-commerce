package ua.ugolek.helpers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.ugolek.model.Client;
import ua.ugolek.service.ClientService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ua.ugolek.utils.Utility.*;

@ExtendWith(SpringExtension.class)
public class ClientHelper {
    @Autowired
    private ClientService clientService;

    public List<Client> createClients(int clientsCount) {
        String[] firstNames = {"Иван", "Олег", "Александр", "Алексей", "Дмитрий", "Роман", "Евгений"};
        String[] lastNames = {"Иванов", "Петров", "Михайлов", "Добров", "Качер", "Бруяк", "Качан"};
        String[] domains = {"@gmail.com", "@mail.ru", "@ukr.net", "@yahoo.com"};
        for (int i = 1; i <= clientsCount; i++) {
            Client client = new Client();

            int domainIndex = ThreadLocalRandom.current().nextInt(0, domains.length);
            client.setEmail(generateString(10) + domains[domainIndex]);

            int firstNameIndex = ThreadLocalRandom.current().nextInt(0, firstNames.length);
            client.setFirstName(firstNames[firstNameIndex]);

            int lastNameIndex = ThreadLocalRandom.current().nextInt(0, lastNames.length);
            client.setLastName(lastNames[lastNameIndex]);

            client.setPhoneNumber("+3809" + generateNumericString(8));
            client.setPassword(generateString(6));

            Date startDate = new Calendar.Builder().setDate(2020,1,1).build().getTime();
            client.setRegistrationDate(generateDate(startDate));

            clientService.create(client);
        }
        return clientService.getAll();
    }
}
