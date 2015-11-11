package edu.towson.cis.cosc455.rob.project1.implementation;

import MarkdownTokenDefinitions.TokenDefinitions;
import MarkdownTokenDefinitions.TokenDefinitions.Type;


/**
 * @author lt
 *	This class requests a token from the lexical analyzer and determines whether that token is using
 *correct syntax according to the Markdown grammar rules
 */
public class SyntaxAnalyzer implements edu.towson.cis.cosc455.rob.project1.interfaces.SyntaxAnalyzer {
	/**Flag used by SyntaxAnalyzer to determine if Syntax Error has occurred */
	public boolean syntaxError;
	/**Turns debugging information from the compiler on or off*/
	public boolean verboseOutput;
	/**Token currently in use by the Syntax ANalyzer */
	public Token currentToken;
	/**Local reference for the lexical analyzer */
	public LexicalAnalyzer lex;
	/**Local reference for the Markdown definitions */
	public TokenDefinitions def;
	/**String used for reporting error messages*/
	public String syntaxErrorMessage;
	
	
	
	/**
	 * This constructor initializes the Syntax Analyzer 
	 * @throws CompilerException
	 */
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
		if(!syntaxError){
			doc_begin();
			code();
			doc_end();
		}
	}
	
	/**
	 * This method checks that the "document begin" tag is present and in the correct location
	 * @throws CompilerException
	 */
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
	
	/**
	 * This method checks that the "document end" tag is present and in the correct location
	 * @throws CompilerException
	 */
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
	
	/**
	 * This method checks for any markdown tokens inside the document's beginning and ending tags
	 * @throws CompilerException
	 */
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
	/**
	 * This method checks for the presence of text or whitespace nodes and if they are present,
	 * cycles through them
	 * @throws CompilerException
	 */
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
	/**
	 * This method checks to see if whitespace is present, and if so,
	 * cycles past it.
	 * @throws CompilerException
	 */
	public void whiteSpace() throws CompilerException {
		// TODO Auto-generated method stub
		if(!syntaxError){
			if(lex.currenttoken.tokenType == Type.WHITESPACE){
				lex.getNextToken();
			}
		}
	}
	/**
	 * THis method is used to bypass multiple white space tokens
	 * @throws CompilerException
	 */
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
	/**
	 * This method implements the BNF grammar rule for a web http address annotation.
	 * @throws CompilerException
	 */
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
	
	/**
	 * This method constructs an error message based on the exception generated during syntax analysis
	 * @param missingToken
	 * @return A string containing relevant information about the syntax error
	 */
	public String getSyntaxErrorMessage(Type missingToken){
		/** Used to store the exact position of where the exception occurred*/
		int errorPosition = lex.currentPosition;
		
		String errorArea = lex.sourceFile.substring(errorPosition - 30 < 0 ? 0 : errorPosition - 30, 
				errorPosition + 30 <  lex.sourceFile.length() ? errorPosition + 30 : lex.sourceFile.length()) ;
		
		String errorMessage = "Syntax Error - " + missingToken.toString() + " was expected and not found.";
		
		errorMessage += "\nCurrent Token - " + lex.currenttoken.content;
		
		return "\n" + errorArea + "\n" + errorMessage;
	}
	
	/**
	 * This method sets the Syntax Analyzers Error Flag to true
	 */
	public void setErrorFlag(){
		syntaxError = true;
	}
	/**
	 * This method sets the Syntax Analyzers Error Flag to false
	 */
	public void resetErrorFlag(){
		syntaxError = false;
	}
}
