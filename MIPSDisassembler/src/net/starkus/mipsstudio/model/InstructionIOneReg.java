package net.starkus.mipsstudio.model;

public class InstructionIOneReg extends InstructionOneReg {
	
	protected int imm;
	
	public InstructionIOneReg(String name, int word)
	{
		super(name, word);
		
		imm = word & 0xFFFF;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, $%04X", getName(), RS, imm);
	}
}
