package com.learning.generics;

import com.google.common.math.LongMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Executor {
    public static void printList(List<?> list) {
        System.out.println("Current size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.print("|" + (i + 1) + ":" + list.get(i));
        }
        System.out.println("|");
    }

    private static long getFactorial(int n) {
        return LongMath.factorial(n);
    }

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        DIYArrayList<Integer> myList = new DIYArrayList<>();

        for (int i = 0; i < 30; i++) {
            myList.add(i % 11);
            list.add(i % 11);
        }
        printList(myList);

        // Add
        myList.add(-2);
        myList.add(23, -1);
        printList(myList);

        // Remove
        myList.remove(23);
        Integer i = -2;
        myList.remove(i);
        printList(myList);

        // Set
        myList.set(0, 9999);
        printList(myList);

        // Sort
        myList.sort();
        printList(myList);

        // Add All
        myList.addAll(25, list);
        printList(myList);

        // Copy
        DIYArrayList<Integer> newMyList = new DIYArrayList<>(myList.size());
        Collections.copy(newMyList, myList);
        printList(newMyList);

        // Static sort
        Collections.sort(newMyList);
        printList(newMyList);

        // Clear
        myList.clear();
        printList(myList);
    }
}
