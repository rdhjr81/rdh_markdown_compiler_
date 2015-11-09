package edu.towson.cis.cosc455.rob.project1.implementation;
import MarkdownTokenDefinitions.TokenDefinitions.Type;

public class Token {
	
	
	/** The text content of a Token*/
	public String content;
	/** The type of a Token*/
	public Type tokenType;
	
	/**
	 * This constructor builds a Token using a type and content
	 * @param t
	 * @param content
	 */
	public Token( Type t , String content){
		this.tokenType = t;
		this.content = content;
	}
	/**
	 * This method builds a Token using only its type and sets the content to null
	 * @param t
	 */
	public Token( Type t){
		this.tokenType = t;
		this.content = "";
	}
	/**
	 * This method returns the content of a token
	 * @return a string
	 */
	public String toString(){
		if(content.equals("")) return "Token Type " + tokenType.toString();
		else return "Token Type: " + tokenType.toString() + "\tContents: " + content;
	}
	
}
