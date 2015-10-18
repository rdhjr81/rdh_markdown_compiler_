package edu.towson.cis.cosc455.rob.project1.implementation;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Token {
	
	public static final String DOCB = "#BEGIN";
	public static final String DOCE	= "#END" ;
	public static final String HEAD	= "^" ;
	public static final String TITLEB	= "<" ;
	public static final String TITLEE	= ">" ;
	public static final String PARAB	= "{" ;
	public static final String PARAE	= "}" ;
	public static final String DEFB	= "$DEF" ;
	public static final String DEFUSEE	= "$END" ;
	public static final String EQSIGN	= "=" ;
	public static final String USEB	= "$USE" ;
	public static final String BOLD	= "**" ;
	public static final String ITALICS	=  "*" ;
	public static final String LISTITEMB 	= "+" ;	
	public static final String LISTITEME	= ";" ;
	public static final String NEWLINE		= "~" ;
	public static final String LINKB		= "[" ;
	public static final String LINKE		= "]" ;
	public static final String AUDIO		= "@" ;
	public static final String VIDEO		= "%" ;
	public static final String ADDRESSB	= "(" ;
	public static final String ADDRESSE	= ")" ;
	public static final ArrayList<String> WHITESPACE  = {' ', '\t', '\r' , '\n' } ;
	
	//String for pattern matching
	String letterPatternString = "a-zA-Z";
	String numberPatternString = "0-9";
	String whitespacePatternString = "\\s"; // \\s matches for whitespace characters: [ \t\n\x0B\f\r]
	
	//patterns for numbers, letters, whitespace
	public static final Pattern letterPattern;
	public static final Pattern numberPattern;
	public static final Pattern whitespacePattern;
	
	
	
	public static enum Type {TEXT, WHITESPACE,DOCB ,DOCE ,HEAD ,TITLEB ,TITLEE ,PARAB  ,PARAE ,
		DEFB ,DEFUSEE ,EQSIGN ,USEB ,BOLD ,ITALICS ,LISTITEMB  ,LISTITEME ,NEWLINE ,LINKB  ,LINKE  ,AUDIO ,
		VIDEO  ,ADDRESSB  ,ADDRESSE 
		
	}
	
	
}
