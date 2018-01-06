package net.starkus.mipsstudio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InstructionTwoReg extends InstructionOneReg {
	
	private final BooleanProperty RTFloat = new SimpleBooleanProperty(false);
	protected Register RT;
	
	
	public InstructionTwoReg(String name, int word)
	{
		super(name, word);

		RT = new Register((word >>> 16) & 31, RTFloatProperty());
	}

	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s", getName(), RS, RT);
	}


	public Register getRT()
	{
		return RT;
	}

	public void setRT(Register rT)
	{
		RT = rT;
	}
	
	public InstructionTwoReg setRTFloat(boolean value)
	{
		RTFloat.set(value);
		return this;
	}
	
	public boolean isRTFloat()
	{
		return RTFloat.get();
	}

	public BooleanProperty RTFloatProperty()
	{
		return RTFloat;
	}
}
