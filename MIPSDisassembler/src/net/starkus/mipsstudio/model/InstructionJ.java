package net.starkus.mipsstudio.model;

public class InstructionJ extends Instruction {

	protected int targetAddress;
	
	
	public InstructionJ(String name, int word)
	{
		super(name, word);
		
		targetAddress = (word & 0x3FFFFFF) << 2;
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s $%08X", getName(), targetAddress);
	}

}
