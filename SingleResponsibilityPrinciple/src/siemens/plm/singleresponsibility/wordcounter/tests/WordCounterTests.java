package siemens.plm.singleresponsibility.wordcounter.tests;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import siemens.plm.singleresponsibility.wordcounter.DirectoryLexicalAnalyzer;

public class WordCounterTests {
	
	@Test
	public void givenAnEmptyDirectory_WhenWordsAreCounted_ShouldBeZero() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(0, counter.getWordCount());
	}
	
	@Test
	public void givenAnEmptyDirectory_WhenLongestWordIsQueried_LengthShouldBeZero() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(0, counter.getLengthOfLongestWord());
	}
	
	@Test
	public void givenAnEmptyDirectory_WhenWordFrequencyIsQueried_ShouldBeZero() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(0, counter.getFrequency("the"));
	}
	
	@Test
	public void givenOneFileWith5Words_WhenWordsAreCounted_ShouldBe5() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one two three four five");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(5, counter.getWordCount());
	}
	
	@Test
	public void givenOneFileWith10LetterWordWords_WhenLongestWordIsQueried_ShouldBe10() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one two vegetarian three four five");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(10, counter.getLengthOfLongestWord());
	}
	
	@Test
	public void givenOneFileWith3RepeatedWords_WhenFrequencyIsQueried_ShouldBe3() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one two two three three three four");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(3, counter.getFrequency("three"));
	}
	
	@Test
	public void givenOneWordWithDifferentFirstLetterCapitalization_WhenFrequencyIsQueried_CountShouldBeReportedAgainstLowerCaseWord() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one One");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(2, counter.getFrequency("one"));
	}
	
	@Test
	public void givenOneWordWithDifferentCapitalization_WhenFrequencyIsQueried_ShouldBeCountedAsDifferentWords() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
		TemporaryFile file = new TemporaryFile(directory.getName(), "who WHO");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(1, counter.getFrequency("who"));
	}
	
	@Test
	public void givenAWordInDoubleQuotes_WhenFrequencyIsQueried_ShouldBeCountedWithTheUnquotedWord() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one \"one\"");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(2, counter.getFrequency("one"));
	}
	
	@Test
	public void givenAWordInSingleQuotes_WhenFrequencyIsQueried_ShouldBeCountedWithTheUnquotedWord() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "one 'one'");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(2, counter.getFrequency("one"));
	}
	
	@Test
	public void givenAWordInDoubleQuotes_WhenWordLengthIsQueried_ShouldIgnoreTheQuotes() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "\"one\"");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(3, counter.getLengthOfLongestWord());
	}
	
	@Test
	public void givenWordsWithApostrophes_WhenQueried_CountShouldCountThemAsIndependentWords() throws IOException
	{
	    // Given
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "ones one one's");
	
	    // When
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    StringBuilder s = new StringBuilder();
	    appendFrequency(s, "ones", counter.getFrequency("ones"));
	    appendFrequency(s, "one", counter.getFrequency("one"));
	    appendFrequency(s, "one's", counter.getFrequency("one's"));
	    String result = s.toString();
	
	    // Then
	    assertEquals("ones: 1, one: 1, one's: 1", result);
	}
	
	@Test
	public void givenAWordEndingInPunctuation_WhenWordLengthIsQueried_ShouldIgnorePunctuation() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "Hello world!!!");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(5, counter.getLengthOfLongestWord());
	}
	
	@Test
	public void givenPunctuationSurroundedByWhitespace_WhenWordCountIsQueried_ShouldIgnoreWordsConsistingOnlyOfPunctuation() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "Hello , world !!!");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(2, counter.getWordCount());
	}
	
	@Test
	public void givenOneFileWith10WordsOnMultipleLines_WhenWordsAreCounted_ShouldBe10() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file = new TemporaryFile(directory.getName(), "\r\none two three fours five six\r\n \r\nseven eight nine \r\n\r\nten\r\n");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(10, counter.getWordCount());
	}
	
	@Test
	public void givenTwoFilesWith5WordsEach_WhenWordsAreCounted_ShouldBe10() throws IOException
	{
	    TemporaryDirectory directory = new TemporaryDirectory();
	    @SuppressWarnings("unused")
	    TemporaryFile file1 = new TemporaryFile(directory.getName(), "one two three four five");
	    @SuppressWarnings("unused")
	    TemporaryFile file2 = new TemporaryFile(directory.getName(), "one two three four five");
	    DirectoryLexicalAnalyzer counter = new DirectoryLexicalAnalyzer(directory.getName());
	    assertEquals(10, counter.getWordCount());
	}
		
	private void appendFrequency(StringBuilder s, String word, int frequency) {
	    s.append(word);
	    s.append(": ");
	    s.append(frequency);
	    s.append(", ");

	}
}
