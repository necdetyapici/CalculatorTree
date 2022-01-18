package com.foreks.feed;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.gradle.MyTreeSetImpl;

public class Calculator {

    public MyTreeSetImpl<String> createCalculatorTree(final String s) {
        final Stack<MyTreeSetImpl<String>> stack = new Stack<MyTreeSetImpl<String>>();
        MyTreeSetImpl<String> leftChild = null;
        MyTreeSetImpl<String> rightChild = null;
        MyTreeSetImpl<String> nodeValue = null;
        final String postfix = convertPostfixExpression(s);
        char c;
        for (int i = 0; i < postfix.length(); i++) {
            c = postfix.charAt(i);
            if (isOperator("" + c)) {
                nodeValue = new MyTreeSetImpl<String>("inorder", Comparator.comparing(String::toString));
                nodeValue.setRoot(true);
                nodeValue.setValue("" + c);
                rightChild = stack.pop();
                leftChild = stack.pop();
                nodeValue.setLeftChild(leftChild);
                nodeValue.setRightChild(rightChild);
                stack.push(nodeValue);
            } else {
                nodeValue = new MyTreeSetImpl<String>("inorder", Comparator.comparing(String::toString));
                nodeValue.setRoot(true);
                nodeValue.setValue("" + c);
                stack.add(nodeValue);
            }
        }
        return stack.pop();
    }

    public String convertPostfixExpression(final String infixExpression) {
        char c;
        final Stack<String> operatorStack = new Stack<String>();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infixExpression.length(); i++) {
            c = infixExpression.charAt(i);
            if ("(".equals("" + c)) {
                operatorStack.push("" + c);
            } else if (")".equals("" + c)) {
                while (operatorStack.peek().equals("(") != true) {
                    sb.append(operatorStack.pop());
                }
                if (operatorStack.peek().equals("(")) {
                    operatorStack.pop();
                }
            } else if (isOperator("" + c)) {
                if (operatorStack.isEmpty() == true) {
                    operatorStack.push("" + c);
                } else {
                    if (ICP("" + c) < ISP(operatorStack.peek())) {
                        sb.append(operatorStack.pop());
                        operatorStack.push("" + c);
                    } else {
                        operatorStack.push("" + c);
                    }
                }
            } else {
                sb.append("" + c);
            }
        }
        while (operatorStack.isEmpty() != true) {
            sb.append(operatorStack.pop());
        }
        return sb.toString();
    }

    public int ISP(final String token) {
        int precedence = 0;
        if (token.equals("+") || token.equals("-")) {
            precedence = 2;
        } else if (token.equals("*") || token.equals("/")) {
            precedence = 4;
        } else if (token.equals("^")) {
            precedence = 5;
        } else if (token.equals("(")) {
            precedence = 0;
        }
        return precedence;
    }

    public int ICP(final String token) {
        int precedence = 0;
        if (token.equals("+") || token.equals("-")) {
            precedence = 1;
        } else if (token.equals("*") || token.equals("/")) {
            precedence = 3;
        } else if (token.equals("^")) {
            precedence = 6;
        }
        return precedence;
    }

    private boolean isOperator(final String token) {
        return (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^"));
    }

    Map<String, Operator> operatorMap = new HashMap<String, Operator>() {

                                          private static final long serialVersionUID = 1L;

                                          {
                                              put("+",
                                                  (final MyTreeSetImpl<String> tree) -> {
                                                      return Calculator.this.calculate((MyTreeSetImpl<String>) tree.getLeftChild())
                                                             + Calculator.this.calculate((MyTreeSetImpl<String>) tree.getRightChild());
                                                  });
                                              put("-",
                                                  (final MyTreeSetImpl<String> tree) -> {
                                                      return Calculator.this.calculate((MyTreeSetImpl<String>) tree.getLeftChild())
                                                             - Calculator.this.calculate((MyTreeSetImpl<String>) tree.getRightChild());
                                                  });
                                              put("^", (final MyTreeSetImpl<String> tree) -> {
                                                  return Math.pow(Calculator.this.calculate((MyTreeSetImpl<String>) tree.getLeftChild()),
                                                                  Calculator.this.calculate((MyTreeSetImpl<String>) tree.getRightChild()));
                                              });
                                              put("*",
                                                  (final MyTreeSetImpl<String> tree) -> {
                                                      return Calculator.this.calculate((MyTreeSetImpl<String>) tree.getLeftChild())
                                                             * Calculator.this.calculate((MyTreeSetImpl<String>) tree.getRightChild());
                                                  });
                                              put("/",
                                                  (final MyTreeSetImpl<String> tree) -> {
                                                      return Calculator.this.calculate((MyTreeSetImpl<String>) tree.getLeftChild())
                                                             / Calculator.this.calculate((MyTreeSetImpl<String>) tree.getRightChild());
                                                  });
                                          }
                                      };

    public double calculate(final MyTreeSetImpl<String> tree) {
        double sonuc = 0;
        if (tree.getLeftChild() == null && tree.getRightChild() == null) {
            return Integer.parseInt(tree.getValue());
        }
        final Operator op = this.operatorMap.get(tree.getValue());
        sonuc = op.calculate(tree);
        return sonuc;
    }
}
