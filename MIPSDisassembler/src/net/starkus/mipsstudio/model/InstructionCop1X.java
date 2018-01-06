package net.starkus.mipsstudio.model;

public abstract class InstructionCop1X extends Instruction {

	public static Instruction fromWord(int word)
	{	
		return null;
	}

	public InstructionCop1X(String name, int word)
	{
		super(name, word);
	}
}
