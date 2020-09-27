# Search words
## Overview
This project contains a simple console application for searching words in files.
Application as an input parameter takes path to a folder containing text files.
After starting it takes search phrases from standard input. Then it takes all the words from specified phrase
and search for them files from the provided directory. Based on that application returns a list of File Ranks, 
which consist of file name and percentage rank value. 
The files in the result are sorted by their percentage ranks in descending order (from highest to lowest rank).

### Rank
Currently rank is calculated: 

    100 * (number of words from phase which can be found in the file) / (number of unique words in phrase)
    
## Implementation
The most naive approach would be to just load all files as lists of words and linearly go through them on every search. 
But this is slow for large files. 
To get acceptable requests time we need to do the preprocessing. In first step, we build TextFilesRepresentation. 
It uses indexes in form of Sets of words per file underneath. Then, when search request comes, every word from search 
phrase is checked against each built FileIndex (constant time).

Main motivations behind code structure:
- Provide acceptable response time;
- Decouple business logic from the UI. Thanks to that with very little effort it could be used in
- Allow to unit test on as higher level as possible (component level) - this is main reason for introducing 
TextFile and TextFileRepository abstractions.

### Potential improvements

- TextFile models read whole file into String - that was easier to implement, but should be improved so large files 
(let's say hundreds of MBs) are processed correctly. Some kind of buffered reading or reading by lines should be implemented.
- Input validation should be improved, i.e. inspect if file is a text file (not folder or binary)
- Current search implementation is based on building indexes in form of Sets of words per each file.
This is relatively fast (const time per file), but could be further improved with reversed indexes which points to given files.
This implementation should have good effects if number of file is large.
- Not all required cases are tested, test coverage should be increased, 
some parts of code could be tested more thoroughly (lack of time);
- The engine api should be improved by providing dtos for all requests and responses 
(now internal objects are returned); then some of the response formatting logic could be moved there (to dto creation).
- The input normalization could be improved. For example now things like different forms (plural, 's) 
are handled as separate words. We could use some external 
- There are some inconsistencies with using constructors vs factory method;

## How to run

First, the code must be build:
    
    mvn clean package
    
After that, go to ./target directory and run the jar (don't forget to pass path to directory contianing text files).
For example:

    java -jar search-words-1.0-SNAPSHOT.jar "../src/test/resources/testFiles/elevenFiles"
    


    