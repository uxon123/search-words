package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.PathNotFoundException;

public interface TextFilesRepresentation {
    void build(String folderPath) throws PathNotFoundException;
    SearchResult search(Phrase phrase);
}
