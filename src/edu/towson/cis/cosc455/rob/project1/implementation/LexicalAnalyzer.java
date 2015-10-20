package edu.towson.cis.cosc455.rob.project1.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MarkdownTokenDefinitions.TokenDefinitions;
import MarkdownTokenDefinitions.TokenDefinitions.Type;
import edu.towson.cis.cosc455.rob.project1.implementation.Token;


public class LexicalAnalyzer implements edu.towson.cis.cosc455.rob.project1.interfaces.LexicalAnalyzer {

	 /** The next character. */
    String nextCharacter = "";

    /** The current position. */
    int currentPosition;
    
    public String lexeme;
    
    public String sourceFile;
    
    public boolean unrecognizedToken; 
    
    /** The current index of the TokenBin. */
    int tokenBinIndex = 0;
    
    
    public Token currenttoken;
    
    public HashMap<String, Type> markdownTokenHashmap;//use a hashmap instead?
    
    public ArrayList<String> definitionList; //stores string definitions of each token
    
  //String for pattern matching
  	String wordPatternString = "\\w";	// 	 \\w matches for a word character: [a-zA-Z_0-9]
  	String nonWordPatternString = "[,.\":?!]";
  	String whitespacePatternString = "\\s"; // \\s matches for whitespace characters: [ \t\n\x0B\f\r]
  	
  //patterns for single numbers, letters and whitespace symbols
  	public  Pattern wordPattern;
  	public 	Pattern nonWordPattern;
  	public  Pattern whitespacePattern;
    
  	public  Matcher matcher;
 
    
    public LexicalAnalyzer(String sourceFile) {
    	
    	unrecognizedToken = false;
    	
		//initialize index values
    	currentPosition = 0;
		//initialize patterns
    	whitespacePattern = compilePattern(whitespacePattern, whitespacePatternString);
    	wordPattern = compilePattern(wordPattern, wordPatternString);
    	nonWordPattern = compilePattern(nonWordPattern, nonWordPatternString);
    	
    	//debug
    	System.out.println("patterns compiled");
		System.out.println(sourceFile);
    	//use local reference for hashMap
    	markdownTokenHashmap = TokenDefinitions.getMarkdownHashmap();
    	
    	
    	//populate token hash map
    	TokenDefinitions.populatemarkdownTokenHashmap();
		
		
		lexeme = "";
		
		//use local reference for sourceFile
		this.sourceFile = sourceFile;
		
		//test pattern
		whitespacePattern = Pattern.compile(whitespacePatternString);
		matcher = whitespacePattern.matcher( " ");
		boolean test = matcher.matches();
		if(test) System.out.println("success");
		
		
	}
    
   	/**
   	 * This is the public method to be called when the Syntax Analyzer needs a new
   	 * token to be parsed.
   	 * @throws CompilerException 
   	 */
	public void getNextToken(){
		//at start of file no character exists in 'nextCharacter' so initialize it to first character
				if(currentPosition == 0){	
					getCharacter();
					
					//test
					System.out.println("-initialized. first Char is " + nextCharacter + " - Index of Compiler is " + currentPosition);
					
					
				}
				
				if(isSpace(nextCharacter)){
					
					//test
					System.out.println("Char is " + nextCharacter + " - Index of Compiler is " + currentPosition);
					
					addCharacter();
					getCharacter();
					while(isSpace(nextCharacter)){ //continue capturing whitespace characters
						addCharacter();
						getCharacter();
					}
					//we have a whitespace token
					currenttoken = new Token(Type.WHITESPACE, lexeme);
					
				}
				else if(isWord(nextCharacter) || isNonWord(nextCharacter)){
					addCharacter();
					getCharacter();
					while(isWord(nextCharacter) || isNonWord(nextCharacter)){ //continue capturing whitespace characters
						addCharacter();
						getCharacter();
					}
					//we have a text token
					currenttoken = new Token(Type.TEXT, lexeme);
				}
				else{
					addCharacter();
					getCharacter();
					while(isWord(nextCharacter) || isNonWord(nextCharacter)){ //continue capturing whitespace characters
						addCharacter();
						getCharacter();
					}
					System.out.println("Potential Reserved word is " + lexeme + " - Index of Compiler is " + currentPosition);
					if(!lookupToken()){
						unrecognizedToken = true;
					}
						
				}
				//reset variable 'lexeme' to blank
				lexeme = new String();
				
				if(currentPosition == sourceFile.length()){
					Compiler.setEndOfFile(true);
				}
	}
	
	/**
	 * This is method gets the next character from the input and places it in
	 * the nextCharacter class variable.
	 *
	 * @return the character
	 */
	public void getCharacter(){
		if(currentPosition < sourceFile.length()){
			nextCharacter = sourceFile.substring(currentPosition, currentPosition + 1);
			currentPosition++;
		}
		
	}

	 /**
     * This method adds the current character the nextToken.
     */
	public void addCharacter(){
		lexeme+= nextCharacter;
	}

	/**
	 * This is method gets the next character from the input and places it in
	 * the nextCharacter class variable.
	 *
	 * @param c the current character
	 * @return true, if is space; otherwise false
	 */
	public boolean isSpace(String c){
		
		if(whitespacePattern == null){
			System.out.println("null whitespace");
		}
		if(matcher == null){
			System.out.println("null matcher");
		}
		matcher = whitespacePattern.matcher(c);
		return matcher.matches();
	}
	
	public boolean isWord(String c){
		matcher = wordPattern.matcher(c);
		return matcher.matches();
	}
	
	public boolean isNonWord(String c){
		matcher = nonWordPattern.matcher(c);
		return matcher.matches();
	}
	public void addLetterUntilWhiteSpace(){
		getCharacter();
		while(isWord(nextCharacter)){
			addCharacter();
			getCharacter();
		}
		
	}

	/**
	 * This method checks to see if the current, possible token is legal in the
	 * defined grammar.
	 *
	 * @return true, if it is a legal token, otherwise false
	 */
	public boolean lookupToken(){ //if it is a legal token why not add it to token bin right now?
		for(String x : markdownTokenHashmap.keySet()){
			if(lexeme.equalsIgnoreCase(x)){
				currenttoken = new Token(markdownTokenHashmap.get(x)); //how to know what type of token to add? 
				return true;
			}
				
		}
		return false;
	}
	
	
	
	public Pattern compilePattern(Pattern p, String s){
		p = Pattern.compile(s);		
		//test 
		System.out.println(s + "pattern was compiled");
		
		return p;
	}

	public boolean isUnrecognizedToken() {
		return unrecognizedToken;
	}

	public String getLexeme() {
		return lexeme;
	}
}
	
