package com.learning.atm_project;

import com.learning.atm_project.atm.ATM;
import com.learning.atm_project.atm.CassetteATM;
import com.learning.atm_project.atm.CashMachine;
import com.learning.atm_project.cassette.CassetteImpl;
import com.learning.atm_project.dto.FaceValuesPair;
import com.learning.atm_project.exceptions.AddError;
import com.learning.atm_project.exceptions.NotEnoughBills;
import com.learning.atm_project.exceptions.WithdrawError;
import com.learning.atm_project.face_value.FaceValue;

import java.util.*;
import java.util.stream.Collectors;

public class ATMImpl implements ATM, CashMachine {
    private List<CassetteATM> cassettes;

    public ATMImpl() {
        cassettes = new ArrayList<>();
        List<FaceValue> values = Arrays.asList(FaceValue.values());
        values.sort(Comparator.reverseOrder());
        for (FaceValue faceValue : values) {
            cassettes.add(new CassetteATM(new CassetteImpl(), faceValue));
        }
    }

    public int getCurrentBalance() {
        return cassettes.stream()
                .map(cassette -> (cassette.faceValue.getValue() * cassette.cassette.getBillsCount()))
                .reduce(0, Integer::sum);
    }

    @Override
    public int addBills(List<FaceValue> pack) {
        Map<FaceValue, Long> counters = pack.stream()
                .collect(Collectors.groupingBy(faceValue -> faceValue, Collectors.counting()));
        for (CassetteATM cassette : cassettes) {
            if (counters.containsKey(cassette.faceValue)) {
                if (cassette.cassette.getBillsCount() + counters.get(cassette.faceValue) > CassetteImpl.getCassetteCapacity())
                    throw new AddError();
            }
        }
        int amount = 0;
        int count;
        for (CassetteATM cassette : cassettes) {
            if (counters.containsKey(cassette.faceValue)) {
                count = Math.toIntExact(counters.get(cassette.faceValue));
                amount += count * cassette.faceValue.getValue();
                cassette.cassette.addBills(count);
            }
        }
        return amount;
    }

    private FaceValue getMinimalFaceValue() {
        for (int i = cassettes.size() - 1; i >= 0; i--) {
            if (cassettes.get(i).cassette.getBillsCount() > 0)
                return cassettes.get(i).faceValue;
        }
        throw new NotEnoughBills();
    }

    @Override
    public List<FaceValue> withdrawBills(int amount) {
        FaceValue minimal = getMinimalFaceValue();
        if (amount % minimal.getValue() > 0)
            throw new WithdrawError();
        int remainingAmount = amount;
        List<FaceValue> faceValues = new ArrayList<>();
        for (CassetteATM cassette : cassettes) {
            int value = cassette.faceValue.getValue();
            if (value > remainingAmount) continue;
            int count = cassette.cassette.withdrawBills(
                    Math.min(cassette.cassette.getBillsCount(), (int) remainingAmount / value));
            faceValues.addAll(Collections.nCopies(count, cassette.faceValue));
            remainingAmount -= count * value;
        }
        return faceValues;
    }

    @Override
    public List<FaceValuesPair> balance() {
        return cassettes.stream()
                .map(cassette -> new FaceValuesPair(cassette.faceValue, cassette.cassette.getBillsCount()))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ATMImpl atm = new ATMImpl();
        System.out.println(atm.getCurrentBalance());
        for (FaceValuesPair i : atm.balance()) {
            System.out.println(i.getFaceValue().toString() + " " + i.getCount());
        }
//        for (FaceValue i : atm.withdrawBills(1)) {
//            System.out.println(i.toString());
//        }

        List<FaceValue> faceValues = new ArrayList<>();
        faceValues.addAll(Collections.nCopies(1000, FaceValue.FIFE_THOUSAND));
        faceValues.addAll(Collections.nCopies(1000, FaceValue.TWO_THOUSAND));
        faceValues.addAll(Collections.nCopies(1000, FaceValue.ONE_THOUSAND));
        faceValues.addAll(Collections.nCopies(1000, FaceValue.FIVE_HUNDRED));
        faceValues.addAll(Collections.nCopies(1000, FaceValue.TWO_HUNDRED));
        faceValues.addAll(Collections.nCopies(1000, FaceValue.ONE_HUNDRED));

        System.out.println("Added bills, summ = " + atm.addBills(faceValues));

        System.out.println("ATM balance = " + atm.getCurrentBalance());

        List<FaceValue> faceValues1 = atm.withdrawBills(18900 );
        if (faceValues1 != null) {
            faceValues1.forEach(System.out::println);
            faceValues1.stream()
                    .collect(Collectors.groupingBy(faceValue -> faceValue, Collectors.counting()))
                    .forEach((faceValue, aLong) -> { System.out.println(faceValue + " x " + aLong); } );
        }
    }
}
