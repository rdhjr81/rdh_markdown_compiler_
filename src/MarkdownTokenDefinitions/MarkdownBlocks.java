package MarkdownTokenDefinitions;

import java.util.HashMap;

import MarkdownTokenDefinitions.TokenDefinitions.Type;

public class MarkdownBlocks {
	public static HashMap <Type , Type> markdownScopeHashmap;
	
	public static final void populatemarkdownTokenHashmap(){
		
		markdownScopeHashmap.put(Type.DOCE, Type.DOCB);
		markdownScopeHashmap.put(Type.TITLEE , Type.TITLEB);
		markdownScopeHashmap.put(Type.PARAE, Type.PARAB);
		markdownScopeHashmap.put(Type.LISTITEME, Type.LISTITEMB);
		markdownScopeHashmap.put(Type.LINKE, Type.LINKB);
		markdownScopeHashmap.put(Type.ADDRESSE, Type.ADDRESSB);
		
		markdownScopeHashmap.put(Type.HEAD, Type.HEAD);
		markdownScopeHashmap.put(Type.BOLD, Type.BOLD);
		markdownScopeHashmap.put(Type.ITALICS, Type.ITALICS);
		
	}
	
	public static HashMap < Type , Type> getScopeBlockHashmap(){
		markdownScopeHashmap = new HashMap<Type, Type>();
		return markdownScopeHashmap;
	}
}
