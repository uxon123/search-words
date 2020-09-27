package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.file.File;
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
    public static FileIndex parse(File file) {
        Set<String> occuredWords = new HashSet<>();
        String content = file.getStringContent();
        List<String> words = Arrays.asList(content.toLowerCase().split("\\W+"));
        for(String word : words) {
            occuredWords.add(word);
        }
        return new FileIndex(occuredWords, file.getName());
    }

    public boolean containsWord(String word) {
        return index.contains(word);
    }
}
