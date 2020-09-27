package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTextFilesRepository implements TextFilesRepository {
    private Map<String, List<TextFile>> folders = new HashMap<>();

    @Override
    public List<TextFile> getFiles(String folderPath) throws PathNotFoundException {
        if (!folders.containsKey(folderPath)) {
            throw new PathNotFoundException(folderPath);
        }
        return folders.get(folderPath);
    }

    public void addFolder(String path, List<TextFile> textFiles){
        folders.put(path, textFiles);
    }
}
