package com.learning.atm_project;

import com.learning.atm_project.dto.FaceValuesPair;
import com.learning.atm_project.exceptions.AddError;
import com.learning.atm_project.exceptions.IncorrectValue;
import com.learning.atm_project.exceptions.NotEnoughBills;
import com.learning.atm_project.face_value.FaceValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName(value = "Банкомат должен")
class ATMImplTest {
    private ATMImpl atm;

    @BeforeEach
    void prepareATM() {
        atm = new ATMImpl();
        List<FaceValue> faceValues = new ArrayList<>();
        faceValues.addAll(Collections.nCopies(2, FaceValue.FIFE_THOUSAND));
        faceValues.addAll(Collections.nCopies(2, FaceValue.TWO_THOUSAND));
        faceValues.addAll(Collections.nCopies(2, FaceValue.ONE_THOUSAND));
        faceValues.addAll(Collections.nCopies(2, FaceValue.FIVE_HUNDRED));
        faceValues.addAll(Collections.nCopies(2, FaceValue.TWO_HUNDRED));
        faceValues.addAll(Collections.nCopies(2, FaceValue.ONE_HUNDRED));
        atm.addBills(faceValues);
    }

    @Test
    @DisplayName(value = "подсчитывать текущий баланс средств")
    void getCurrentBalance() {
        assertEquals(17600, atm.getCurrentBalance());
    }

    @Test
    @DisplayName(value = "принимать пачку банкнот")
    void addBills() {
        atm.addBills(Arrays.asList(
                FaceValue.TWO_THOUSAND,
                FaceValue.TWO_HUNDRED,
                FaceValue.ONE_HUNDRED,
                FaceValue.ONE_HUNDRED
        ));
        assertEquals(20000, atm.getCurrentBalance());
    }

    @Test
    @DisplayName(value = "выдавать ошибку, если нет места для принятия банкнот")
    void notEnoughSpace() {
        assertThrows(AddError.class, () -> { atm.addBills(Collections.nCopies(99999, FaceValue.FIFE_THOUSAND)); });
    }

    @Test
    @DisplayName(value = "выдавать нужную сумму минимальным количеством банкнот")
    void withdrawBills() {
        List<FaceValue> withdrawFaceValues = atm.withdrawBills(8600);
        List<FaceValue> expectedFaceValues = Arrays.asList(
                FaceValue.FIFE_THOUSAND,
                FaceValue.TWO_THOUSAND,
                FaceValue.ONE_THOUSAND,
                FaceValue.FIVE_HUNDRED,
                FaceValue.ONE_HUNDRED
        );
        assertEquals(expectedFaceValues, withdrawFaceValues);
    }

    @Test
    @DisplayName(value = "выдавать ошибку, если нет нужной суммы для выдачи")
    void notEnoughBills() {
        assertThrows(NotEnoughBills.class, () -> { atm.withdrawBills(17700); });
    }

    @Test
    @DisplayName(value = "выдавать ошибку, если введена сумма, некратная 100")
    void incorrectSum() {
        assertThrows(IncorrectValue.class, () -> { atm.withdrawBills(17550); });
    }

    @Test
    @DisplayName(value = "выдавать количетсво банкнот в кассетах")
    void billsBalance() {
        List<FaceValuesPair> pairs = Arrays.asList(
                new FaceValuesPair(FaceValue.FIFE_THOUSAND, 2),
                new FaceValuesPair(FaceValue.TWO_THOUSAND, 2),
                new FaceValuesPair(FaceValue.ONE_THOUSAND, 2),
                new FaceValuesPair(FaceValue.FIVE_HUNDRED, 2),
                new FaceValuesPair(FaceValue.TWO_HUNDRED, 2),
                new FaceValuesPair(FaceValue.ONE_HUNDRED, 2)
        );
        assertEquals(pairs, atm.billsBalance());
    }
}