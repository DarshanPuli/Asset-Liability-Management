package org.example.DaoImplementation;

import org.example.Dao.CounterPartyDao;
import org.example.entity.Asset;
import org.example.entity.CounterParty;
import org.example.entity.MaturityBucket;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class CounterPartyDaoImplementation implements CounterPartyDao {
    public CounterPartyDaoImplementation() throws SQLException {
    }

    @Override
    public CounterParty getCounterParty(UUID counterPartyId) {
        return null;
    }

    private AssetDaoImplementation assetDaoImplementation = new AssetDaoImplementation();
    private final MaturityBucketDaoImplementation maturityBucketDaoImplementation = new MaturityBucketDaoImplementation();

    public CounterParty addCounterParty(CounterParty counterParty) {
        return null;
    }


    public void addToMaturityBucket(CounterParty counterParty) throws SQLException {
        //get assetId
        UUID assetId = counterParty.getAssetId();

        //get asset
        Asset asset = assetDaoImplementation.getAsset(assetId);

        //get maturity date
        LocalDate maturityDate = assetDaoImplementation.getMaturityDate(assetId);

        //convert to months
        Period period = Period.between(maturityDate, LocalDate.now());
        int monthsLeftToMaturity = period.getYears()*12+period.getMonths();

        //get maturity bucket
        MaturityBucket bucket = maturityBucketDaoImplementation.findMaturityBucket(monthsLeftToMaturity);

        //update maturity bucket object
        bucket.setTotalAssetsValue(bucket.getTotalAssetsValue()+asset.getprincipalAmount());
        bucket.setNetGap(bucket.getNetGap()+asset.getprincipalAmount());

        //update maturity bucket in db
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
