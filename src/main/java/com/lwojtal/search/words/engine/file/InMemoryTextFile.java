package com.lwojtal.search.words.engine.file;

public class InMemoryTextFile implements TextFile {
    private String name;
    private String content;

    public InMemoryTextFile(String name, String content){
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
