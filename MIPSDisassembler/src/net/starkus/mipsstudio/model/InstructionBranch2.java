package net.starkus.mipsstudio.model;

public class InstructionBranch2 extends InstructionTwoReg {
	
	protected int offset;
	
	public InstructionBranch2(String name, int word)
	{
		super(name, word);
		
		offset = (short) (1 + (word & 0xFFFF)) << 2;
	}
	
	int makeAbsoluteAddress(int programCounter)
	{
		return offset + (programCounter << 2);
	}
	
	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s, $%08X", getName(), RS, RT, makeAbsoluteAddress(programCounter));
	}
}
