package com.lwojtal.search.words.engine.file;

import com.lwojtal.search.words.engine.PathNotFoundException;

import java.util.List;

public interface FileSystem {
    List<File> getFiles(String folderPath) throws PathNotFoundException;
}
