package com.hzy.guava;

import static com.google.common.base.Preconditions.checkArgument;

public class PreconditionsTest {
    public static void main(String[] args) {
        checkArgumentTest();
    }

    public static void checkArgumentTest(){
        int i=0;
        checkArgument(i!=0,"分母不能等于0");
        int j = 9/i;
    }
}
