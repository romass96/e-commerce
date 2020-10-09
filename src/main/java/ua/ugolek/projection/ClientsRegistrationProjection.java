package ua.ugolek.projection;

import java.time.LocalDate;

public interface ClientsRegistrationProjection {
    LocalDate getRegistrationDate();
    Long getClientsCount();
}
