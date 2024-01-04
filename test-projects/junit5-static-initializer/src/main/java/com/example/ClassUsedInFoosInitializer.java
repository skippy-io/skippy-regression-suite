package com.example;

class ClassUsedInFoosInitializer {

    static String doSomething() {
        return ClassUsedInFoosInitializer.class.getName();
    }
}
