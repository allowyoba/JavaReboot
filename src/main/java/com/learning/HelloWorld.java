package com.learning;

import com.google.common.math.LongMath;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println("=============");
        System.out.println(getFactorial(52));
    }

    private static long getFactorial(int n) {
        return LongMath.factorial(n);
    }
}
