package com.lwojtal.search.words.engine.file;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

@AllArgsConstructor
public class LocalTextFile implements TextFile{
    private String name;
    private String path;

    static LocalTextFile create(String name, String path){
        return new LocalTextFile(name, path);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStringContent() {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("There was problem reading a file: " + e.getMessage());
        }
        return "";
    }
}
