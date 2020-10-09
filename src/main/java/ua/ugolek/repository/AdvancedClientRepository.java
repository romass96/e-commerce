package ua.ugolek.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class AdvancedClientRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Map<Date, Long> countClientsByRegistrationDate() {
        Stream<Tuple> stream = (Stream<Tuple>) entityManager.createNativeQuery(
                "SELECT DATE(c.registrationDate) as registrationDate, COUNT(c) as clientsCount " +
                        "FROM clients c GROUP BY regDate", Tuple.class);

        return stream.collect(Collectors.toMap(tuple -> (Date)tuple.get("registrationDate"),
                        tuple -> (Long)tuple.get("clientsCount")));
    }

    @Data
    @NoArgsConstructor
    private static class Result {
        private Date registrationDate;
        private Long clientsCount;
    }

}
