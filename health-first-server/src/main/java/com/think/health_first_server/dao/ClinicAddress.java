package com.think.health_first_server.dao;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ClinicAddress {
    private String street;
    private String city;
    private String state;
    private String zip;
}

