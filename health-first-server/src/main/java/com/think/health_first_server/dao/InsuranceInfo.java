package com.think.health_first_server.dao;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class InsuranceInfo {
    private String provider;
    private String policyNumber;
}

