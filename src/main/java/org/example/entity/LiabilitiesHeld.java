package org.example.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class LiabilitiesHeld {
    private UUID userId;
    private UUID liabilityId;
    private long principalAmount;
    private LocalDate maturityDate;
    private long amountLeftToRepay;
    private LocalDate createdAt;

    public LiabilitiesHeld(){
        this.createdAt = LocalDate.now();
    }
    public LiabilitiesHeld(UUID userId, UUID liabilityId, long principalAmount, LocalDate maturityDate) {
        this();
        this.userId = userId;
        this.liabilityId = liabilityId;
        this.principalAmount = principalAmount;
        this.maturityDate = maturityDate;
        this.amountLeftToRepay = principalAmount;
    }
}
