package net.starkus.mipsstudio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InstructionOneReg extends Instruction {
	
	private final BooleanProperty RSFloat = new SimpleBooleanProperty(false);
	protected Register RS;
	
	
	public InstructionOneReg(String name, int word)
	{
		super(name, word);

		RS = new Register((word >>> 21) & 31, RSFloatProperty());
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s", getName(), RS);
	}


	public Register getRS()
	{
		return RS;
	}

	public void setRS(Register rS)
	{
		RS = rS;
	}
	
	public InstructionOneReg setRSFloat(boolean value)
	{
		RSFloat.set(value);
		return this;
	}
	
	public boolean isRSFloat()
	{
		return RSFloat.get();
	}

	public BooleanProperty RSFloatProperty()
	{
		return RSFloat;
	}
}
