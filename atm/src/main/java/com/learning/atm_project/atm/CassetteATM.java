package com.learning.atm_project.atm;

import com.learning.atm_project.cassette.CassetteImpl;
import com.learning.atm_project.face_value.FaceValue;
import lombok.Data;

@Data
public class CassetteATM {
    public final CassetteImpl cassette;
    public final FaceValue faceValue;
}