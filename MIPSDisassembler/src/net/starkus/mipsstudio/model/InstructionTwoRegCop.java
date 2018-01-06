package net.starkus.mipsstudio.model;

public class InstructionTwoRegCop extends InstructionThreeReg {

	public InstructionTwoRegCop(String name, int word)
	{
		super(name, word);
		
		RS = new Register(RD.id, RSFloatProperty());
		RD = new Register((word >>> 6) & 31, RDFloatProperty());
		RT = null;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s", getName(), RD, RS);
	}
}
