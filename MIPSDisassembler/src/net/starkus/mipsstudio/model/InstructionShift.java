package net.starkus.mipsstudio.model;

public class InstructionShift extends InstructionR {
	
	protected int shiftAmmount;

	public InstructionShift(String name, int word)
	{
		super(name, word);
		
		shiftAmmount = (word >>> 6) & 31;
	}

}
