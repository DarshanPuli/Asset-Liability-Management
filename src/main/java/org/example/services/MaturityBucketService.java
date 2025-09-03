package org.example.services;

import org.example.DaoImplementation.MaturityBucketDaoImplementation;
import org.example.entity.MaturityBucket;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class MaturityBucketService {

    public MaturityBucketDaoImplementation mbdi = new MaturityBucketDaoImplementation();

    public MaturityBucketService() throws SQLException {}

    public void addMaturityBucket(Scanner scanner) throws SQLException {
        String bucketName, description;
        int startRange, endRange;
        
        System.out.println("Please enter the following Details");
        System.out.print("Maturity Bucket Name: ");
        bucketName = scanner.nextLine();
        System.out.println();
        System.out.print("Maturity Bucket Description: ");
        description = scanner.nextLine();
        System.out.println();
        System.out.print("Maturity Bucket Start Range (In Months): ");
        startRange = scanner.nextInt();
        System.out.println();
        System.out.print("Maturity Bucket End Range (In Months): ");
        endRange = scanner.nextInt();

        MaturityBucket bucket = new MaturityBucket(bucketName, startRange, endRange, description);

        mbdi.addMaturityBucket(bucket);
    }

    public void updateMaturityBucket(Scanner scanner) throws SQLException {
        double totalAssetsValue, netGap, totalLiabilitiesValue;
        String bucketId;
        System.out.println("Please enter the following Details");
        System.out.print("Please enter the bucket id: ");
        bucketId = scanner.nextLine();
        System.out.print("Please enter the Total Assets Value : ");
        totalAssetsValue = scanner.nextDouble();
        System.out.print("Please enter the Total Liabilities Value : ");
        totalLiabilitiesValue = scanner.nextDouble();
        System.out.print("Please enter the Net Gain Value : ");
        netGap = scanner.nextDouble();

        MaturityBucket maturityBucket = new MaturityBucket();
        maturityBucket.setTotalAssetsValue(totalAssetsValue);
        maturityBucket.setTotalLiabilitiesValue(totalLiabilitiesValue);
        maturityBucket.setNetGap(netGap);
        maturityBucket.setBucketID(UUID.fromString(bucketId));

        mbdi.updateMaturityBucket(maturityBucket);
    }

    public void getMaturityBucket(Scanner scanner) throws SQLException {
        System.out.println("Below are the all Maturity Buckets");
        mbdi.getMaturityBuckets();
    }

    public void findMaturityBucketByRange(Scanner scanner) throws SQLException {
        int range;
        System.out.println("Please enter the following Details");
        System.out.println("Please Enter The Month Range");
        range = scanner.nextInt();

        MaturityBucket bucket = mbdi.findMaturityBucketByRange(range);

        System.out.println(bucket);
    }

    public void deleteMaturityBucket(Scanner scanner) throws SQLException {
        String uuid;
        System.out.println("Please enter the Maturity Bucket ID");
        uuid = scanner.nextLine();
        mbdi.deleteMaturityBucket(uuid);
    }



}
