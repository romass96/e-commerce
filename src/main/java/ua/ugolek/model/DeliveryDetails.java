package ua.ugolek.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

//@Entity
//@Data
//@NoArgsConstructor
public class DeliveryDetails {
    private String receiverFirstName;
    private String receiverLastName;
    private String phoneNumber;

}
