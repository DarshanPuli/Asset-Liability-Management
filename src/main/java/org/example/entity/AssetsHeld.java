package org.example.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
public class AssetsHeld {
    private UUID userId;
    private UUID assetId;
    private long principalAmount;
    private LocalDate maturityDate;
    private long amountLeftToRepay;
    private int possibilityOfDefault;
    private LocalDate createdAt;

    public AssetsHeld(){
        this.createdAt = LocalDate.now();
        this.amountLeftToRepay = 0;
        this.possibilityOfDefault = 0;
    }
    public AssetsHeld(UUID userId,UUID assetId,long principalAmount,LocalDate maturityDate) {
        this();
        this.userId = userId;
        this.assetId = assetId;
        this.principalAmount = principalAmount;
        this.maturityDate = maturityDate;
    }
}
