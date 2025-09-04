package org.example.entity;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class MaturityBucket {
    private UUID bucketID;
    private int startRange;
    private int endRange;
    private double totalAssetsValue;
    private double totalLiabilitiesValue;

    public MaturityBucket(){
        this.bucketID = UUID.randomUUID();
        this.totalAssetsValue = 0;
        this.totalLiabilitiesValue = 0;
    }

    public MaturityBucket(int startRange, int endRange){
        this();
        this.startRange = startRange;
        this.endRange = endRange;
    }
}

