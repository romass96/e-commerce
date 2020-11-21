package ua.ugolek.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO implements DTO {
    public static final String ORDERS_COUNT_ALIAS = "ordersCount";
    public static final String FIRST_NAME_FIELD = "firstName";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String EMAIL_FIELD = "email";
    public static final String PHONE_NUMBER_FIELD = "phoneNumber";
    public static final String ID_FIELD = "id";

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long ordersCount;
}
