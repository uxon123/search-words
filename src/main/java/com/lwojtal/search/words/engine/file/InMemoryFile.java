package com.lwojtal.search.words.engine.file;

public class InMemoryFile implements File {
    private String name;
    private String content;

    public InMemoryFile(String name, String content){
        this.name = name;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStringContent() {
        return content;
    }
}
