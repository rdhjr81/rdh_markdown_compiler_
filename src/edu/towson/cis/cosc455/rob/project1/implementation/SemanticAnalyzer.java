package edu.towson.cis.cosc455.rob.project1.implementation;

import java.util.ArrayList;
import java.util.Stack;

import MarkdownTokenDefinitions.HtmlDefinitions;
import MarkdownTokenDefinitions.TokenDefinitions.Type;


public class SemanticAnalyzer {
	public static ArrayList<Token> tokenBin;
	public static Stack<Token> tokenStack;
	public static int tokenBinIndex;
	public static boolean verboseOutput;
	
	public SemanticAnalyzer(){
		tokenBin = Compiler.tokenBin;
		tokenStack = new Stack<Token>();
		tokenBinIndex = 0;
		verboseOutput = Compiler.verboseOutput;
		
	}
	
	public String translateToHtml(){
		String htmlFile = "";
		boolean headBegin = false, italicsBegin = false, boldBegin = false, newListHasBegun = false, listHasEnded = false,
				linkBegin = false; 

		tokenBinIndex = 0;
		
		if(verboseOutput){
			
			System.out.println("Sem: translateToHtml()(): Starting html translation");
		}
		
		Token t = tokenBin.get(tokenBinIndex );
		while(tokenBinIndex  < tokenBin.size() - 1){
			switch(t.tokenType){
			case TEXT:
				htmlFile += t.content;
				break;
			case WHITESPACE:
				htmlFile += t.content;
				break;
			case DOCB: 
				htmlFile += getOpenTag(HtmlDefinitions.HTML_DOC);
				break;
			case TITLEB:
				htmlFile += getOpenTag(HtmlDefinitions.HTML_TITLE);
				break;
			case PARAB: 
				htmlFile += getOpenTag(HtmlDefinitions.HTML_PARA);
				break;
			case LISTITEMB:
				if(!newListHasBegun){
					newListHasBegun = true;
					htmlFile += getOpenTag(HtmlDefinitions.HTML_LIST);
					htmlFile += getOpenTag(HtmlDefinitions.HTML_LISTITEM);
				}
				else{
					htmlFile += getOpenTag(HtmlDefinitions.HTML_LISTITEM);
				}
				break;
				//special case
			case LINKB: 
				htmlFile = convertLinkNodeToHtmlLink(htmlFile);
				break;
			case AUDIO:  
				htmlFile = convertAudioNodeToHtml(htmlFile);
				break;
			case VIDEO:  
				htmlFile = convertVideoNodeToHtml(htmlFile);
			
		
			case NEWLINE:  
				htmlFile += getOpenTag(HtmlDefinitions.HTML_NEWLINE);
				break;
			case DOCE:  
				htmlFile += getCloseTag(HtmlDefinitions.HTML_DOC);
				break;
			case TITLEE:  
				htmlFile += getCloseTag(HtmlDefinitions.HTML_TITLE);
				break;
			case PARAE: 
				htmlFile += getCloseTag(HtmlDefinitions.HTML_PARA);
				break;
			case LISTITEME:  
				htmlFile += getCloseTag(HtmlDefinitions.HTML_LISTITEM);
				if(!isAnotherListItemAhead()){
					htmlFile += getCloseTag(HtmlDefinitions.HTML_LIST);
					newListHasBegun = false;
					listHasEnded = true;
				}
				
				
				break;
			case HEAD:  
				if(headBegin){
					htmlFile += getCloseTag(HtmlDefinitions.HTML_HEAD);
					headBegin = false;
				}
				else{
					headBegin = true;
					htmlFile += getOpenTag(HtmlDefinitions.HTML_HEAD);
					break;
				}
				break;
			case BOLD:  
				if(boldBegin){
					htmlFile += getCloseTag(HtmlDefinitions.HTML_BOLD);
					boldBegin = false;
				}
				else{
					htmlFile += getOpenTag(HtmlDefinitions.HTML_BOLD);
					boldBegin = true;
				}
				break;
			case ITALICS:  
				if(italicsBegin){
					htmlFile += getCloseTag(HtmlDefinitions.HTML_ITALICS);
					italicsBegin = false;
				}
				else{
					htmlFile += getOpenTag(HtmlDefinitions.HTML_ITALICS);
					italicsBegin = true;
				}
				break;

			case ADDRESSB: 
			case ADDRESSE:
			case LINKE: 
			default:
				if(verboseOutput){
					System.out.println("Oops, html switch came across a token containing " + t.content);
				}
				break;
			}
			if(verboseOutput){
				System.out.println("Sem: translateToHtml(): current token is " + t.tokenType);
				System.out.println("Sem: translateToHtml(): html is" + htmlFile);
			}
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex );
			
		}
		return htmlFile;
	}
	
	private String convertVideoNodeToHtml(String htmlFile) {
		// TODO Auto-generated method stub
		
		Token t;
		
		htmlFile += HtmlDefinitions.HTML_BRACKET_L + HtmlDefinitions.HTML_VIDEO+ " " +
				 HtmlDefinitions.HTML_SRC + "=\"";
		
		tokenBinIndex += 2; //skip past '%('
		t = tokenBin.get(tokenBinIndex);
		while(t.tokenType != Type.ADDRESSE){
			htmlFile += t.content;
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex);
		}
		
		return htmlFile += "\"" + HtmlDefinitions.HTML_BRACKET_CLOSE + HtmlDefinitions.HTML_BRACKET_R;
	}

	private String convertAudioNodeToHtml(String htmlFile) {
		// TODO Auto-generated method stub
		
		Token t;
		
		htmlFile += HtmlDefinitions.HTML_BRACKET_L + HtmlDefinitions.HTML_AUDIO + " " +
				 HtmlDefinitions.HTML_AUDIO_CONTROLS + HtmlDefinitions.HTML_BRACKET_R + "\n";
		htmlFile += HtmlDefinitions.HTML_BRACKET_L + HtmlDefinitions.HTML_SOURCE + " " + HtmlDefinitions.HTML_SRC + "=\"";
				;
		tokenBinIndex += 2; //skip past '@('
		t = tokenBin.get(tokenBinIndex);
		while(t.tokenType != Type.ADDRESSE){
			htmlFile += t.content;
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex);
		}
		
		return htmlFile += "\"" + HtmlDefinitions.HTML_BRACKET_R + "\n" + getCloseTag(HtmlDefinitions.HTML_AUDIO);
		
	}

	private String convertLinkNodeToHtmlLink(String htmlFile) {
		// TODO Auto-generated method stubs
		String linkText = "", linkAddress = "";
		Token t;
		
		tokenBinIndex++;
		t = tokenBin.get(tokenBinIndex);
		while(t.tokenType != Type.LINKE){
			linkText += t.content;
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex);
		}
		//skip LINK END Token
		tokenBinIndex++;
		t = tokenBin.get(tokenBinIndex);
		//Grab address of link, ignore whitespace between ) [
		if(t.tokenType == Type.WHITESPACE){
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex);
		}
		if(t.tokenType == Type.ADDRESSB){
			tokenBinIndex++;
			t = tokenBin.get(tokenBinIndex);
			while(t.tokenType != Type.ADDRESSE){
				linkAddress += t.content;
				tokenBinIndex++;
				t = tokenBin.get(tokenBinIndex);
			}
		}
		//completed scan of link address
		//convert to html
		if(verboseOutput){
			System.out.println("Sem: convertLinkNodeToHtmlLink(): link address " + linkAddress);
			System.out.println("Sem: convertLinkNodeToHtmlLink(): link text " + linkText);
		}
		 return htmlFile += getOpenTag(HtmlDefinitions.HTML_LINK + " " + HtmlDefinitions.HTML_HREF + "=\"" + linkAddress + "\"") + 
				linkText + getCloseTag(HtmlDefinitions.HTML_LINK);
		
		
	}

	private boolean isAnotherListItemAhead() {
		// TODO Auto-generated method stub
		int localIndex = tokenBinIndex;
		Token t = tokenBin.get(++localIndex);
		
		while(t.tokenType == Type.WHITESPACE){
			if(verboseOutput){
				System.out.println("Sem: isAnotherListItemAhead(): during comparison, current token is " + t.tokenType + " , local index is " + localIndex);
			}
			localIndex++;
			t = tokenBin.get(localIndex);
		}
		if(verboseOutput){
			System.out.println("Sem: isAnotherListItemAhead(): during comparison, current token is " + t.tokenType);
			System.out.println("Sem: isAnotherListItemAhead(): Comparing " + t.tokenType + " with " + Type.LISTITEMB);
		}
		return t.tokenType == (Type.LISTITEMB);
	}

	private String getCloseTag(String htmlTag) {
		// TODO Auto-generated method stub
		return HtmlDefinitions.HTML_BRACKET_L + HtmlDefinitions.HTML_BRACKET_CLOSE + htmlTag + HtmlDefinitions.HTML_BRACKET_R;
	}

	private String getOpenTag(String htmlTag) {
		return HtmlDefinitions.HTML_BRACKET_L + htmlTag + HtmlDefinitions.HTML_BRACKET_R;
		
	}

	public void resolveVariables() throws CompilerException{
		
		boolean headBegin = false, italicsBegin = false, boldBegin = false; 
		for(tokenBinIndex = 0; tokenBinIndex < tokenBin.size(); tokenBinIndex++){
			Token t = tokenBin.get(tokenBinIndex);
			
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
				if(verboseOutput){System.out.println("Sem: going to next token");}
				
				break;
			case USEB:
				if(verboseOutput){System.out.println("Sem: " + Type.USEB +" found, calling variableHunter()");}
				variableHunter();
				
				break;
			case DOCE:  
				if(verboseOutput){System.out.println("Sem: " + Type.DOCE +" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.DOCB);
				break;
			case TITLEE:  
				if(verboseOutput){System.out.println("Sem: " + Type.TITLEE +" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.TITLEB);
				break;
			case PARAE: 
				if(verboseOutput){System.out.println("Sem: " + Type.PARAE +" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.PARAB);
				break;
			case LISTITEME:  
				if(verboseOutput){System.out.println("Sem: " + Type.LISTITEME+" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.LISTITEMB);
				break;
			case ADDRESSE:  
				if(verboseOutput){System.out.println("Sem: " + Type.ADDRESSE+" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.ADDRESSB);
				break;
			case LINKE:  
				if(verboseOutput){System.out.println("Sem: " + Type.LINKE+" found, calling variableDefinitionDestroyer()");}
				variableDefinitionDestroyer(Type.LINKB);
				break;
			
			case HEAD:  
				if(headBegin){
					if(verboseOutput){System.out.println("Sem: " + Type.HEAD +" found, calling variableDefinitionDestroyer()");}
				
					variableDefinitionDestroyer(Type.HEAD);
					headBegin = false;
				}
				else{
					headBegin = true;
				}
				break;
			case BOLD:  
				if(boldBegin){
					if(verboseOutput){System.out.println("Sem: " + Type.BOLD +" found, calling variableDefinitionDestroyer()");}
				
					variableDefinitionDestroyer(Type.BOLD);
					boldBegin = false;
				}
				else{
					boldBegin = true;
				}
				break;
			case ITALICS:  
				if(italicsBegin){
					if(verboseOutput){System.out.println("Sem: " + Type.ITALICS +" found, calling variableDefinitionDestroyer()");}
				
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
	
	public String findVariableDefinition(String varName, int varUseIndex) throws CompilerException{
		int currentIndex = varUseIndex, tempIndex = varUseIndex;
	
		
		while(currentIndex > 0){
			currentIndex--;
			Token t = tokenBin.get(currentIndex);
			
			if(verboseOutput){
				System.out.println("Sem: findVariableDefinition(): current token is " + t.tokenType + ", content is " + t.content);
			}

			if(t.tokenType == Type.DEFB){
				tempIndex = currentIndex;
				
				if(verboseOutput){
					System.out.println("Sem: findVariableDefinition(): " + Type.DEFB + " Found");
				}
				
				while(t.tokenType != Type.TEXT){
					t = tokenBin.get(currentIndex++);
				}
				
				if(t.content.equals(varName)){
					if(verboseOutput){
						System.out.println("Sem: findVariableDefinition(): Varname " + varName + " Found");
					}
					t = tokenBin.get(++currentIndex);
					while(t.tokenType != Type.EQSIGN){
						t = tokenBin.get(++currentIndex);
					}
					while(t.tokenType != Type.TEXT){
						t = tokenBin.get(++currentIndex);
					}
					if(verboseOutput){
						System.out.println("Sem: findVariableDefinition(): var def is " + t.content );
					}
					return t.content;
					
				}
				else{
					if(verboseOutput){
						System.out.println("Sem: findVariableDefinition(): searching for " + varName + " found " + t.content); 
					}
					currentIndex = tempIndex - 1;
					t = tokenBin.get(currentIndex);
				}
			}
			
			
			
			
			
		}
		throw new CompilerException(getSemanticErrorMessage(varName)); 
	}
	
	private String getSemanticErrorMessage(String varName) {
		String errorMessage = "Semantic Error: variable : \""+ varName + "\" is undefined";
		
		return "\n" + errorMessage + "\n";
	}

	public void replaceVarNameWithValue(int index, String content){
		
		//tokenBin.remove(index);
		tokenBin.add(index, new Token(Type.TEXT, content));
		System.out.println("Sem: replaceVarNameWithValue(): after replacing var name with def :" );
		System.out.println(printTokenStream());
		
	}
	
	public void snipVarUsage(int varUseIndex){
		String before = "", after = "";
		int count = 0;
		
		before = printTokenStream();
		
		
		Token t = tokenBin.get(varUseIndex);
		
		while(t.tokenType != Type.DEFUSEE){
			if(verboseOutput){
				printTokenStream();
			}
			tokenBin.remove(varUseIndex);
			t = tokenBin.get(varUseIndex);
		}
		tokenBin.remove(varUseIndex);
		if(verboseOutput){
			System.out.println("Sem: snipVarUsage(): removing " + t.tokenType + ": " + t.content + " at index " + varUseIndex);
		}
		System.out.println("Sem: snipVarUsage(): Tokens after removing var usage");
		
		after = printTokenStream();	
		
		printBeforeAndAfter(before,after);
		
		/*while(t.tokenType != Type.DEFUSEE){
			
			t = tokenBin.get(varUseIndex + ++count);
		}
		
		for(int i = 0; i < count; i++){
			t = tokenBin.get(varUseIndex);
			if(verboseOutput){
				System.out.println("Sem: snipVarUsage(): removing " + t.tokenType + ": " + t.content + " at index " + i);
			}
			tokenBin.remove(varUseIndex);
		}*/
		
		
		
	}
	
	private void printBeforeAndAfter(String before, String after) {
		// TODO Auto-generated method stub
		System.out.println("Before: " + before);
		System.out.println("After: " + after);
	}

	private String printTokenStream() {
		// TODO Auto-generated method stub
		String tokens = "";
		for(Token x : tokenBin){
			tokens += x.content;
		}
		tokens += "\n";
		return tokens;
	}

	public void variableHunter() throws CompilerException{
		int varUseIndex = 0;
		String varName, varContent;
		
		String before = printTokenStream();
		
		if(verboseOutput){
			System.out.println("Sem: variableHunter(): $USE  at index " + tokenBinIndex);
		}
		
		varUseIndex = getNextIndexOfType(Type.TEXT, tokenBinIndex);
		
		
		
		varName = getVariableName((Token)tokenBin.get(varUseIndex));
		
		if(verboseOutput){
			System.out.println("Sem: variableHunter(): varName is " + varName + " varIndex is " + varUseIndex);
		}
		
		varContent = findVariableDefinition(varName,varUseIndex);
		
		if(verboseOutput){
			System.out.println("Sem: variableHunter(): varContent is " + varContent);
			System.out.println("Sem: variableHunter(): calling snipVarUsage()" );
		}
		String after = printTokenStream();
		
		printBeforeAndAfter(before, after);
		
		snipVarUsage(tokenBinIndex);
		replaceVarNameWithValue(tokenBinIndex, varContent);
		
	}
	public void variableDefinitionDestroyer(Type terminalType){
		int currentIndex = tokenBinIndex;
		int count = 0;
		Token t;

		do{
			currentIndex--;
			
			t = tokenBin.get(currentIndex);
			
			if(verboseOutput){
				System.out.println("Sem: variableDefinitionDestroyer(): current token is " + t.tokenType + " hunting for " + terminalType);
				System.out.println("Sem: variableDefinitionDestroyer(): currentIndex + " + currentIndex);
			}
			
			if(t.tokenType == Type.DEFUSEE){
				
				
				while(t.tokenType != Type.DEFB){
					currentIndex--;
					count++;
					t = tokenBin.get(currentIndex);
					System.out.println("Sem: variableDefinitionDestroyer():  current token is " + t.content);
				}
				count++;
				for(int i = 0; i < count; i++){
					t = tokenBin.get(currentIndex);
					
					System.out.println("Sem: variableDefinitionDestroyer(): removing " + t.tokenType + ": " + t.content + " at index " + currentIndex);
					System.out.println(printTokenStream());
					tokenBin.remove(currentIndex);
				}
				printTokenStream();
			}
			
			if(verboseOutput && t.tokenType == terminalType){
				System.out.println("Sem: variableDefinitionDestroyer(): current token is " + t.tokenType + " hunting for " + terminalType);
				System.out.println("I'll be exiting now");
			}
			
			
		}while(t.tokenType != terminalType);
		if(verboseOutput && t.tokenType == terminalType){
			System.out.println("Sem: variableDefinitionDestroyer(): current token is " + t.tokenType + " hunting for " + terminalType);
			System.out.println("Elvis has left the building");
		}
		System.out.println(printTokenStream());
		
		
		/*
		while(t.tokenType != terminalType && currentIndex > 0){
			
			if(t.tokenType == Type.DEFUSEE){
					currentIndex--;
					t = tokenBin.get(currentIndex);
					System.out.println("current index: " + currentIndex);
				}
				if(verboseOutput){
					System.out.println("Sem: variableDefinitionDestroyer(): $DEF found at " + currentIndex);
				
				while(t.tokenType != Type.DEFUSEE){
					tokenBin.remove(currentIndex);
					
					if(true){
						//print token stream out
						for(Token x : tokenBin){
							
							System.out.print( x.content);
							//System.out.println
						}
						//System.out.println("current index: " + currentIndex);
					}
					t = tokenBin.get(currentIndex);
				}
				tokenBin.remove(currentIndex);
			//t = tokenBin.get(currentIndex--);
			}
			currentIndex--;
			t = tokenBin.get(currentIndex);
			*/
			/*if(t.tokenType == Type.DEFUSEE){
				
				endOfVarDef = currentIndex;
				
				if(verboseOutput){
					System.out.println("Sem: variableDefinitionDestroyer(): $END found at " + endOfVarDef);
				}
				
				//t = tokenBin.get(--currentIndex);
				
				while(t.tokenType != Type.DEFB){
					t = tokenBin.get(--currentIndex);
				}
				startOfVarDef = currentIndex;
				
				if(verboseOutput){
					System.out.println("Sem: variableDefinitionDestroyer(): $DEF found at " + startOfVarDef);
				}
				
				for(int i = 0; i < endOfVarDef - startOfVarDef; i++){
					t = tokenBin.get(startOfVarDef);
					System.out.println("Sem: variableDefinitionDestroyer(): removing " + t.content);
					tokenBin.remove(startOfVarDef);
				}
				
			}
			else{
				currentIndex--;
				t = tokenBin.get(currentIndex);
			}*/
			
		}
	
}
	

