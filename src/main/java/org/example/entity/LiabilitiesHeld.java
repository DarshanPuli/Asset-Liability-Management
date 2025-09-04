package org.example.entity;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class LiabilitiesHeld {
    private UUID userId;
    private UUID liabilityId;
    private long principalAmount;
    private Date maturityDate;
    private long amountLeftToRepay;
    private Date createdAt;

    public LiabilitiesHeld(){
        this.createdAt = new Date();
    }
    public LiabilitiesHeld(UUID userId, UUID liabilityId, long principalAmount, Date maturityDate, long amountLeftToRepay) {
        this();
        this.userId = userId;
        this.liabilityId = liabilityId;
        this.principalAmount = principalAmount;
        this.maturityDate = maturityDate;
        this.amountLeftToRepay = amountLeftToRepay;
    }
}
