package edu.towson.cis.cosc455.rob.project1.implementation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Compiler {

	//definitions for file types
	static String markdownExtension = "mkd";
	
	//definitions for error messages
	static String invalidExtensionError = "Invalid extension error. File extension must be of type .mkd.";
	
	//variables
	static boolean verboseOutput = false;
	public static String contentsOfMarkdownFile = "";
	
	//
	BufferedReader reader;
	
	public static String currentToken = "";
	
	public static void main(String[] args) throws CompilerException, FileNotFoundException,IOException {
		// TODO Auto-generated method stub
		if(!verifyExtensionType(args[0], markdownExtension)){
			throw new CompilerException(invalidExtensionError);
		}
		
		contentsOfMarkdownFile = openMKDFile(args[0]);
		
	}
	
	public static boolean verifyExtensionType(String fileName, String extensionType){
		return(fileName.endsWith('.'+ extensionType));
	}
	
	static String openMKDFile(String fileName) throws FileNotFoundException, IOException{
		
		String contentsOfMarkdownFile = "";
		BufferedReader reader;
		
		try{
			reader = new BufferedReader(new FileReader(fileName));
			while(reader.readLine()!=null){
				contentsOfMarkdownFile += reader.readLine();
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

}
