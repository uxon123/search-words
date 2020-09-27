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
        inMemoryFileSystem.addFolder("emptyDir", new ArrayList<TextFile>(Arrays.asList()))
        inMemoryFileSystem.addFolder("dirWithSingleFile", new ArrayList<TextFile>(Arrays.asList(
                new InMemoryTextFile("file1", "This is simple text file.")
        )))
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

    def createWithSingleFile(String path, String fileName, String fileContent) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, new ArrayList<TextFile>(Arrays.asList(
                new InMemoryTextFile(fileName, fileContent)
        )))
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem)
        return new EngineFacade(representation)
    }

    def createWith2Files(String path, String file1Name, String file1Content, String file2Name, String file2Content) {
        InMemoryTextFilesRepository inMemoryFileSystem = new InMemoryTextFilesRepository()
        inMemoryFileSystem.addFolder(path, new ArrayList<TextFile>(Arrays.asList(
                new InMemoryTextFile(file1Name, file1Content),
                new InMemoryTextFile(file2Name, file2Content)
        )))
        TextFilesRepresentation representation = new SetsBasedRepresentation(inMemoryFileSystem)
        return new EngineFacade(representation)
    }
}
