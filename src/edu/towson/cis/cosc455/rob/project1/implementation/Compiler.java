package edu.towson.cis.cosc455.rob.project1.implementation;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import MarkdownTokenDefinitions.HtmlDefinitions;
import MarkdownTokenDefinitions.MarkdownBlocks;
import MarkdownTokenDefinitions.TokenDefinitions;

public class Compiler {

	//definition for markdown extension
	static String markdownExtension = TokenDefinitions.MARKDOWN_FILE_EXTENSION;
	static String htmlExtension = HtmlDefinitions.HTML_EXTENSION;
	
	//definitions for error messages
	static String invalidExtensionError = "Invalid extension error. File extension must be of type .mkd.";
	static String invalidTokenError = " is an Invalid Token.";
	
	//variables
	
	public static boolean endOfFile = false;
	public static boolean verboseOutput = false;
	public static Token currentToken = null;
	public static String contentsOfMarkdownFile = "", markdownFileName = "", markdownFileLocation = "";
	public static String htmlFile = "";
	
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
		
		if(verboseOutput){
			System.out.println("File name is " + markdownFileName);
		}
		
		markdownFileLocation = getFileLocationOfMarkdownFile(args[0]);
		
		if(verboseOutput){
			System.out.println("File location is " + markdownFileLocation);
		}
		
		contentsOfMarkdownFile = openMKDFile(args[0]);
		
		tokenBin = new ArrayList<Token>();
		
		lex = new LexicalAnalyzer(contentsOfMarkdownFile);
		
		syn = new SyntaxAnalyzer();
		
		sem = new SemanticAnalyzer();
		
		syn.markdown();
		
		if(verboseOutput){
			System.out.println("syntax Analysis complete, starting semantic analyzer");
		}
		
		sem.resolveVariables();
		
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
				
				System.out.print( x.content);
			}
		}
		
		htmlFile = sem.translateToHtml();
		if(verboseOutput){
			System.out.println(htmlFile);
			
		}
		htmlFile = writeHtmlToFile();
		
		openHTMLFileInBrowswer(htmlFile);
		
		if(verboseOutput){
			System.out.println("Compiler end of file");
		}
	}
	
	private static String writeHtmlToFile() {
		String pathname = markdownFileLocation + markdownFileName + "." + htmlExtension;
		try {
			FileWriter out = new FileWriter(new File(pathname));
			out.write(htmlFile);
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pathname;
		
	}

	private static String getFileLocationOfMarkdownFile(String s) {
		if(s.contains("/") || s.contains("\\")){
			return s.substring(0, Math.max(s.lastIndexOf('\\'), s.lastIndexOf('/')) + 1);
		}
		else return "./";
	}

	public static boolean verifyExtensionType(String fileName, String extensionType){
		
		
		if(verboseOutput){
			System.out.println("Filename: " + fileName);
			System.out.println("Extension type: " + extensionType);
		}
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
	public static void getNextTokenFromLex() throws CompilerException {
		lex.getNextToken();
	}
	public static void getNextTokenFromLexAndAddTokenBin() throws CompilerException{
		getNextTokenFromLex();
		tokenBin.add(lex.currenttoken);
	}
	
	public static String getFileNameOfMarkdownFile(String s){
		 return s.substring(
				 Math.max(s.lastIndexOf('\\'), s.lastIndexOf('/')) + 1, 
				 s.lastIndexOf('.'));
	}
	
	public static void openHTMLFileInBrowswer(String htmlFileStr){
		File file= new File(htmlFileStr.trim());
		if(!file.exists()){
			System.err.println("File "+ htmlFileStr +" does not exist.");
			return;
		}
		try{
			Desktop.getDesktop().browse(file.toURI());
		}
		catch(IOException ioe){
			System.err.println("Failed to open file");
			ioe.printStackTrace();
		}
		return ;
	}
	
	

}
