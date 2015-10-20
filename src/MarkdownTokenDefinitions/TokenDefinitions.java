package MarkdownTokenDefinitions;

import java.util.HashMap;

public class TokenDefinitions {
	
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
	
	public static enum Type {TEXT, WHITESPACE,DOCB ,DOCE ,HEAD ,TITLEB ,TITLEE ,PARAB  ,PARAE ,
		DEFB ,DEFUSEE ,EQSIGN ,USEB ,BOLD ,ITALICS ,LISTITEMB,LISTITEME ,NEWLINE ,LINKB  ,LINKE  ,AUDIO ,
		VIDEO  ,ADDRESSB  ,ADDRESSE 
	}
	
	public static HashMap <String , Type> markdownTokenHashmap;
	
	public static void instantiateMarkdownTokenHashMap(){
		
	}
	
	public static final void populatemarkdownTokenHashmap(){
		markdownTokenHashmap.put(DOCB, Type.DOCB);
		markdownTokenHashmap.put(DOCE, Type.DOCE);
		markdownTokenHashmap.put(HEAD, Type.HEAD);
		markdownTokenHashmap.put(TITLEB , Type.TITLEB);
		markdownTokenHashmap.put(TITLEE, Type.TITLEE);
		markdownTokenHashmap.put(PARAB, Type.PARAB);
		markdownTokenHashmap.put(PARAE, Type.PARAE);
		markdownTokenHashmap.put(DEFB, Type.DEFB);
		markdownTokenHashmap.put(DEFUSEE, Type.DEFUSEE);
		markdownTokenHashmap.put(EQSIGN, Type.EQSIGN);
		markdownTokenHashmap.put(USEB, Type.USEB);
		markdownTokenHashmap.put(BOLD, Type.BOLD);
		markdownTokenHashmap.put(ITALICS, Type.ITALICS);
		markdownTokenHashmap.put(LISTITEMB, Type.LISTITEMB);
		markdownTokenHashmap.put(LISTITEME, Type.LISTITEME);
		markdownTokenHashmap.put(NEWLINE, Type.NEWLINE);
		markdownTokenHashmap.put(LINKB, Type.LINKB);
		markdownTokenHashmap.put(LINKE, Type.LINKE);
		markdownTokenHashmap.put(AUDIO, Type.AUDIO);
		markdownTokenHashmap.put(VIDEO, Type.VIDEO);
		markdownTokenHashmap.put(ADDRESSB, Type.ADDRESSB);
		markdownTokenHashmap.put(ADDRESSE, Type.ADDRESSE);
	}
	
	public static HashMap <String , Type> getMarkdownHashmap(){
		markdownTokenHashmap = new HashMap<String, TokenDefinitions.Type>();
		return markdownTokenHashmap;
	}
	//list of definitions used by Lexical Analyzer for comparison
	/*public static final ArrayList<String> definitionsList;
	
	public static final ArrayList<String> populateDefinitionsList(){
		definitionsList.add(DOCB);
		definitionsList.add(DOCE);
		definitionsList.add(HEAD);
		definitionsList.add(TITLEB);
		definitionsList.add(TITLEE);
		definitionsList.add(PARAB);
		definitionsList.add(PARAE);
		definitionsList.add(DEFB);
		definitionsList.add(DEFUSEE);
		definitionsList.add(EQSIGN);
		definitionsList.add(USEB);
		definitionsList.add(BOLD);
		definitionsList.add(ITALICS);
		definitionsList.add(LISTITEMB);
		definitionsList.add(LISTITEME);
		definitionsList.add(NEWLINE);
		definitionsList.add(LINKB);
		definitionsList.add(LINKE);
		definitionsList.add(AUDIO);
		definitionsList.add(VIDEO);
		definitionsList.add(ADDRESSB);
		definitionsList.add(ADDRESSE);
	}
	
	public static ArrayList<String> getDefinitionList(){
		return definitionsList;
	}	*/


}
	
	
	/*public static String TokenList[]; 
	
	public static Hashtable<String, Type> tokenDictionary = new Hashtable<String, Token.Type>();
	
	public static void loadTokenHashTable(){
		tokenDictionary.put(DOCB, Token.;
	}*/
	
	

