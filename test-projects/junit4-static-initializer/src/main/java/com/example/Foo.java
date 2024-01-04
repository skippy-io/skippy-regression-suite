package com.example;

class Foo {

    static String field;

    static {
        field = ClassUsedInFoosInitializer.doSomething();
    }

    static boolean doSomething() {
        return true;
    }
}
