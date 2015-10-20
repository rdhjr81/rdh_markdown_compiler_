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
	
	public static String contentsOfMarkdownFile = "";
	
	//token bin!!!!
	public static ArrayList<Token> tokenBin;
	
	Scanner reader;
	
	public static String currentToken = "";
	
	//Compiler Components
	public static LexicalAnalyzer lex;
	
	public static void main(String[] args) throws CompilerException, FileNotFoundException,IOException {
		// TODO Auto-generated method stub
		if(!verifyExtensionType(args[0], markdownExtension)){
			throw new CompilerException(invalidExtensionError);
		}
		
		contentsOfMarkdownFile = openMKDFile(args[0]);
		
		 lex = new LexicalAnalyzer(contentsOfMarkdownFile);
		
		while(!endOfFile){
			lex.getNextToken();
			if(lex.isUnrecognizedToken()){
				throw new CompilerException(lex.getLexeme() + invalidTokenError);
			}
			else System.out.println(lex.currenttoken.toString());
			
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
	

}
