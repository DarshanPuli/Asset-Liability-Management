package Entity;
import java.math.BigDecimal;
import java.sql.Date;

public class BucketGap {

    private int gapID;
    private int bucketID;
    private long totalAssetsValue;
    private long totalLiabilitiesValue;
    private long netGap;
    private Date calculationDate;
    private String gapType;

    // Getters and Setters

    public int getGapID() {
        return gapID;
    }

    public void setGapID(int gapID) {
        this.gapID = gapID;
    }

    public int getBucketID() {
        return bucketID;
    }

    public void setBucketID(int bucketID) {
        this.bucketID = bucketID;
    }

    public long getTotalAssetsValue() {
        return totalAssetsValue;
    }

    public void setTotalAssetsValue(long totalAssetsValue) {
        this.totalAssetsValue = totalAssetsValue;
    }

    public long getTotalLiabilitiesValue() {
        return totalLiabilitiesValue;
    }

    public void setTotalLiabilitiesValue(long totalLiabilitiesValue) {
        this.totalLiabilitiesValue = totalLiabilitiesValue;
    }

    public long getNetGap() {
        return netGap;
    }

    public void setNetGap(long netGap) {
        this.netGap = netGap;
    }

    public Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getGapType() {
        return gapType;
    }

    public void setGapType(String gapType) {
        this.gapType = gapType;
    }
}

