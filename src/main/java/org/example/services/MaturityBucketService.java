package org.example.services;

import org.example.DaoImplementation.MaturityBucketDaoImpl;
import org.example.entity.MaturityBucket;

import java.sql.SQLException;
import java.util.Scanner;

public class MaturityBucketService {

    public MaturityBucketDaoImpl maturityBucketDaoImpl = new MaturityBucketDaoImpl();
    public MaturityBucketService() throws SQLException {}

    public void addMaturityBucket(Scanner scanner) throws SQLException {

        System.out.print("Maturity Bucket Start Range (In Months): ");
        int startRange = scanner.nextInt();
        System.out.print("Maturity Bucket End Range (In Months): ");
        int endRange = scanner.nextInt();

        MaturityBucket bucket = new MaturityBucket(startRange, endRange);
        maturityBucketDaoImpl.addMaturityBucket(bucket);
    }

}
