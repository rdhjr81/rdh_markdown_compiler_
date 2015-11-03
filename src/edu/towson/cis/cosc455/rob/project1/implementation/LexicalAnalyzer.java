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
    public boolean verboseOutput;
    
    
    /** The current index of the TokenBin. */
    int tokenBinIndex = 0;
    
    
    public Token currenttoken;
    
    public HashMap<String, Type> markdownTokenHashmap;//use a hashmap instead?
    
    public ArrayList<String> definitionList; //stores string definitions of each token
    
    public static ArrayList<Token> tokenBin;
    
  //String for pattern matching
  	String wordPatternString = "\\w";	// 	 \\w matches for a word character: [a-zA-Z_0-9]
  	String nonWordPatternString = "[,.\":?!\u000C/]";
  	String whitespacePatternString = "[\\s\\e]"; // \\s matches for whitespace characters: [ \t\n\x0B\f\r]
  	
  //patterns for single numbers, letters and whitespace symbols
  	public  Pattern wordPattern;
  	public 	Pattern nonWordPattern;
  	public  Pattern whitespacePattern;
    
  	public  Matcher matcher;
 
    
    public LexicalAnalyzer(String sourceFile) {
    	
    	unrecognizedToken = true;
    	verboseOutput = Compiler.verboseOutput;
		//initialize index values
    	currentPosition = 0;
		//initialize patterns
    	whitespacePattern = compilePattern(whitespacePattern, whitespacePatternString);
    	wordPattern = compilePattern(wordPattern, wordPatternString);
    	nonWordPattern = compilePattern(nonWordPattern, nonWordPatternString);
    	
    	//debug
    	if(verboseOutput)System.out.println("patterns compiled");
    	if(verboseOutput)System.out.println(sourceFile);
    	//use local reference for hashMap
    	markdownTokenHashmap = TokenDefinitions.getMarkdownHashmap();
    	
    	
    	//populate token hash map
    	TokenDefinitions.populatemarkdownTokenHashmap();
		
    	tokenBin = Compiler.tokenBin;
		lexeme = "";
		
		//use local reference for sourceFile
		this.sourceFile = sourceFile;
		
		//test pattern
		whitespacePattern = Pattern.compile(whitespacePatternString);
		matcher = whitespacePattern.matcher( " ");
		boolean test = matcher.matches();
		if(verboseOutput)if(test) System.out.println("success");
		
		
	}
    
   	/**
   	 * This is the public method to be called when the Syntax Analyzer needs a new
   	 * token to be parsed.
   	 * @throws CompilerException 
   	 */
	public void getNextToken(){
		//at start of file no character exists in 'nextCharacter' so initialize it to first character
		String alternativeLexeme = "";
		
		unrecognizedToken = true;
		
		if(currentPosition == 0){	
			getCharacter();
			
			//test
			if(verboseOutput)System.out.println("-initialized. first Char is " + nextCharacter + " - Index of Compiler is " + currentPosition);
			
			
		}
		
		if(currentPosition == sourceFile.length()){
			Compiler.setEndOfFile(true);
		}
		
		
		if(isSpace(nextCharacter)){
			
			//test
			//System.out.println("Char is " + nextCharacter + " - Index of Compiler is " + currentPosition);
			if(verboseOutput)System.out.println("\t Test for WhiteSpace: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
			
			addCharacter();
			getCharacter();
			while(isSpace(nextCharacter) && !endOfFile()){ //continue capturing whitespace characters
				
				if(verboseOutput)System.out.println("\t Test for WhiteSpace: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
				
				addCharacter();
				getCharacter();
				
				if(currentPosition == sourceFile.length()){
					Compiler.setEndOfFile(true);
				}
			}
			//we have a whitespace token
			unrecognizedToken = false;
			if(verboseOutput)System.out.println("WHITESPACE token : " + lexeme);
			currenttoken = new Token(Type.WHITESPACE, lexeme);
			tokenBin.add(currenttoken);
			
		}
		else if(isWord(nextCharacter) || isNonWord(nextCharacter)){
			
			if(verboseOutput)System.out.println("\t Test for Word: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
			
			addCharacter();
			getCharacter();
			while(isWord(nextCharacter) || isNonWord(nextCharacter)){ //continue capturing word and non-word characters
				if(verboseOutput)System.out.println("\t Test for Word: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
				addCharacter();
				getCharacter();
			}
			//we have a text token
			unrecognizedToken = false;
			if(verboseOutput)System.out.println("TEXT token : " + lexeme);
			currenttoken = new Token(Type.TEXT, lexeme);
			tokenBin.add(currenttoken);
		}
		else{//not a letter or whitespace, must be a reserved symbol OR the start of a reserved symbol
			addCharacter();	//add current character to lexeme string
			getCharacter(); //get next character and place it in nextCharacter
			if(isSpace(nextCharacter)){
				if(verboseOutput)System.out.println("getNextToken Single Reserved Character followed by Space: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
			}
			else if(isWord(nextCharacter)){	//how to handle #BEGIN and [http]
				
				alternativeLexeme = lexeme; //store candidate 
				if(!lookupToken()){
					while(isWord(nextCharacter) && currentPosition < sourceFile.length()){
						if(verboseOutput)System.out.println("getNextToken Single Reserved Character followed by Letter: lexeme=" + lexeme + " nextCharacter=" +nextCharacter);
						addCharacter();
						getCharacter();
					}
				}
				
			}
			else{
				addCharacter();
				getCharacter();
				if(verboseOutput)System.out.println("getNextToken initial lexeme: " + lexeme);
				if(!lookupToken()){
					if(verboseOutput)System.out.println("getNextToken 2nd lexeme: " + lexeme);
					while(!(isWord(nextCharacter) || isSpace(nextCharacter))){
						addCharacter();
						getCharacter();
					}
				}
			}
			
			if(unrecognizedToken){
				if(!lookupToken()){
					if(verboseOutput)System.out.println("3rd else (while loop) of getNextToken: lexeme=" + lexeme + " IS UNRECOGNIZED nextCharacter="+nextCharacter);
					if(verboseOutput)System.out.println("***Setting unrecognized token to TRUE***");
					
				}
			}
		}
		
		//reset variable 'lexeme' to blank
		lexeme = "";
		
		
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
     * This method adds the current character \"nextCharacter\" to the string \"lexeme\" the nextToken.
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
			if(verboseOutput)System.out.println("null whitespace");
		}
		if(matcher == null){
			if(verboseOutput)System.out.println("null matcher");
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
				
				if(verboseOutput)System.out.println("lookupToken: " + lexeme + " found in hashmap");
				
				currenttoken = new Token(markdownTokenHashmap.get(x), lexeme); //how to know what type of token to add? 
				 
				unrecognizedToken = false;
				
				tokenBin.add(currenttoken);
				
				
				return true;
			}
			
				
			
				
		}
		if(verboseOutput)System.out.println("lookupToken:" + lexeme + "NOT found in hashmap");
		return false;
	}
	
	
	
	public Pattern compilePattern(Pattern p, String s){
		p = Pattern.compile(s);		
		//test 
		if(verboseOutput)System.out.println(s + "pattern was compiled");
		
		return p;
	}

	public boolean isUnrecognizedToken() {
		return unrecognizedToken;
	}

	public String getLexeme() {
		return lexeme;
	}
	
	public boolean endOfFile() {		
		return currentPosition >= sourceFile.length();
	}
}
	
