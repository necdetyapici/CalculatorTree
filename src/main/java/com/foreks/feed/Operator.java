package com.foreks.feed;

import org.gradle.MyTreeSetImpl;

public interface Operator {
    public double calculate(MyTreeSetImpl<String> tree);
}
