package net.starkus.mipsstudio.model;

public class InstructionMoveConditional extends InstructionThreeReg {
	
	int cc;
	
	public InstructionMoveConditional(String name, int word)
	{
		super(name, word);
		
		cc = (word >>> 18) & 7;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s, %X", getName(), RD, RS, cc);
	}
}
