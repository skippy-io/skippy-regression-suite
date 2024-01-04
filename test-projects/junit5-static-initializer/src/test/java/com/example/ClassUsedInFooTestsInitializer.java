package com.example;

class ClassUsedInFooTestsInitializer {

    static String doSomething() {
        return ClassUsedInFooTestsInitializer.class.getName();
    }
}
