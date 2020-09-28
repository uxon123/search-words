package com.lwojtal.search.words.engine.representation;

import lombok.Getter;

import java.util.List;

public class SearchResult {
    @Getter
    private final List<FileRank> fileRanks;

    private SearchResult(List<FileRank> fileRanks) {
        this.fileRanks = fileRanks;
    }

    static SearchResult create(List<FileRank> fileRanks) {
        return new SearchResult(fileRanks);
    }

}
