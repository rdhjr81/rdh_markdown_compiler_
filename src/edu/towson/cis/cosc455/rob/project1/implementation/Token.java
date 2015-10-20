package edu.towson.cis.cosc455.rob.project1.implementation;
import MarkdownTokenDefinitions.TokenDefinitions.Type;

public class Token {
	
	
	
	public String content;
	public Type tokenType;
	
	public Token( Type t , String content){
		this.tokenType = t;
		this.content = content;
	}
	public Token( Type t){
		this.tokenType = t;
		this.content = "";
	}
	
	public String toString(){
		if(content.equals("")) return "Token Type " + tokenType.toString();
		else return "Token Type: " + tokenType.toString() + "\tContents: " + content;
	}
	
}
