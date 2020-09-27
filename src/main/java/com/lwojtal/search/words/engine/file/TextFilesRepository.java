package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.util.List;

public interface TextFilesRepository {
    List<TextFile> getFiles(String folderPath) throws PathNotFoundException;
}
