package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalTextFilesRepository implements TextFilesRepository {

    @Override
    public List<TextFile> getFiles(String folderPath) throws PathNotFoundException {
        List<TextFile> textFiles = new ArrayList<>();
        final File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new PathNotFoundException(folderPath);
        }

        for (final File fileEntry : folder.listFiles()) {
            String fileName = fileEntry.getName();
            String absolutePath = fileEntry.getAbsolutePath();
            TextFile textFile = LocalTextFile.create(fileName, absolutePath);
            textFiles.add(textFile);
        }

        return textFiles;
    }
}
