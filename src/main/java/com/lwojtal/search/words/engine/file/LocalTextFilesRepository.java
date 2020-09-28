package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalTextFilesRepository implements TextFilesRepository {

    @Override
    public List<TextFile> getFiles(String folderPath) throws PathNotFoundException {
        List<TextFile> textFiles = new ArrayList<>();
        final File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null) {
            throw new PathNotFoundException(folderPath);
        }

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isFile()) {
                String fileName = fileEntry.getName();
                String absolutePath = fileEntry.getAbsolutePath();
                TextFile textFile = LocalTextFile.create(fileName, absolutePath);
                textFiles.add(textFile);
            }
        }

        return textFiles;
    }
}
