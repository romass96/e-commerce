package ua.ugolek.repository;

import org.springframework.stereotype.Repository;
import ua.ugolek.model.Client;

@Repository
public class AdvancedClientRepository extends StringSearchRepository<Client> {
    public AdvancedClientRepository() {
        super(new String[] {"firstName", "lastName", "email", "phoneNumber"});
    }

}
