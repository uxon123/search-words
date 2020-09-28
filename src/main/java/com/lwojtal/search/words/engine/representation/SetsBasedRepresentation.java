package com.lwojtal.search.words.engine.representation;

import com.lwojtal.search.words.engine.PathNotFoundException;
import com.lwojtal.search.words.engine.file.TextFilesRepository;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class SetsBasedRepresentation implements TextFilesRepresentation {
    private static int DEFAULT_RESULT_MAX_SIZE = 10;

    @NonNull
    private TextFilesRepository textFilesRepository;
    private List<FileIndex> filesIndexes;
    private int resultCountLimit;

    public SetsBasedRepresentation(@NonNull TextFilesRepository textFilesRepository, int resultCountLimit) {
        this.textFilesRepository = textFilesRepository;
        this.resultCountLimit = resultCountLimit;
    }

    public SetsBasedRepresentation(@NonNull TextFilesRepository textFilesRepository) {
        this(textFilesRepository, DEFAULT_RESULT_MAX_SIZE);
    }

    @Override
    public void build(String folderPath) throws PathNotFoundException {
        filesIndexes = textFilesRepository.getFiles(folderPath).stream()
                .map(x-> FileIndex.parse(x))
                .collect(Collectors.toList());
    }

    @Override
    public SearchResult search(Phrase phrase) {
        List<FileRank> fileRanks = filesIndexes.stream()
                .map(fileIndex -> getFileRank(phrase, fileIndex))
                .collect(toList());

        if (fileRanks.size() > 1) {
            fileRanks = fileRanks.stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(resultCountLimit)
                    .filter(rank -> rank.getRankPercents() > 0)
                    .collect(toList());
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
        return (int) uniqueWords.stream().filter(fileIndex::containsWord).count();
    }

}
