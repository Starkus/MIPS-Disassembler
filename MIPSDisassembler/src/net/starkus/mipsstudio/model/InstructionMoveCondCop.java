package net.starkus.mipsstudio.model;

public class InstructionMoveCondCop extends InstructionTwoRegCop {

	protected int cc;
	
	public InstructionMoveCondCop(String name, int word)
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
