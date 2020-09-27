package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.PathNotFoundException;
import com.lwojtal.search.words.engine.file.TextFile;
import com.lwojtal.search.words.engine.file.TextFilesRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class SetsBasedRepresentation implements TextFilesRepresentation {
    @NonNull
    private TextFilesRepository textFilesRepository;
    private List<FileIndex> filesIndexes;

    @Override
    public void build(String folderPath) throws PathNotFoundException {
        List<FileIndex> newFilesIndexes = new ArrayList<>();
        List<TextFile> textFiles = textFilesRepository.getFiles(folderPath);
        for(TextFile textFile : textFiles) {
            newFilesIndexes.add(FileIndex.parse(textFile));
        }

        filesIndexes = newFilesIndexes;
    }

    @Override
    public SearchResult search(Phrase phrase) {
        List<FileRank> fileRanks = new ArrayList<>();
        for(FileIndex fileIndex : filesIndexes) {
            fileRanks.add(getFileRank(phrase, fileIndex));
        }
        if (fileRanks.size() > 1) {
            fileRanks = fileRanks.stream().sorted().collect(toList());
        }
        return SearchResult.create(fileRanks);
    }

    // TODO move this to FileRankCalculator/Strategy
    private FileRank getFileRank(Phrase phrase, FileIndex fileIndex) {
        Set<String> uniqueWords = phrase.getUniqueWords();
        int numberOfMatches = findNumberOfMatches(uniqueWords, fileIndex);
        int matchPercent = 100 * numberOfMatches / uniqueWords.size();
        return FileRank.create(fileIndex.getName(), matchPercent);
    }

    private int findNumberOfMatches(Set<String> uniqueWords, FileIndex fileIndex) {
        int numberOfMatches = 0;
        for(String word : uniqueWords){
            if (fileIndex.containsWord(word)) numberOfMatches++;
        }
        return numberOfMatches;
    }

}
