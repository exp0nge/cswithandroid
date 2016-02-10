package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private static Random random;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        random = new Random();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix.isEmpty())
            return getGoodWordStartingWith(prefix);
        return binarySearch(words, prefix);
    }

    private String binarySearch(ArrayList<String> words, String prefix) {
        int length = words.size();
        String middleWord = words.get(length/2);

        if (middleWord.startsWith(prefix))
            return middleWord;

        if (length == 1)
            return null;

        char[] prefixArray = prefix.toCharArray();
        char[] middleWordArray = middleWord.toCharArray();

        int maxIterations;
        if (middleWordArray.length <= prefixArray.length){
            maxIterations = middleWordArray.length;
        }
        else {
            maxIterations = prefixArray.length;
        }

        for (int i = 0; i < maxIterations; i++) {
            if (middleWordArray[i] != prefixArray[i]){
                if (middleWordArray[i] > prefixArray[i])
                    return binarySearch(new ArrayList<>(words.subList(0, length/2)), prefix);
                else
                    return binarySearch(new ArrayList<>(words.subList(length/2, length)), prefix);
            }
        }

        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String word = words.get(random.nextInt(words.size() - 1));
        return word.substring(0, 4);
    }
}
