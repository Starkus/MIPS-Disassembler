package net.starkus.mipsstudio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;

public class Register {
	
	private static final String[] registerNames = new String[] {
		"R0", "AT", "V0", "V1", "A0", "A1", "A2", "A3",
		"T0", "T1", "T2", "T3", "T4", "T5", "T6", "T7",
		"S0", "S1", "S2", "S3", "S4", "S5", "S6", "S7",
		"T8", "T9", "K0", "K1", "GP", "SP", "FP", "RA"
	};
	
	
	public final int id;
	private final ReadOnlyBooleanProperty isFPUProp;
	
	public Register(int id, BooleanProperty fpu)
	{
		this.id = id;
		isFPUProp = BooleanProperty.readOnlyBooleanProperty(fpu);
	}
	
	public boolean isFPU()
	{
		return isFPUProp.get();
	}
	
	public ReadOnlyBooleanProperty isFPUProperty()
	{
		return isFPUProp;
	}

	@Override
	public String toString()
	{
		if (isFPUProp.get())
			return String.format("F%d", id);
		
		return registerNames[id];
	}
}
