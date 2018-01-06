package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.List;

public abstract class InstructionCop0 extends Instruction {
	

	private static List<String> functions, fmts;

	static
	{
		try
		{
			functions = txtToList("cop0func.txt");
			fmts = txtToList("cop0fmt.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public InstructionCop0(String name, int word)
	{
		super(name, word);
	}

	public static Instruction fromWord(int word)
	{
		int function = word & 63;
		if (function >= functions.size())
			return null;
		String functionPnem = functions.get(function);
		
		int fmt = (word >>> 21) & 31;
		if (fmt >= fmts.size())
			return null;
		String fmtPnem = fmts.get(fmt);
		
		switch (fmtPnem)
		{
		case "MFC0": case "MTC0":
			return new InstructionTwoReg(fmtPnem, word) {
				@Override
				public String makeString(int programCounter)
				{
					return String.format("%s %s, %s", getName(), RT, RS);
				}
			}.setRTFloat(false).setRSFloat(true);
		}
		
		switch (functionPnem)
		{
		case "TLBR": case "TLBWI": case "TLBWR": case "TLBP": case "ERET":
			return new Instruction(functionPnem, word);

			
		default:
			return null;
		}
	}
}
