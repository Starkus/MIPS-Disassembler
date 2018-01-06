package net.starkus.mipsstudio.model;

public class InstructionR extends InstructionThreeReg {
	
	
	public InstructionR(String name, int word)
	{
		super(name, word);
	}


	/*@Override
	public String makeString(int programCounter)
	{
		return String.format("%s R%d, R%d, R%d", getName(), RD, RS, RT);
	}*/
}
