package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFileSystem implements FileSystem {
    private Map<String, List<File>> folders = new HashMap<>();

    @Override
    public List<File> getFiles(String folderPath) throws PathNotFoundException {
        if (!folders.containsKey(folderPath)) {
            throw new PathNotFoundException(folderPath);
        }
        return folders.get(folderPath);
    }

    public void addFolder(String path, List<File> files){
        folders.put(path, files);
    }
}
