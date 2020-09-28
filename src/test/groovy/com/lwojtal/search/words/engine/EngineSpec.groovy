package com.lwojtal.search.words.engine


import com.lwojtal.search.words.engine.file.InMemoryTextFile
import com.lwojtal.search.words.engine.file.InMemoryTextFilesRepository
import com.lwojtal.search.words.engine.file.TextFile
import com.lwojtal.search.words.engine.representation.FileRank
import com.lwojtal.search.words.engine.representation.SearchResult
import com.lwojtal.search.words.engine.representation.SetsBasedRepresentation
import com.lwojtal.search.words.engine.representation.TextFilesRepresentation
import spock.lang.Specification

class EngineSpec extends Specification {
    InMemoryTextFilesRepository inMemoryFileSystem = initFileSystem()
    def initFileSystem() {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder("emptyDir", [])
        inMemoryFileSystem.addFolder("dirWithSingleFile", [
                new InMemoryTextFile("file1", "This is simple text file.")
        ])
        return inMemoryFileSystem
    }
    EngineFacade facade = new EngineFacade(new SetsBasedRepresentation(inMemoryFileSystem))

    def "should throw exception if given directory doesn't exist"() {
        given: "path to the directory doesn't exist"
        when: "reading the directory content"
        facade.loadSourceFiles('nonExistingPath')

        then: "throw PathNotFoundException"
        thrown(PathNotFoundException)
    }

    def "should respond with empty result if given directory is empty"() {
        given: "the directory is empty"
        facade.loadSourceFiles('emptyDir')

        when: "searching for sample phrase"
        SearchResult response = facade.search('phrase')

        then: "List of fileRanks should be empty"
        response.getFileRanks().isEmpty()
    }

    def "should return 100% rank for a file which content is identical to search phrase"() {
        given: "directory contains single file"
        String sample = "this is sample text"
        EngineFacade facade = createWithSingleFile("path", "file1",
                sample)
        facade.loadSourceFiles("path")

        when: "searching for a phrase which is identical to the file content"
        SearchResult response = facade.search(sample)

        then: "The rank of the file equals 100%"
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == "file1"
        fileRank.getRankPercents() == 100
    }

    def "should return 0% rank for a file if it doesn't contain any words from the search phrase"() {
        given: "directory contains single file"
        String sample = "this is sample text"
        EngineFacade facade = createWithSingleFile("path", "file1",
                sample)
        facade.loadSourceFiles("path")

        when: "The file contains none of the words of the phrase"
        SearchResult response = facade.search("zxc xcv")

        then: "The rank of the file equals 0%"
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == "file1"
        fileRank.getRankPercents() == 0
    }

    def "should return 50% rank for a file if it contains half of words from search phrase"() {
        given: "directory contains single file"
        String sample = "this is sample text"
        EngineFacade facade = createWithSingleFile("path", "file1",
                sample)
        facade.loadSourceFiles("path")

        when: "searching for a phrase which half of words are in the file content"
        SearchResult response = facade.search("sample text zx zv")

        then: "The rank of the file equals 50%"
        FileRank fileRank = response.getFileRanks().get(0)
        fileRank.getName() == "file1"
        fileRank.getRankPercents() == 50
    }

    def "should return fileRanks sorted from highest to lowest rank"() {
        given: "directory contains many files"
        EngineFacade facade = createWith3Files("path",
                "file1", "aaa",
                "file2", "aaa bbb",
                "file3", "aaa bbb ccc")
        facade.loadSourceFiles("path")

        when: "searching for a phrase"
        SearchResult response = facade.search("aaa bbb ccc")

        then: "The file ranks should be sorted form highest to lowest"
        response.getFileRanks().get(0) >= response.getFileRanks().get(1)
        response.getFileRanks().get(1) >= response.getFileRanks().get(2)
    }

    def "should return no more than N fileRanks"() {
        given: "directory contains many files and Engine is configured to return no more than 2 results"
        EngineFacade facade = createWith3FilesAndLimitOf2("path",
                "file1", "aaa",
                "file2", "aaa bbb",
                "file3", "aaa bbb ccc")
        facade.loadSourceFiles("path")

        when: "searching for a phrase containing words which can be found in all 3 files"
        SearchResult response = facade.search("aaa bbb ccc")

        then: "The number of returned fileRanks is 2"
        response.getFileRanks().size() == 2
    }

    def createWithSingleFile(String path, String fileName, String fileContent) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, [
                new InMemoryTextFile(fileName, fileContent)
        ])
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem)
        return new EngineFacade(representation)
    }

    def createWith2Files(String path, String file1Name, String file1Content, String file2Name, String file2Content) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, [
                new InMemoryTextFile(file1Name, file1Content),
                new InMemoryTextFile(file2Name, file2Content)
        ])
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem)
        return new EngineFacade(representation)
    }

    def createWith3Files(String path, String file1Name, String file1Content, String file2Name, String file2Content,
                         String file3Name, String file3Content) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, [
                new InMemoryTextFile(file1Name, file1Content),
                new InMemoryTextFile(file2Name, file2Content),
                new InMemoryTextFile(file3Name, file3Content)
        ])
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem)
        return new EngineFacade(representation)
    }

    def createWith3FilesAndLimitOf2(String path, String file1Name, String file1Content, String file2Name, String file2Content,
                         String file3Name, String file3Content) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, [
                new InMemoryTextFile(file1Name, file1Content),
                new InMemoryTextFile(file2Name, file2Content),
                new InMemoryTextFile(file3Name, file3Content)
        ])
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem, 2)
        return new EngineFacade(representation)
    }
}
