package edu.towson.cis.cosc455.rob.project1.implementation;

public class SyntaxAnalyzer implements edu.towson.cis.cosc455.rob.project1.interfaces.SyntaxAnalyzer {
	
	
	@Override
	public void markdown() throws CompilerException {
		if(Compiler.currentToken.equalsIgnoreCase(Tokens.DOCB)){
			
		}
		else{
			//do error stuffs
		}
	}

	@Override
	public void head() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void title() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void body() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void paragraph() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void innerText() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void variableDefine() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void variableUse() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void bold() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void italics() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void listitem() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void innerItem() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void link() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void audio() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void video() throws CompilerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void newline() throws CompilerException {
		// TODO Auto-generated method stub

	}

}
