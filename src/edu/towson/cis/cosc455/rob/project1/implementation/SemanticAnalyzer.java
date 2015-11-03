package edu.towson.cis.cosc455.rob.project1.implementation;

import java.util.ArrayList;

import java.util.Stack;

import MarkdownTokenDefinitions.TokenDefinitions.Type;


public class SemanticAnalyzer {
	public static ArrayList<Token> tokenBin;
	public static Stack<Token> tokenStack;
	public static int tokenBinIndex;
	
	public SemanticAnalyzer(){
		tokenBin = Compiler.tokenBin;
		tokenStack = new Stack<Token>();
		tokenBinIndex = 0;
		
	}
	
	public void resolveVariables(){
		
		boolean headBegin = false, italicsBegin = false, boldBegin = false; 
		for(Token t : tokenBin){
			switch(t.tokenType){
			case TEXT:
			case WHITESPACE:
			case DOCB:  
			case TITLEB:  
			case PARAB:  
			case DEFB:  
			case LISTITEMB:  
			case LINKB:  
			case AUDIO:  
			case VIDEO:  
			case ADDRESSB: 
			case NEWLINE:  
			case DEFUSEE:  
			case EQSIGN: 
				tokenBinIndex++;
				break;
			case USEB:
				variableHunter();
				
				break;
			case DOCE:  
				variableDefinitionDestroyer(Type.DOCB);
				break;
			case TITLEE:  
				variableDefinitionDestroyer(Type.TITLEB);
				break;
			case PARAE:  
				variableDefinitionDestroyer(Type.PARAB);
				break;
			case LISTITEME:  
				variableDefinitionDestroyer(Type.LISTITEMB);
				break;
			case ADDRESSE:  
				variableDefinitionDestroyer(Type.ADDRESSB);
				break;
			case LINKE:  
				variableDefinitionDestroyer(Type.LINKB);
				break;
			
			case HEAD:  
				if(headBegin){
					variableDefinitionDestroyer(Type.HEAD);
					headBegin = false;
				}
				else{
					headBegin = true;
				}
				break;
			case BOLD:  
				if(boldBegin){
					variableDefinitionDestroyer(Type.BOLD);
					boldBegin = false;
				}
				else{
					boldBegin = true;
				}
				break;
			case ITALICS:  
				if(italicsBegin){
					variableDefinitionDestroyer(Type.ITALICS);
					italicsBegin = false;
				}
				else{
					italicsBegin = true;
				}
				break;
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			}
			
		}
	}
	
	public int getNextIndexOfType(Type t, int currentIndex){
		while(tokenBin.get(currentIndex).tokenType != t){
			currentIndex++;
		}
		return currentIndex;
	}
	
	public String getVariableName (Token t){
		return t.content;
	}
	
	public String findVariableDefinition(String varName){
		int currentIndex = tokenBinIndex;
		
		while(currentIndex >= 0){
			currentIndex--;
			Token t = tokenBin.get(++currentIndex);
			if(t.tokenType == Type.DEFB){
				t = tokenBin.get(++currentIndex);
				if(t.content == varName){
					t = tokenBin.get(++currentIndex);
					while(t.tokenType != Type.EQSIGN){
						t = tokenBin.get(++currentIndex);
					}
					while(t.tokenType != Type.TEXT){
						t = tokenBin.get(++currentIndex);
					}
					return t.content;
					
				}
			}
			
		}
		return "Variable name: "+ varName + "not resolved";
	}
	public void replaceVarNameWithValue(int index, String content){
		tokenBin.remove(index);
		tokenBin.add(index, new Token(Type.TEXT, content));
	}
	public void snipVarUsage(){
		Token t = tokenBin.get(tokenBinIndex);
		int endOfVarUsage = tokenBinIndex;
		int startOfVarUsage =  tokenBinIndex;
		
		while(t.tokenType != Type.DEFUSEE){
			t = tokenBin.get(++endOfVarUsage);
		}
		
		for(int i = startOfVarUsage; i <= endOfVarUsage; i++){
			tokenBin.remove(i);
		}
		
	}
	public void variableHunter(){
		int varUseIndex = 0;
		String varName, varContent;
		
		varUseIndex = getNextIndexOfType(Type.TEXT, tokenBinIndex);
		varName = getVariableName((Token)tokenBin.get(varUseIndex));
		varContent = findVariableDefinition(varName);
		snipVarUsage();
		replaceVarNameWithValue(varUseIndex, varContent);
		
	}
	public void variableDefinitionDestroyer(Type terminalType){
		int currentIndex = tokenBinIndex;
		int endOfVarDef = tokenBinIndex;
		int startOfVarDef =  tokenBinIndex;
		
		Token t = tokenBin.get(--currentIndex);
		
		while(t.tokenType != terminalType){
			if(t.tokenType == Type.DEFUSEE){
				endOfVarDef = currentIndex;
				t = tokenBin.get(--currentIndex);
				while(t.tokenType != Type.DEFB){
					t = tokenBin.get(--currentIndex);
				}
				startOfVarDef = currentIndex;
				
				for(int i = startOfVarDef; i <= endOfVarDef; i++){
					tokenBin.remove(i);
				}
				
			}
			
		}
	
	}
	
	/*public class Variable{
		public String varName;
		public String varContent;
		
		public Variable(String name, String content){
			this.varName = name;
			this. varContent = content;
		}

		public String getVarName() {
			return varName;
		}

		public void setVarName(String varName) {
			this.varName = varName;
		}

		public String getVarContent() {
			return varContent;
		}

		public void setVarContent(String varContent) {
			this.varContent = varContent;
		}
	}*/
}
