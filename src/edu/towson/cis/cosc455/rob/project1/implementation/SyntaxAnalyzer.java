package edu.towson.cis.cosc455.rob.project1.implementation;

import MarkdownTokenDefinitions.TokenDefinitions;
import MarkdownTokenDefinitions.TokenDefinitions.Type;

/*markdown	: DOCB code DOCE ;
	code		: vardef head? body ;
	head		: HEAD title? HEAD ;
	title		: TITLEB TEXT* TITLEE;
	body		: (paragraph | listitem | link | audio | video | varuse| (NEWLINE) | TEXT)* ;
	paragraph	:  PARAB vardef? (link | listitem| audio | video|  varuse|  (NEWLINE)| TEXT)* PARAE ;
	vardef		: (DEFB TEXT EQSIGN TEXT DEFUSEE)* ;
	varuse		: USEB TEXT+ DEFUSEE ;
	boldtext	: BOLD TEXT+ BOLD  ;
	italicstext	: ITALICS TEXT+ ITALICS ; 
	link		: LINKB TEXT+ LINKE ADDRESSB TEXT+ ADDRESSE;
	audio		: AUDIO ADDRESSB TEXT+ ADDRESSE ;
	video		: VIDEO ADDRESSB TEXT+ ADDRESSE ;
	listitem	: LISTITEMB (TEXT | varuse)+ LISTITEME ; */

public class SyntaxAnalyzer implements edu.towson.cis.cosc455.rob.project1.interfaces.SyntaxAnalyzer {
	
	public boolean syntaxError;
	public boolean verboseOutput;
	public Token currentToken;
	public LexicalAnalyzer lex;
	public TokenDefinitions def;
	
	public String syntaxErrorMessage;
	
	
	
	public SyntaxAnalyzer() throws CompilerException {
		// TODO Auto-generated constructor stub
		syntaxError = false;
		
		verboseOutput = Compiler.verboseOutput;
		
		syntaxErrorMessage = "Syntax Error";
		
		lex = Compiler.lex;
		
		lex.getNextToken();
		
		

	}
	@Override
	public void markdown() throws CompilerException {
		 //markdown	: DOCB code DOCE ;
		if(!syntaxError){
			doc_begin();
			code();
			doc_end();
		}
	}
	
