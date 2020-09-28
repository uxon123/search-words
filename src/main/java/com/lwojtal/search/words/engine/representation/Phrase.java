package com.lwojtal.search.words.engine.representation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Phrase {
    private final List<String> words;

    private Phrase(List<String> words) {
        this.words = words;
    }

    public static Phrase of(String input) {
        List<String> words = Arrays.asList(input.toLowerCase().split("\\W+"));
        return new Phrase(words);
    }

    Set<String> getUniqueWords(){
        return new HashSet<>(words);
    }
}
