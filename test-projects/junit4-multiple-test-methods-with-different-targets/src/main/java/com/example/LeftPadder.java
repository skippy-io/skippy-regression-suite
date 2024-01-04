package com.example;

class LeftPadder {

    static String padLeft(String input, int size) {
        if (input.length() < size) {
            return padLeft(" " + input, size);
        }
        return input;
    }

}
