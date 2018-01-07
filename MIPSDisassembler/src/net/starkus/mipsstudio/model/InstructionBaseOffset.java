package net.starkus.mipsstudio.model;

public class InstructionBaseOffset extends InstructionTwoReg {
	
	protected int offset;
	
	public InstructionBaseOffset(String name, int word)
	{
		super(name, word);
		
		//base = word >>> 21 & 0b11111;
		offset = word & 0xFFFF;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, $%04X(%s)", getName(), RT, offset, RS);
	}
}
