package com.learning.atm_project.cassette;

import com.learning.atm_project.exceptions.CrowdedCassette;
import com.learning.atm_project.exceptions.IncorrectValue;
import com.learning.atm_project.exceptions.NotEnoughBills;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "Кассета должна")
class CassetteImplTest {
    private Cassette cassette;

    @BeforeEach
    void prepareCassette() {
        cassette = new CassetteImpl(15);
    }

    @Test
    @DisplayName(value = "добавлять положительное количество банкнот")
    void addPositiveBills() {
        cassette.addBills(5);
        assertEquals(20, cassette.getBillsCount());
    }

    @Test
    @DisplayName(value = "добавлять 0 банкнот")
    void addZeroBills() {
        cassette.addBills(0);
        assertEquals(15, cassette.getBillsCount());
    }

    @Test
    @DisplayName(value = "не добавлять отрицательное количество банкнот")
    void addNegativeBills() {
        assertThrows(IncorrectValue.class, () -> { cassette.addBills(-1); });
    }

    @Test
    @DisplayName(value = "не принимать банкноты, если не может их вместить")
    void modelingCrowdedCell() {
        assertThrows(CrowdedCassette.class, () -> { cassette.addBills(CassetteImpl.getCassetteCapacity());} );
    }

    @Test
    @DisplayName(value = "выдавать положительное количество банкнот")
    void withdrawPositiveBills() {
        cassette.withdrawBills(5);
        assertEquals(10, cassette.getBillsCount());
    }

    @Test
    @DisplayName(value = "выдавать 0 банкнот")
    void withdrawZeroBills() {
        cassette.withdrawBills(0);
        assertEquals(15, cassette.getBillsCount());
    }

    @Test
    @DisplayName(value = "не выдавать отрицательное количество банкнот")
    void withdrawNegativeBills() {
        assertThrows(IncorrectValue.class, () -> { cassette.withdrawBills(-1); });
    }

    @Test
    @DisplayName(value = "не выдавать банкнот больше, чем имеется")
    void withdrawMoreBillsThanAvailable() {
        assertThrows(NotEnoughBills.class, () -> { cassette.withdrawBills(16); });
    }

    @Test
    @DisplayName(value = "показывать текущее количество купюр")
    void getBillsCount() {
        assertEquals(15, cassette.getBillsCount());
    }
}