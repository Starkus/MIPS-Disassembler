package net.starkus.mipsstudio.model;

import java.io.IOException;
import java.util.List;

public class InstructionCop1 extends Instruction {
	

	private static List<String> functions, fmts;
	private static String[] BCs = new String[] {
			"BC1F", "BC1T", "BC1FL", "BC1TL"
	};

	static
	{
		try
		{
			functions = txtToList("cop1func.txt");
			fmts = txtToList("cop1fmt.txt");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public InstructionCop1(String name, int word)
	{
		super(name, word);
	}

	public static Instruction fromWord(int word)
	{
		int function = word & 63;
		String functionPnem = functions.get(function);
		
		int fmt = (word >>> 21) & 31;
		String fmtPnem = fmts.get(fmt);
		
		String compoundName = String.format("%s.%s", functionPnem, fmtPnem);
		
		
		switch (fmtPnem)
		{
		case "BC":
			String BC = BCs[(word >>> 16) & 3];
			
			return new InstructionBranchCop(BC, word);
		
		
		case "MFC1": case "MTC1": case "DMFC1": case "DMTC1": case "CFC1": case "CTC1":
			return new InstructionTwoReg(fmtPnem, word) {
				@Override
				public String makeString(int programCounter)
				{
					return String.format("%s %s, %s", getName(), RT, RS);
				}
			}.setRTFloat(false).setRSFloat(true);
			

		default:
			break;
		}
		
		
		switch (functionPnem)
		{
		case "MOVN": case "MOVZ":
			return new InstructionRCop(compoundName, word).setRTFloat(false);
			
		case "ADD": case "SUB": case "MUL": case "DIV":
			return new InstructionRCop(compoundName, word);
			
			
		case "ABS": case "CEIL.L": case "CEIL.W": case "CVT.D": case "CVT.L": case "CVT.S":
		case "CVT.W": case "FLOOR.L": case "FLOOR.W": case "MOV": case "NEG": case "RECIP":
		case "ROUND.L": case "ROUND.W": case "RSQRT": case "SQRT": case "TRUNC.L": case "TRUNC.W":
			return new InstructionTwoRegCop(compoundName, word).setRDFloat(true).setRSFloat(true);
			
			
		case "MOVF":
			return new InstructionMoveCondCop(compoundName, fmt);
			

		default:
			return null;
		}
	}

}
