package net.starkus.mipsstudio.model;

public class InstructionI extends InstructionTwoReg {
	
	protected int imm;
	
	public InstructionI(String name, int word)
	{
		super(name, word);
		
		imm = word & 0xFFFF;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s, $%04X", getName(), RT, RS, imm);
	}
}
