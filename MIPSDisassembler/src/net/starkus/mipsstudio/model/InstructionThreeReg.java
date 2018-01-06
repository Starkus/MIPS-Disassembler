package net.starkus.mipsstudio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InstructionThreeReg extends InstructionTwoReg {
	
	private final BooleanProperty RDFloat = new SimpleBooleanProperty(false);
	protected Register RD;
	
	
	public InstructionThreeReg(String name, int word)
	{
		super(name, word);

		RD = new Register((word >>> 11) & 31, RDFloatProperty());
	}
	
	
	@Override
	public String makeString(int programCounter)
	{
		return String.format("%s %s, %s, %s", getName(), RD, RS, RT);
	}
	

	public Register getRD()
	{
		return RD;
	}

	public void setRD(Register rD)
	{
		RD = rD;
	}
	
	public InstructionTwoReg setRDFloat(boolean value)
	{
		RDFloat.set(value);
		
		return this;
	}
	
	public boolean isRDFloat()
	{
		return RDFloat.get();
	}
	
	public BooleanProperty RDFloatProperty()
	{
		return RDFloat;
	}
}
