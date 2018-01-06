package net.starkus.mipsstudio.model;

public class InstructionBaseOffset extends InstructionTwoReg {
	
	protected int base, offset;
	
	public InstructionBaseOffset(String name, int word)
	{
		super(name, word);
		
		base = word >>> 21 & 0b11111;
		offset = word & 0xFFFF;
	}

}
