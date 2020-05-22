package com.learning.atm_project.atm;

import com.learning.atm_project.dto.FaceValuesPair;

import java.util.List;

public interface CashMachine {
    List<FaceValuesPair> billsBalance();
}
