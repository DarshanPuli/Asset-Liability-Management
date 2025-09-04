package org.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class Liability {
    private final UUID liabilityId;
    private String liabilityName;
    private double interestRate;
    private String rateType;
    private LocalDate repricingDate;

    public Liability(){
        this.liabilityId = UUID.randomUUID();
    }

    public Liability(String liabilityName, double interestRate, String rateType, LocalDate repricingDate){
        this();
        this.liabilityName = liabilityName;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.repricingDate = repricingDate;
    }
}
