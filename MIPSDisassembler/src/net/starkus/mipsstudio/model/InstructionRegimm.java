package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.List;

public abstract class InstructionRegimm extends Instruction {


	private static List<String> regimms;

	static
	{
		try
		{
			regimms = txtToList("regimms.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	
	public InstructionRegimm(String name, int word)
	{
		super(name, word);
	}
	

	public static Instruction fromWord(int word)
	{
		int regimm = (word >>> 16) & 31;
		if (regimm >= regimms.size())
			return null;
		String regimmPnem = regimms.get(regimm);
		
		switch (regimmPnem)
		{
		case "BLTZ": case "BGEZ": case "BLTZL": case "BGEZL":
		case "BLTZAL": case "BGEZAL": case "BLTZALL": case "BGEZALL":
			return new InstructionBranch(regimmPnem, word);
			
			
		case "TGEI": case "TGEIU": case "TLTI": case "TLTIU": case "TEQI": case "TNEI":
			return new InstructionTrap(regimmPnem, word);
			

		default:
			return null;
		}
	}

}
