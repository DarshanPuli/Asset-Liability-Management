package Entity;
import java.math.BigDecimal;
import java.sql.Date;

public class BucketGap {

    private int gapID;
    private int bucketID;
    private BigDecimal totalAssetsValue;
    private BigDecimal totalLiabilitiesValue;
    private BigDecimal netGap;
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

    public BigDecimal getTotalAssetsValue() {
        return totalAssetsValue;
    }

    public void setTotalAssetsValue(BigDecimal totalAssetsValue) {
        this.totalAssetsValue = totalAssetsValue;
    }

    public BigDecimal getTotalLiabilitiesValue() {
        return totalLiabilitiesValue;
    }

    public void setTotalLiabilitiesValue(BigDecimal totalLiabilitiesValue) {
        this.totalLiabilitiesValue = totalLiabilitiesValue;
    }

    public BigDecimal getNetGap() {
        return netGap;
    }

    public void setNetGap(BigDecimal netGap) {
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

