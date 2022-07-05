
-----SPRING 2022 CS209A ASSIGNMENT 1-----

Release date: Wednesday, March 9, 2022

-----------------------------------------------------------------------------
Junit version: 5.4

Folder Tree
Project:.
        ├─resources
        │     messy_sentences.txt
        │     messy_sentences2.txt
        │     new_concept_english.txt
        │     stop_words_english.txt
        │
        ├─src
        │      Position.java
        │      TextProcessor.java
        │
        ├─test
        │       MethodEntity.java
        │       TextProcessorTest.java
        │
        └readme.txt

-----------------------------------------------------------------------------

Final Test Cases:

Before:
    (0.5 points)
    1. Test if the constructor method is valid.

getCommonWordsTest:
    (1 point)
    1. Meet the basic requirements. Note that a "word" can be thought of as a sequence of letters separated by white spaces and any punctuation.
    2. If multiple words have the same count, these words should be ordered by the natural order of String (lexicographic order).

    (0.5 point)
    1. Distinguish between words with "-", "'" and words without "-", "'".
    2. Count all words with "-", "'" correctly.

highlightWordTest:
    (0.5 point)
    1. Point to the first letter of a word.

    (0.5 point)
    1. Point to the non-first letter of a word.

    (0.5 point)
    1. Point to a non-word.

    (0.5 point)
    1. Point to words with "-", "'".

fixLowercaseFirstLetterTest:
    (0.5 point)
    1. The sentences are divided by ".", "!","?" only.
    2. A sentence will definitely within the same line.

    (0.5 point)
    1. A sentence may not be in the same line (i.e., a long sentence may take several lines).
    2. Each line may start with multiple spaces or \t.

    (0.5 point)
    1. A sentence may be a paragraph wrapped in double quotation marks.
    2. Words may contain "-", "'".

    (0.5 point)
    1. All of the above conditions can occur.
    2. A sentence may start with a number.

encryptSpecialTest:
    (1 point)
    1. Satisfy basic encryption requirements.
    2. The punctuation should not be changed after encryption.
    3. The document contains no numbers; words don't contain special punctuations.

    (0.5 point)
    1. A word may contain numbers or is a number.

    (0.5 point)
    1. All of the above conditions can occur.
    2. Words may contain "-", "'".

ngramSimTest:
    (1 point)
    1. Meet the basic requirements.

    (1 point)
    1. A word may contain numbers or is a number.
    2. Words may contain "-", "'".