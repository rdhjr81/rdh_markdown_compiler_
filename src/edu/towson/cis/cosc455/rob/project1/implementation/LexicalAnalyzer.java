package edu.towson.cis.cosc455.rob.project1.implementation;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer implements edu.towson.cis.cosc455.rob.project1.interfaces.LexicalAnalyzer {

	/** The next character. */
    String nextCharacter = "";

    /** The current position. */
    int currentPosition = 0;
	
    
    
    public int sourceFileIndex;
	public int tokenBinIndex;
	public char currentChar;
	public String lexeme;
	public ArrayList<Tokens> tokenBin;
	public Stack<Tokens> tokenStack;
	public String sourceFile;
	public Tokens currentToken;
	public Dictionary<Token, String> lexemeDictionary;
	
	
	//String for pattern matching
	String wordPatternString = "\\w";	// 	 \\w matches for a word character: [a-zA-Z_0-9]
	String nonWordPatternString = "[,.\":?!]";
	String whitespacePatternString = "\\s"; // \\s matches for whitespace characters: [ \t\n\x0B\f\r]
		
	//patterns for numbers, letters, whitespace
	public static Pattern wordPattern;
	public static Pattern nonWordPattern;
	public static Pattern whitespacePattern;
	
	//matcher
	Matcher matcher;
	
	public LexicalAnalyzer(String sourceFile) {
		//initialize index values
		sourceFileIndex = 0;
		tokenBinIndex = 0;
		//initialize patterns
		wordPattern = Pattern.compile(wordPatternString);
		nonWordPattern= Pattern.compile(nonWordPatternString);
		whitespacePattern = Pattern.compile(whitespacePatternString);
		
		lexeme = "";
		this.sourceFile = sourceFile;
		tokenBin = new ArrayList<Tokens>();
		
	}
	@Override
	public void getNextToken() {
		// TODO Auto-generated method stub
		currentToken = tokenBin.get(tokenBinIndex++);
	}

	@Override
	public void getCharacter() {
		// TODO Auto-generated method stub
		currentChar = sourceFile.charAt(sourceFileIndex++);

	}

	@Override
	public void addCharacter() {
		// TODO Auto-generated method stub
		lexeme+=currentChar;
	}

	@Override
	public boolean isSpace(String c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean lookupToken() {
		// TODO Auto-generated method stub
		return false;
	}
	public void lex(){
		
		
		if(sourceFileIndex == 0){
			getCharacter();
		}
		
		switch(currentChar){
			case '#':
				addCharacter();
				while(matcher = )
				break;
			case '^':
				addCharacter();
				break;
			case '<':
				addCharacter();
				break;
			case '>':
				
				break;
			case '{':
				
				break;
			case '}':
				
				break;
			case '$':
				
				break;
			case '=':
				
				break;
			case '*':
				
				break;
			case '+':
				
				break;
			case ';':
				
				break;
			case '~':
				
				break;
			case '[':
				
				break;
			case ']':
				
				break;
			case '@':
				
				break;
			case '%':
				
				break;
			case '(':
				
				break;
			case ')':
				
				break;
			default:
				
				break;
		}
	}

}
