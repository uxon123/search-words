package com.lwojtal.search.words.engine;

import com.lwojtal.search.words.engine.representation.Phrase;
import com.lwojtal.search.words.engine.representation.SearchResult;
import com.lwojtal.search.words.engine.representation.TextFilesRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EngineFacade {
    @NonNull
    private TextFilesRepresentation textFilesRepresentation;

    public void loadSourceFiles(String folderPath) throws PathNotFoundException {
        textFilesRepresentation.build(folderPath);
    }

    public SearchResult search(String text) {
        Phrase phrase = Phrase.of(text);
        return textFilesRepresentation.search(phrase);
    }

}
