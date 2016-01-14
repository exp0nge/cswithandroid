package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    protected static HashSet<String> wordSet;
    protected static HashMap<Integer, ArrayList> sizeToWords;
    protected static HashMap<String, ArrayList> lettersToWord;
    public static final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        wordSet = new HashSet<>();
        sizeToWords = new HashMap<>();
        lettersToWord = new HashMap<>();
        String line;
        ArrayList<String> wordMapList;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            if (sizeToWords.containsKey(word.length())){
                wordMapList = sizeToWords.get(word.length());
                wordMapList.add(word);
                sizeToWords.put(word.length(), wordMapList);
            }
            else{
                ArrayList<String> newWordList = new ArrayList<>();
                newWordList.add(word);
                sizeToWords.put(word.length(), newWordList);
            }
            if (!lettersToWord.containsKey(sortWord(word))){
                ArrayList<String> newArrayList = new ArrayList<>();
                newArrayList.add(word);
                lettersToWord.put(sortWord(word), newArrayList);
            }

        }
    }

    private String sortWord(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char letter : alphabet) {
            if (lettersToWord.containsKey(sortWord(word + letter))){
                ArrayList<String> anagramList = lettersToWord.get(sortWord(word + letter));
                for (String anotherWord : anagramList){
                    if (!anotherWord.contains(word))
                        result.add(anotherWord);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        while (wordLength < MAX_WORD_LENGTH) {
            ArrayList<String> words = sizeToWords.get(wordLength);
            for (int i = random.nextInt(words.size()); i < (words.size() - i); i++) {
                if (getAnagramsWithOneMoreLetter(words.get(i)).size() >= MIN_NUM_ANAGRAMS){
                    wordLength = DEFAULT_WORD_LENGTH;
                    return words.get(i);
                }
            }
            wordLength++;
        }
        return "equaled";
    }
}
