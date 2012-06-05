package com.wordsbattle.util;

import java.util.Random;
// Класс для генерации случайных букв.
public class LetterGenerator {
    private static char[] engLetters = {'a','b','c','d',
                                        'e','f','g','h',
                                        'i','j','k','l',
                                        'm','n','o','p',
                                        'q','r','s','t',
                                        'u','v','w','x',
                                        'y','z'};
    
    public static char generateLetter() {
        Random generator = new Random();
        return engLetters[generator.nextInt(26)];
    }
}