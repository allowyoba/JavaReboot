package com.learning.atm_project.face_value;

public enum FaceValue {
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    TWO_THOUSAND(2000),
    FIFE_THOUSAND(5000);

    private int value;

    FaceValue(int value) { this.value = value; }

    public int getValue() { return value; }

    public static FaceValue getByValue(int value) {
        for (FaceValue faceValue: values()) {
            if (faceValue.value == value) return faceValue;
        }
        return null;
    }
}
