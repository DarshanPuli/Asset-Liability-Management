package org.example.DaoImplementation;

import org.example.Dao.CounterPartyDao;
import org.example.entity.Asset;
import org.example.entity.CounterParty;
import org.example.entity.MaturityBucket;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class CounterPartyDaoImplementation implements CounterPartyDao {

    private final AssetDaoImplementation assetDaoImplementation = new AssetDaoImplementation();
    private final MaturityBucketDaoImplementation maturityBucketDaoImplementation = new MaturityBucketDaoImplementation();

    public CounterPartyDaoImplementation() throws SQLException {}

    @Override
    public CounterParty getCounterParty(UUID counterPartyId) {
        return null;
    }

    public CounterParty addCounterParty(CounterParty counterParty) {
        return null;
    }

    public void addToMaturityBucket(CounterParty counterParty) throws SQLException {
        //get assetId
        UUID assetId = counterParty.getAssetId();
        //get asset
        Asset asset = assetDaoImplementation.getAsset(assetId);

        //get maturity date
        int monthsToExpiry = asset.getMonthsToExpiry();

        LocalDate maturityDate = asset.getCreatedAt().toLocalDateTime().toLocalDate().plusMonths(monthsToExpiry);

        //convert to months
        Period period = Period.between(maturityDate, LocalDate.now());
        int monthsLeftToMaturity = period.getYears() * 12 + period.getMonths();

        //get maturity bucket
        MaturityBucket bucket = maturityBucketDaoImplementation.findMaturityBucketByRange(monthsLeftToMaturity);

        //update maturity bucket object
        bucket.setTotalAssetsValue(bucket.getTotalAssetsValue() + counterParty.getPrincipalAmount());
        bucket.setNetGap(bucket.getNetGap() + counterParty.getPrincipalAmount());

        //update maturity bucket in db
        maturityBucketDaoImplementation.updateMaturityBucket(bucket);
    }

    @Override
    public CounterParty updateCounterParty(CounterParty counterParty) {
        return null;
    }

    @Override
    public CounterParty deleteCounterParty(UUID counterPartyId) {
        return null;
    }

    @Override
    public int updateCount(UUID assetId, UUID counterPartyId) {
        return 0;
    }
}
