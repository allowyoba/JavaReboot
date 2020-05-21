package com.learning.atm_project.dto;

import com.learning.atm_project.face_value.FaceValue;
import lombok.Data;

@Data
public class FaceValuesPair {
    private final FaceValue faceValue;
    private final int count;
}
