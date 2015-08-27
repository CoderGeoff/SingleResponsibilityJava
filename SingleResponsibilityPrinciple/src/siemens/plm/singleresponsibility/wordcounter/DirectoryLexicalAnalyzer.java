package siemens.plm.singleresponsibility.wordcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DirectoryLexicalAnalyzer {
	private int wordCount = 0;
	private int longestWordLength = 0;
	private String directoryPath;
	private Map<String, Integer> frequency = new HashMap<String, Integer>();
	private boolean isInitialized = false;

	public DirectoryLexicalAnalyzer(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	public int getWordCount() {
		lazyInitialize();
		return wordCount;
	}
	
	public int getLengthOfLongestWord() {
		lazyInitialize();
		return longestWordLength;
	}
	
	public int getFrequency(String word) {
		lazyInitialize();
		Integer frequencyOrNull = frequency.get(word); 
		return frequencyOrNull == null ? 0 : frequencyOrNull.intValue();
	}
	
	private void lazyInitialize() {
		if (isInitialized)
			return;
		
		try {
			Files.walk(Paths.get(directoryPath)).forEach(filePath -> countWordsInFile(filePath));
		} 
		catch (IOException e) {
		}
		
		isInitialized = true;
	}
	
	private void countWordsInFile(Path filePath)
	{
	    if (!Files.isRegularFile(filePath)) 
	    	return;
	    
	    try {
			Files.readAllLines(filePath).forEach(line -> Arrays.stream(line.split(" ")).forEach(word -> count(word)));
		} 
	    catch (IOException e) {
		}
	}
	
	private void count(String word)
	{
		if (word.length() > 0) {
			
			word = getWordWithoutSurroundingPunctuation(word, ",:;!\\[\\]\\{\\}\\(\\)\\'\\\"\t\r\n");
			
			if (word.length() > 0) {
				// we lower case the first letter, unless there are capitals inside the word
				// in which case we assume that it's some kind of abbreviation
				if (word.chars().skip(1).noneMatch(c -> Character.isUpperCase(c)))
					word = Character.toLowerCase(word.charAt(0)) + word.substring(1, word.length());

				wordCount++;
				frequency.put(word, frequency.getOrDefault(word, 0) + 1);
				longestWordLength = Math.max(word.length(), longestWordLength);
			}
		}
	}

	private String getWordWithoutSurroundingPunctuation(String word, String punctuationSymbols) {
		word = word.replaceFirst("^[" + punctuationSymbols + "]*", "");
		word = word.replaceFirst("[" + punctuationSymbols + "]*$", "");
		return word;
	}

}
