package org.example.services;

import org.example.DaoImplementation.MaturityBucketDaoImplementation;
import org.example.entity.MaturityBucket;

import java.sql.SQLException;
import java.util.Scanner;

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

}
