package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.List;

public abstract class InstructionCop2 extends Instruction {
	

	private static List<String> functions;

	static
	{
		try
		{
			functions = txtToList("cop2func.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public InstructionCop2(String name, int word)
	{
		super(name, word);
	}

	public static Instruction fromWord(int word)
	{
		return null;
		/*int function = word & 63;
		String functionPnem = functions.get(function);
		
		switch (functionPnem)
		{
		case "":

		default:
			return null;
		}*/
	}
}
