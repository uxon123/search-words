package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.file.TextFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class FileIndex {
    private final Set<String> index;
    @Getter
    private final String name;

    // consider moving parsing
    public static FileIndex parse(TextFile textFile) {
        Set<String> occuredWords = new HashSet<>();
        String content = textFile.getStringContent();
        List<String> words = Arrays.asList(content.toLowerCase().split("\\W+"));
        for(String word : words) {
            occuredWords.add(word);
        }
        return new FileIndex(occuredWords, textFile.getName());
    }

    public boolean containsWord(String word) {
        return index.contains(word);
    }
}