	public void doc_begin() throws CompilerException {
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.DOCB){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.DOCB));
			}
		}
	}
	
	public void doc_end() throws CompilerException {
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.DOCE){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.DOCE));
			}
		}
	}
	@Override
	public void bold() throws CompilerException {
		// TODO Auto-generated method stub
		//boldtext	: BOLD TEXT+ BOLD  ;
		
		if(!syntaxError){
			lex.getNextToken();
			if(lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.WHITESPACE){
				textOrWhitespaceMultipleTimes();
			}
			else{
					setErrorFlag();
					throw new CompilerException(getSyntaxErrorMessage(Type.TEXT));
				}
			
			if(lex.currenttoken.tokenType == Type.BOLD){
					lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.BOLD));
			}
		}
	}
	//code		: vardef head? body ;
	public void code() throws CompilerException {
		if(!syntaxError){
			
			whiteSpace();
			variableDefine();
			whiteSpace();
			head();
			body();
		}
	}
	
	@Override
	public void head() throws CompilerException {
		// TODO Auto-generated method stub
		//head		: HEAD title? HEAD ;
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.HEAD){
				lex.getNextToken();
				whiteSpace();
				title();
				whiteSpace();
				if(lex.currenttoken.tokenType == Type.HEAD){
					lex.getNextToken();
				}
				else{
					setErrorFlag();
					throw new CompilerException(getSyntaxErrorMessage(Type.HEAD));
				}
			}
		}
	}
	public void textOrWhitespaceMultipleTimes() throws CompilerException{
		while(lex.currenttoken.tokenType == Type.TEXT || 
				lex.currenttoken.tokenType == Type.WHITESPACE){
			lex.getNextToken();
		}
	}
	@Override
	public void title() throws CompilerException {
		// TODO Auto-generated method stub
		if(lex.currenttoken.tokenType == Type.TITLEB){
			
			lex.getNextToken();
			textOrWhitespaceMultipleTimes();
			
			if(lex.currenttoken.tokenType == Type.TITLEE){
				lex.getNextToken();
			}
			else {
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.TITLEE));
			}
		}
	}

	@Override
	public void body() throws CompilerException {
		// TODO Auto-generated method stub
		//body		: (paragraph | listitem | link | audio | video | varuse 
		//| (NEWLINE) | TEXT)* ;
		while(lex.currenttoken.tokenType == Type.PARAB ||
				lex.currenttoken.tokenType == Type.LISTITEMB ||
				lex.currenttoken.tokenType == Type.LINKB ||
				lex.currenttoken.tokenType == Type.AUDIO ||
				lex.currenttoken.tokenType == Type.VIDEO ||
				lex.currenttoken.tokenType == Type.USEB ||
				lex.currenttoken.tokenType == Type.NEWLINE ||
				lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.BOLD ||
				lex.currenttoken.tokenType == Type.ITALICS ||
				lex.currenttoken.tokenType == Type.WHITESPACE){
			
		
			if(lex.currenttoken.tokenType == Type.PARAB){
				paragraph();
			}
			else if(lex.currenttoken.tokenType == Type.LISTITEMB){
				listitem();
			}
			else if(lex.currenttoken.tokenType == Type.LINKB){
				link();
			}
			else if(lex.currenttoken.tokenType == Type.AUDIO){
				audio();
			}
			else if(lex.currenttoken.tokenType == Type.VIDEO){
				video();
			}
			else if(lex.currenttoken.tokenType == Type.USEB){
				variableUse();
			}
			else if(lex.currenttoken.tokenType == Type.NEWLINE){
				newline();
			}
			else if(lex.currenttoken.tokenType == Type.BOLD){
				bold();
			}
			else if(lex.currenttoken.tokenType == Type.ITALICS){
				italics();
			}
			else{
				textOrWhitespaceMultipleTimes();
			}
		}
				
	}

	@Override
	public void paragraph() throws CompilerException {
		// TODO Auto-generated method stub
		//paragraph	:  PARAB vardef? (link | listitem| audio | video|  varuse|  (NEWLINE)| TEXT)* PARAE ;
		if(!syntaxError){
			lex.getNextToken();
			whiteSpace();
			variableDefine(); 
			whiteSpace();
		
			while(	lex.currenttoken.tokenType == Type.LISTITEMB ||
					lex.currenttoken.tokenType == Type.LINKB ||
					lex.currenttoken.tokenType == Type.AUDIO ||
					lex.currenttoken.tokenType == Type.VIDEO ||
					lex.currenttoken.tokenType == Type.USEB ||
					lex.currenttoken.tokenType == Type.NEWLINE ||
					lex.currenttoken.tokenType == Type.TEXT ||
					lex.currenttoken.tokenType == Type.BOLD ||
					lex.currenttoken.tokenType == Type.ITALICS ||
					lex.currenttoken.tokenType == Type.WHITESPACE){
				
			
				if(lex.currenttoken.tokenType == Type.LISTITEMB){
					listitem();
				}
				else if(lex.currenttoken.tokenType == Type.LINKB){
					link();
				}
				else if(lex.currenttoken.tokenType == Type.AUDIO){
					audio();
				}
				else if(lex.currenttoken.tokenType == Type.VIDEO){
					video();
				}
				else if(lex.currenttoken.tokenType == Type.USEB){
					variableUse();
				}
				else if(lex.currenttoken.tokenType == Type.NEWLINE){
					if(verboseOutput)System.out.println("para(): n");
					newline();
				}
				else if(lex.currenttoken.tokenType == Type.BOLD){
					bold();
				}
				else if(lex.currenttoken.tokenType == Type.ITALICS){
					italics();
				}
				else{
					if(verboseOutput)System.out.println("para(): whitespace or text");
					textOrWhitespaceMultipleTimes();
				}
			}
			if(lex.currenttoken.tokenType == Type.PARAE){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.PARAE));
			}
		}
	}

	@Override
	public void innerText() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.TEXT){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.TEXT));
			}
		}
	}
	public void whiteSpace() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.WHITESPACE){
				lex.getNextToken();
			}
		}
	}
	public void whiteSpaceMultipleTimes() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			while(lex.currenttoken.tokenType == Type.WHITESPACE){
				lex.getNextToken();
			}
		}
	}
	@Override
	public void variableDefine() throws CompilerException {
		// TODO Auto-generated method stub
		//vardef		: (DEFB TEXT EQSIGN TEXT DEFUSEE)* ;
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.DEFB){
				if(verboseOutput)System.out.println("DEFB found");
				while(lex.currenttoken.tokenType == Type.DEFB){
			
					lex.getNextToken();
					whiteSpace();
					innerText();
					whiteSpace();
					
					if(!syntaxError){
						if(lex.currenttoken.tokenType == Type.EQSIGN) {
							lex.getNextToken();
						}
					}
					else{
						setErrorFlag();
						throw new CompilerException(getSyntaxErrorMessage(Type.EQSIGN));
					}
						
					whiteSpace();
					innerText();
					whiteSpace();
					
					if(!syntaxError){
						if(lex.currenttoken.tokenType == Type.DEFUSEE) {
							lex.getNextToken();
							whiteSpace();
						}
					}
					else{
						setErrorFlag();
						throw new CompilerException(getSyntaxErrorMessage(Type.DEFUSEE));
					}
				}
			}
		}
	}
	


	@Override
	public void variableUse() throws CompilerException {
		// TODO Auto-generated method stub
		//varuse		: USEB TEXT+ DEFUSEE ;
		if(!syntaxError){
			lex.getNextToken();
			if(lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.WHITESPACE){
				textOrWhitespaceMultipleTimes();
				
			}
			if(lex.currenttoken.tokenType == Type.DEFUSEE){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.DEFUSEE));
			}
		}
		
	
	}
	@Override
	public void italics() throws CompilerException {
		// TODO Auto-generated method stub
		
		if(!syntaxError){
			lex.getNextToken();
			if(lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.WHITESPACE){
				textOrWhitespaceMultipleTimes();
			}
			else{
					setErrorFlag();
					throw new CompilerException(getSyntaxErrorMessage(Type.TEXT));
			}
			
			if(lex.currenttoken.tokenType == Type.ITALICS){
					lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.ITALICS));
			}
		}
	}
	
	//listitem	: LISTITEMB (TEXT | varuse)+ LISTITEME ; */
	
	@Override
	public void listitem() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			lex.getNextToken();
			if(lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.WHITESPACE ||
				lex.currenttoken.tokenType == Type.USEB) {
				
				while(lex.currenttoken.tokenType == Type.TEXT ||
						lex.currenttoken.tokenType == Type.WHITESPACE ||
						lex.currenttoken.tokenType == Type.USEB) {
					
					if(lex.currenttoken.tokenType == Type.TEXT ||
							lex.currenttoken.tokenType == Type.WHITESPACE ){
						textOrWhitespaceMultipleTimes();
					}
					else if(lex.currenttoken.tokenType == Type.USEB){
						variableUse();
					}
				}
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.DEFUSEE));
			}
			if(lex.currenttoken.tokenType == Type.LISTITEME){
				lex.getNextToken();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.DEFUSEE));
			}
			
		}

	}
	//used in list item
	//var use, bold , italics, link, text
	@Override
	public void innerItem() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.USEB){
				variableUse();
			}
			else if(lex.currenttoken.tokenType == Type.BOLD){
				bold();
			}
			else if(lex.currenttoken.tokenType == Type.ITALICS){
				italics();
			}
			else if(lex.currenttoken.tokenType == Type.LINKB){
				link();
			}
			else if(lex.currenttoken.tokenType == Type.TEXT ||
					lex.currenttoken.tokenType == Type.WHITESPACE){
				textOrWhitespaceMultipleTimes();
			}
			else{
				
			}
		}	
	}

	@Override
	public void link() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			lex.getNextToken();
			if(lex.currenttoken.tokenType == Type.TEXT ||
					lex.currenttoken.tokenType == Type.WHITESPACE){
				textOrWhitespaceMultipleTimes();
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.TEXT));
			}
			if(lex.currenttoken.tokenType == Type.LINKE){
				lex.getNextToken();
				
			}
			else{
				setErrorFlag();
				throw new CompilerException(getSyntaxErrorMessage(Type.LINKE));
			}
			address();
			
		}
	}
	public void address() throws CompilerException {
		// TODO Auto-generated method stub
		if(lex.currenttoken.tokenType == Type.ADDRESSB){
			lex.getNextToken();
			
		}
		else{
			setErrorFlag();
			throw new CompilerException(getSyntaxErrorMessage(Type.ADDRESSB));
		}
		if(lex.currenttoken.tokenType == Type.TEXT ||
				lex.currenttoken.tokenType == Type.WHITESPACE){
			textOrWhitespaceMultipleTimes();
		}
		else{
			setErrorFlag();
			throw new CompilerException(getSyntaxErrorMessage(Type.TEXT));
		}
		if(lex.currenttoken.tokenType == Type.ADDRESSE){
			lex.getNextToken();
			
		}
		else{
			setErrorFlag();
			throw new CompilerException(getSyntaxErrorMessage(Type.ADDRESSE));
		}
	}
	@Override
	public void audio() throws CompilerException {
		// TODO Auto-generated method stub
		lex.getNextToken();
		address();
	}

	@Override
	public void video() throws CompilerException {
		// TODO Auto-generated method stub
		lex.getNextToken();
		address();

	}

	@Override
	public void newline() throws CompilerException {
		// TODO Auto-generated method stub
		lex.getNextToken();
	}
	
	public String getSyntaxErrorMessage(Type missingToken){
		int errorPosition = lex.currentPosition;
		String errorPositionIndicator = "";
		
		String errorArea = lex.sourceFile.substring(errorPosition - 30 < 0 ? 0 : errorPosition - 30, 
				errorPosition + 30 <  lex.sourceFile.length() ? errorPosition + 30 : lex.sourceFile.length()) ;
		
		String errorMessage = "Syntax Error - " + missingToken.toString() + " was expected and not found.";
		
		errorMessage += "\nCurrent Token - " + lex.currenttoken.content;
		
		return "\n" + errorArea + "\n" + errorMessage;
	}
	
	public void setErrorFlag(){
		syntaxError = true;
	}
	
	public void resetErrorFlag(){
		syntaxError = false;
	}
}
