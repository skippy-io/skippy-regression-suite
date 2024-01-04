package com.example;

class RightPadder {

    static String padRight(String input, int size) {
        if (input.length() < size) {
            return padRight(input + " ", size);
        }
        return input;
    }
}
