package net.starkus.mipsstudio.model;

public class InstructionRCop extends InstructionR {

	public InstructionRCop(String name, int word)
	{
		super(name, word);
		
		setRSFloat(true);
		setRTFloat(true);
		setRDFloat(true);
		
		setRS(new Register(RD.id, RSFloatProperty()));
		setRD(new Register((word >>> 6) & 31, RDFloatProperty()));
	}

}
