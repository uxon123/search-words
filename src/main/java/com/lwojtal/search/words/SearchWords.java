package com.lwojtal.search.words;

import com.lwojtal.search.words.engine.EngineFacade;
import com.lwojtal.search.words.engine.PathNotFoundException;
import com.lwojtal.search.words.engine.file.LocalTextFilesRepository;
import com.lwojtal.search.words.engine.representation.FileRank;
import com.lwojtal.search.words.engine.representation.SearchResult;
import com.lwojtal.search.words.engine.representation.SetsBasedRepresentation;

import java.util.Scanner;

public class SearchWords
{
    public static void main( String[] args )
    {
        if (args.length < 1){
            throw new IllegalArgumentException("Path to directory is required");
        }

        EngineFacade engineFacade = new EngineFacade(new SetsBasedRepresentation(new LocalTextFilesRepository()));

        try {
            engineFacade.loadSourceFiles(args[0]);
        } catch (PathNotFoundException e) {
            System.out.println("Provided directory doesn't exist: " + e.getMessage());
            System.exit(1);
        }

        Scanner keys = new Scanner(System.in);
        System.out.println("Type ':quit' to leave.");
        while(true){
            System.out.print("search>");
            final String line = keys.nextLine();
            if (line.equals(":quit")){
                break;
            }
            if (!line.isEmpty()){
                printResult(engineFacade.search(line));
            }
        }

        System.out.println( "Exit" );
    }

    private static void printResult(SearchResult result){
        if (result.getFileRanks().isEmpty()){
            System.out.println("No results found");
            return;
        }
        for (FileRank rank : result.getFileRanks()){
            System.out.println(rank.getName() + ": " + rank.getRankPercents() + "%");
        }
    }
}
