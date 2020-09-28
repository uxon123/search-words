package com.lwojtal.search.words

import com.lwojtal.search.words.engine.EngineFacade
import com.lwojtal.search.words.engine.PathNotFoundException
import com.lwojtal.search.words.engine.file.LocalTextFilesRepository
import com.lwojtal.search.words.engine.representation.FileRank
import com.lwojtal.search.words.engine.representation.SearchResult
import com.lwojtal.search.words.engine.representation.SetsBasedRepresentation
import spock.lang.Specification

class SearchWordsSpec extends Specification {
    String smallSingleFileDir = "src/test/resources/testFiles/smallSingleFile"
    String smallSingleFileName = "sample.txt"
    String largeSingleFileDir = "src/test/resources/testFiles/largeSingleFile"
    String largeSingleFileName = "sample.txt"
    String rootDir = "src/test/resources/testFiles"

    EngineFacade facade = new EngineFacade(new SetsBasedRepresentation(new LocalTextFilesRepository()))

    def "should throw exception if given directory doesn't exist"() {
        given: "path to the directory doesn't exist"
        when: "reading the directory content"
        facade.loadSourceFiles("x")
        then: "throw PathNotFoundException"
        thrown(PathNotFoundException)
    }

    def "should respond with empty result if given directory doesn't have any files"() {
        given: "the directory has only directories"
        facade.loadSourceFiles(rootDir)

        when: "searching for sample phrase"
        SearchResult response = facade.search('phrase')

        then: "List of fileRanks should be empty"
        response.getFileRanks().isEmpty()
    }

    def "should return 100% rank for a file if it contains all words from search phrase"() {
        given: "directory contains single file"
        facade.loadSourceFiles(smallSingleFileDir)

        when: "searching for a phrase which all words are in the file content"
        SearchResult response = facade.search("would you add")

        then: "The rank of the file equals 100%"
        !response.getFileRanks().isEmpty()
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == smallSingleFileName
        fileRank.getRankPercents() == 100
    }

    def "should return 0% rank for a file if it doesn't contain any words from the search phrase"() {
        given: "directory contains single file"
        facade.loadSourceFiles(smallSingleFileDir)

        when: "The file contains none of the words of the phrase"
        SearchResult response = facade.search("zxc xcv")

        then: "The rank of the file equals 0%"
        !response.getFileRanks().isEmpty()
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == smallSingleFileName
        fileRank.getRankPercents() == 0
    }

    def "should return 50% rank for a file if it contains half of words from search phrase"() {
        given: "directory contains single file"
        facade.loadSourceFiles(largeSingleFileDir)

        when: "searching for a phrase which half of words are in the file content"
        SearchResult response = facade.search("Bibendum est zx zv")

        then: "The rank of the file equals 50%"
        !response.getFileRanks().isEmpty()
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == largeSingleFileName
        fileRank.getRankPercents() == 50
    }

}