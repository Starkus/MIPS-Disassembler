package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.starkus.mipsstudio.MainApp;

public class Instruction {
	
	
	private String name;
	protected int word;
	
	
	private static List<String> opcodes;

	static
	{
		try
		{
			opcodes = txtToList("opcodes.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public Instruction(String name, int word)
	{
		setName(name);
		this.word = word;
	}
	

	public static Instruction fromWord(int word)
	{
		int opcode = word >>> 26;
		if (opcode >= opcodes.size())
			return null;
		String opcodePnem = opcodes.get(opcode);
		
		if (word == 0)
			return new Instruction("NOP", word);
		
		switch (opcodePnem)
		{
		case "SPECIAL":
			return InstructionSpecial.fromWord(word);
			
		case "REGIMM":
			return InstructionRegimm.fromWord(word);
			
		case "COP0":
			return InstructionCop0.fromWord(word);
			
		case "COP1":
			return InstructionCop1.fromWord(word);
			
		case "COP2":
			return InstructionCop2.fromWord(word);
			
		case "COP1X":
			return InstructionCop1X.fromWord(word);
			
			
		case "J": case "JAL":
			return new InstructionJ(opcodePnem, word);

		case "BEQ": case "BNE":
		case "BEQL": case "BNEL":
			return new InstructionBranch2(opcodePnem, word);

		case "BLEZ": case "BGTZ":
		case "BLEZL": case "BGTZL":
			return new InstructionBranch(opcodePnem, word);
			
			
		case "ADDI": case "ADDIU": case "SLTI": case "SLTIU": case "ANDI": case "ORI": case "XORI":
		case "DADDI": case "DADDIU":
			return new InstructionI(opcodePnem, word);
			
		case "LUI":
			return new InstructionIOneReg(opcodePnem, word);
			

		case "LDL": case "LDR":
		case "LB": case "LH": case "LWL": case "LW": case "LBU": case "LHU": case "LWR": case "LWU":
		case "SB": case "SH": case "SWL": case "SW": case "SDL": case "SDR": case "SWR":
		case "LL": case "SC": case "LLD": case "SCD": case "LD": case "SD":
			return new InstructionBaseOffset(opcodePnem, word);
		
		case "LWC0": case "LWC1": case "LWC2": case "LWC1X":
		case "SWC0": case "SWC1": case "SWC2": case "SWC1X":
		case "LDC1": case "LDC2": case "SDC1": case "SDC2":
			return new InstructionBaseOffset(opcodePnem, word).setRTFloat(true);
			
			
		case "CACHE":
			return new Instruction(opcodePnem, word);
			

		default:
			return null;
		}
	}
	
	
	public String makeString(int programCounter)
	{
		return getName();
	}
	
	
	public String getName()
	{
		return name.toUpperCase();
	}
	
	public void setName(String name)
	{
		this.name = name.toUpperCase();
	}


	

	protected static List<String> txtToList(String filename) throws IOException
	{
		String[] lines = MainApp.getResourceAsString(filename).split("\n");
		List<String> list = new ArrayList<>();

		for (String l : lines)
		{
			list.add(l.replace("\r", ""));
		}

		return list;
	}
}
