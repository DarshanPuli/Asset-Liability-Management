package org.example.services;

import org.example.DaoImplementation.UserDaoImpl;
import org.example.entity.AssetsHeld;
import org.example.entity.LiabilitiesHeld;
import org.example.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class UserService {

    private UserDaoImpl userDaoImpl = new UserDaoImpl();

    public UserService() throws SQLException {
    }

    public void addUser(Scanner sc) throws SQLException {
        System.out.println("Enter users name : ");
        String name = sc.next();
        System.out.println("Enter users number : ");
        String number = sc.next();
        System.out.println("Enter users credit rate (1-10): ");
        int credit = sc.nextInt();
        User user1 = new User(name, number, credit);
        userDaoImpl.addUser(user1);
    }

    public void purchaseAsset(Scanner sc) throws SQLException {
        System.out.println("Enter userId : ");
        UUID userId =  UUID.fromString(sc.next());
        System.out.println("Enter Asset Id : ");
        UUID assetId = UUID.fromString(sc.next());
        System.out.println("Enter Principal Amount : ");
        long principalAmount = sc.nextLong();
        System.out.println("Enter Date of repayment");
        LocalDate maturityDate = LocalDate.parse(sc.next());
        AssetsHeld assetsHeld = new AssetsHeld(userId,assetId,principalAmount,maturityDate);
        userDaoImpl.purchaseAsset(assetsHeld);
    }

    public void purchaseLiability(Scanner sc) throws SQLException {
        System.out.println("Enter userId : ");
        UUID userId =  UUID.fromString(sc.next());
        System.out.println("Enter Liability Id : ");
        UUID liabilityId = UUID.fromString(sc.next());
        System.out.println("Enter Principal Amount : ");
        long principalAmount = sc.nextLong();
        System.out.println("Enter Date of repayment");
        LocalDate maturityDate = LocalDate.parse(sc.next());
        LiabilitiesHeld liabilitiesHeld = new LiabilitiesHeld(userId,liabilityId,principalAmount,maturityDate);
        userDaoImpl.purchaseLiability(liabilitiesHeld);
    }
}
