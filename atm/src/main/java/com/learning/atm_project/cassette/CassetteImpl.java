package com.learning.atm_project.cassette;

import com.learning.atm_project.exceptions.CrowdedCassette;
import com.learning.atm_project.exceptions.IncorrectValue;
import com.learning.atm_project.exceptions.NotEnoughBills;

public class CassetteImpl implements Cassette {
    private static final int CASSETTE_CAPACITY = 1111;
    private int billsCount = 0;

    public CassetteImpl() {
        this(0);
    }

    protected CassetteImpl(int initialCount) {
        addBills(initialCount);
    }

    @Override
    public void addBills(int billsCount) {
        if (billsCount < 0) throw new IncorrectValue();
        int newSize = billsCount + this.billsCount;
        if (newSize > CASSETTE_CAPACITY) throw new CrowdedCassette();
        this.billsCount = newSize;
    }

    @Override
    public int withdrawBills(int billsCount) {
        if (billsCount < 0) throw new IncorrectValue();
        int newSize = this.billsCount - billsCount;
        if (newSize < 0) throw new NotEnoughBills();
        this.billsCount = newSize;
        return billsCount;
    }

    @Override
    public int getBillsCount() {
        return this.billsCount;
    }

    public static int getCassetteCapacity() {
        return CASSETTE_CAPACITY;
    }

    public static void main(String[] args) {
        CassetteImpl cassette = new CassetteImpl();
        cassette.addBills(100);
        System.out.println(cassette.getBillsCount());
        cassette.withdrawBills(100);
        System.out.println(cassette.getBillsCount());
    }
}




