package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.List;

public abstract class InstructionSpecial extends Instruction {
	

	private static List<String> functions;

	static
	{
		try
		{
			functions = txtToList("functions.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public InstructionSpecial(String name, int word)
	{
		super(name, word);
		// TODO Auto-generated constructor stub
	}

	public static Instruction fromWord(int word)
	{
		int function = word & 63;
		
		String functionPnem = functions.get(function);

		switch (functionPnem)
		{
		case "MOVCI":
			if (((word >>> 16) & 1) == 0)
				return new InstructionMoveConditional("MOVF", word);
			else
				return new InstructionMoveConditional("MOVT", word);
		
		
		case "SYSCALL": case "BREAK": case "SYNC":
			return new Instruction(functionPnem, word);
		
		
		case "SLL": case "SRL": case "SRA": case "SLLV": case "SRLV": case "SRAV":
		case "DSLL": case "SDLL32": case "DSRL": case "DSRL32": case "DSRA": case "DSRA32":
			return new InstructionShift(functionPnem, word);
			
			
		case "JR": case "JALR": case "MTHI": case "MTLO":
			return new InstructionOneReg(functionPnem, word);
			
			
		case "MFHI": case "MFLO":
			return new InstructionRDOnly(functionPnem, word);
			
			
		case "MULT": case "MULTU": case "DIV": case "DIVU": case "DMULT": case "DMULTU": case "DDIV": case "DDIVU":
			return new InstructionTwoReg(functionPnem, word);
			
			
		case "MOVZ": case "MOVN":
		case "DSLLV": case "DSRLV": case "DSRAV":
		case "ADD": case "ADDU": case "SUB": case "SUBU": case "AND": case "OR": case "XOR": case "NOR": 
		case "SLT": case "SLTU": case "DADD": case "DADDU": case "DSUB": case "DSUBU":
			return new InstructionR(functionPnem, word);

			
		case "TGE": case "TGEU": case "TLT": case "TLTU": case "TEQ": case "TNE":
			return new InstructionTrap(functionPnem, word);
			

		default:
			return null;
		}
	}

}
