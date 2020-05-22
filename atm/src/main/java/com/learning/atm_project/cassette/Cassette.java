package com.learning.atm_project.cassette;

import com.learning.atm_project.exceptions.CrowdedCassette;
import com.learning.atm_project.exceptions.NotEnoughBills;

public interface Cassette {
    void addBills(int count) throws CrowdedCassette;
    int withdrawBills(int count) throws NotEnoughBills;
    int getBillsCount();
}
