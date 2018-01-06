package net.starkus.mipsstudio.model;

public class InstructionTrapI extends InstructionOneReg {
	
	protected int imm;

	public InstructionTrapI(String name, int word)
	{
		super(name, word);
		
		imm = word & 0xFFFF;
	}
	
	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s R%d, $%04X", getName(), RS, imm);
	}
}
