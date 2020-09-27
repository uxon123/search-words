package com.lwojtal.search.words.engine.representation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FileRank {
    @Getter
    private String name;
    @Getter
    private int rankPercents;

    public static FileRank create(String name, int rankPercents){
        return new FileRank(name, rankPercents);
    }

}
