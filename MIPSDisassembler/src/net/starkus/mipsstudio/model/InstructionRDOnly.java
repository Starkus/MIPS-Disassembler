package net.starkus.mipsstudio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class InstructionRDOnly extends Instruction {
	
	private final BooleanProperty RDFloat = new SimpleBooleanProperty(false);
	protected Register RD;
	
	
	public InstructionRDOnly(String name, int word)
	{
		super(name, word);

		RD = new Register((word >>> 11) & 31, RDFloatProperty());
	}
	

	public Register getRD()
	{
		return RD;
	}

	public void setRD(Register rD)
	{
		RD = rD;
	}
	
	public Instruction setRDFloat(boolean value)
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
