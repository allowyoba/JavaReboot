package com.learning.atm_project.atm;

import com.learning.atm_project.face_value.FaceValue;

import java.util.List;

public interface ATM {
    int addBills(List<FaceValue> pack);
    List<FaceValue> withdrawBills(int sum);
}
