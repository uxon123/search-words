package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.file.TextFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
class FileIndex {
    private final Set<String> index;
    @Getter
    private final String name;

    // consider moving parsing
    static FileIndex parse(TextFile textFile) {
        String content = textFile.getStringContent();
        String[] words = content.toLowerCase().split("\\W+");
        Set<String> occurredWords = new HashSet<>(Arrays.asList(words));
        return new FileIndex(occurredWords, textFile.getName());
    }

    boolean containsWord(String word) {
        return index.contains(word);
    }
}
