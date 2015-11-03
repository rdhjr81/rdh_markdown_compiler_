package edu.towson.cis.cosc455.rob.project1.implementation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Compiler {

	//definition for markdown extension
	static String markdownExtension = "mkd";
	
	//definitions for error messages
	static String invalidExtensionError = "Invalid extension error. File extension must be of type .mkd.";
	static String invalidTokenError = " is an Invalid Token.";
	
	//variables
	public static boolean endOfFile = false;
	public static boolean verboseOutput = true;
	public static Token currentToken = null;
	public static String contentsOfMarkdownFile = "";
	public static String markdownFileName;
	
	//token bin!!!!
	public static ArrayList<Token> tokenBin;
	
	Scanner reader;
	
	//Compiler Components
	public static LexicalAnalyzer lex;
	public static SyntaxAnalyzer syn;
	public static SemanticAnalyzer sem;
	
	public static void main(String[] args) throws CompilerException, FileNotFoundException,IOException {
		// TODO Auto-generated method stub
		if(!verifyExtensionType(args[0], markdownExtension)){
			throw new CompilerException(invalidExtensionError);
		}
		
		markdownFileName = getFileNameOfMarkdownFile(args[0]);
		
		System.out.println("File name is " + markdownFileName);
		
		contentsOfMarkdownFile = openMKDFile(args[0]);
		
		tokenBin = new ArrayList<Token>();
		
		lex = new LexicalAnalyzer(contentsOfMarkdownFile);
		
		syn = new SyntaxAnalyzer();
		
		sem = new SemanticAnalyzer();
		
		syn.markdown();
		
		
		/*while(!endOfFile){
			lex.getNextToken();
			if(lex.isUnrecognizedToken()){
				throw new CompilerException(lex.getLexeme() + invalidTokenError);
			}
			else if(verboseOutput) System.out.println(lex.currenttoken.toString());
			
		}*/
		
		if(verboseOutput){
			//print token stream out
			for(Token x : tokenBin){
				
				System.out.print(x.tokenType + ": " + x.content+ " ");
			}
		}
		System.out.println("Compiler end of file");
	}
	
	public static boolean verifyExtensionType(String fileName, String extensionType){
		return(fileName.endsWith('.'+ extensionType));
	}
	
	public static String openMKDFile(String fileName) throws FileNotFoundException, IOException{
		
		String contentsOfMarkdownFile = "";
		Scanner reader;
		
		try{
			reader = new Scanner(new FileReader(fileName));
			
			while(reader.hasNextLine()){
				contentsOfMarkdownFile += reader.nextLine();
			}
			reader.close();
		}
		catch(FileNotFoundException e){
			throw new FileNotFoundException(fileName + " not found");
		}
		catch(IOException e){
			throw new IOException(fileName + " IO Exception");
		}
		
		return contentsOfMarkdownFile;
	}

	public static boolean isEndOfFile() {
		return endOfFile;
	}

	public static void setEndOfFile(boolean endOfFile) {
		Compiler.endOfFile = endOfFile;
	}
	public static void addCurrentTokenToTokenBin() {
		tokenBin.add(lex.currenttoken);
	}
	public static void getNextTokenFromLex() {
		lex.getNextToken();
	}
	public static void getNextTokenFromLexAndAddTokenBin(){
		getNextTokenFromLex();
		tokenBin.add(lex.currenttoken);
	}
	
	public static String getFileNameOfMarkdownFile(String s){
		 return s.substring(
				 Math.max(s.lastIndexOf('\\'), s.lastIndexOf('/')) + 1, 
				 s.lastIndexOf('.'));
	}
	
	

}
