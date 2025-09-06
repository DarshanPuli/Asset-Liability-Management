package org.example.model;

// src/com/alm/model/Deposit.java

import java.time.LocalDate;

public class Deposit extends Liability {
    private String depositor;
    private boolean isWithdrawable;

    public Deposit(String id, String name, double principalAmount, double interestRate,
                   LocalDate maturityDate, String currency, boolean isFixedRate,
                   String depositor, boolean isWithdrawable) {
        super(id, name, principalAmount, interestRate, maturityDate, currency, isFixedRate, "Deposit");
        this.depositor = depositor;
        this.isWithdrawable = isWithdrawable;
    }

    public String getDepositor() { return depositor; }
    public boolean isWithdrawable() { return isWithdrawable; }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Depositor: %s, Withdrawable: %b]", depositor, isWithdrawable);
    }
}
