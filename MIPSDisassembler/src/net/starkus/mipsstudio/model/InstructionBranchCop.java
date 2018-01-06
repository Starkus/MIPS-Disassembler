package net.starkus.mipsstudio.model;

public class InstructionBranchCop extends Instruction {
	
	protected int cc, offset;
	
	public InstructionBranchCop(String name, int word)
	{
		super(name, word);
		
		cc = (word >>> 18) & 7;
		offset = (short) (1 + (word & 0xFFFF)) << 2;
	}
	
	int makeAbsoluteAddress(int programCounter)
	{
		return offset + (programCounter << 2);
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %X, $%08X", getName(), cc, makeAbsoluteAddress(programCounter));
	}
}
