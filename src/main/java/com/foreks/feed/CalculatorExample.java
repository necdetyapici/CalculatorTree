package com.foreks.feed;

import org.gradle.MyTreeSetImpl;

public class CalculatorExample {

    public static void main(final String[] args) {
        final Calculator calc = new Calculator();

        System.out.println(calc.convertPostfixExpression("4-6+6*(3*7+5)"));
        final MyTreeSetImpl<String> tree = calc.createCalculatorTree("4-6+6*(3*7+5)");
        System.out.println(tree.toString());
        System.out.println("" + calc.calculate(tree));
    }
}
