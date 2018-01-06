package net.starkus.mipsstudio.assembler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InvalidNameException;

import net.starkus.mipsstudio.assembler.OpcodeFormats.Format;

public class Instructions {

	private static List<String> opcodes, functions, regimm, cop1fmt, cop1func,
			copx, cop2func, cop0fmt, cop0func, regnames;

	@Deprecated
	public static String parseInstruction(int word, long programCounter) throws InvalidNameException
	{
		String opcode = identifyOpcode(word);
		String args = identifyArguments(opcode, word, programCounter);
		args = replaceRegisterNames(args);
		return opcode + " " + args;
	}

	private static String identifyOpcode(int word) throws InvalidNameException
	{
		if (word == 0)
			return "NOP";

		String inst = "";
		String opcode, function, fmt;
		
		int opcoden = word >>> 26;
		
		if (opcoden >= opcodes.size() || opcodes.get(opcoden).isEmpty())
			throw new InvalidNameException("Invalid opcode index");
		
		opcode = opcodes.get(opcoden);
		
		inst = opcode;

		int f;
		
		switch (opcode)
		{
		case "SPECIAL":
			f = word & 0x3F;
			if (f < functions.size() && !functions.get(f).isEmpty())
			{
				function = functions.get(f);
				
				if (function.equals("MOVCI"))
				{
					function = (word & 0x10000) == 0 ? "MOVF" : "MOVT";
				}

				inst = function;
			}
			else
			{
				throw new InvalidNameException("Invalid Special: " + String.format("%X", f));
			}

			break;

		case "REGIMM":
			f = (word >>> 16) & 0x1F;
			if (f < regimm.size() && !regimm.get(f).isEmpty())
			{
				inst = regimm.get(f);
			} else
				inst = "";

			break;

		case "COP0":
			f = (word >>> 21) & 0x1F;
			if (f < cop0fmt.size() && !cop0fmt.get(f).isEmpty())
			{
				fmt = cop0fmt.get(f);
				
				if (fmt.equals("TLB"))
				{
					f = word & 0b111111;
					if (f < cop0func.size() && !cop0func.get(f).isEmpty())
					{
						function = cop0func.get(f);
						inst = function;
					}
					else
					{
						throw new InvalidNameException("Invalid COP0 function: " + Integer.toBinaryString(f));
					}
				}
				else if (!fmt.isEmpty())
				{
					inst = fmt;
				}
			}
			else
			{
				throw new InvalidNameException("Invalid COP0 fmt " + Integer.toBinaryString(f));
			}
			
			break;
			
		case "COP1":
			f = (word >>> 21) & 0x1F;
			if (f < cop1fmt.size() && !cop1fmt.get(f).isEmpty())
			{
				fmt = cop1fmt.get(f);

				if (fmt.equals("S") || fmt.equals("D"))
				{
					f = word & 0b111111;
					if (f < cop1func.size() && !cop1func.get(f).isEmpty())
					{
						function = cop1func.get(f);
						
						if (function.equals("MOVCF"))
						{
							function = (word & 0x10000) == 0 ? "MOVF" : "MOVT";
						}
						
						function += "." + fmt;
						inst = function;
					}
					else
					{
						throw new InvalidNameException("Invalid COP function." + fmt + ": " + Integer.toBinaryString(f));
					}
				}
				else if (fmt.equals("BC"))
				{
					function = (word & 0x10000) == 0 ? "BC1F" : "BC1T";
					inst = function;
				}
				else if (fmt.equals("W") || fmt.equals("L"))
				{
					function = (word & 1) == 0 ? "CVT.S." : "CVT.D.";
					inst = function + fmt;
				}
				else if (!fmt.isEmpty())
				{
					inst = fmt;
				}
				else
				{
					throw new InvalidNameException("Invalid COP1 fmt " + Integer.toBinaryString(f));
				}
			} else
				inst = "";

			break;

		case "COP2":
			f = (word & 0b111111);
			
			function = cop2func.get(f);
			inst = function;
			
			break;

		case "COP1X":
			f = (word >>> 21) & 0x1F;
			if (f < copx.size() && !copx.get(f).isEmpty())
			{
				fmt = copx.get(f);
				inst = fmt;
			} else
				inst = "!";

			break;
		}

		return inst;
	}

	private static String identifyArguments(String opcode, int word, long programCounter)
	{
		if (opcode.equals("!") || opcode.equals("?"))
			return "";

		else if (opcode.equals("NOP") || opcode.equals("SYNC"))
			return "";

		else if (opcode.equals("BREAK") || opcode.equals("SYSCALL"))
		{
			long code = word >>> 6 & 0xFFFFF;
			return String.format("$%08X", code);
		}
		else if (opcode.startsWith("MOVT."))
		{
			int cc = word >>> 18 & 0b111;
			int FS = word >>> 11 & 0b11111;
			int FD = word >>> 6  & 0b11111;
			
			return String.format("F%d, F%d, %X", FD, FS, cc);
		}
		else if (opcode.equals("MTC0") || opcode.equals("MTC1"))
		{
			int RS = word >>> 16 & 0b11111;
			int FT = word >>> 11 & 0b11111;

			return String.format("R%d, F%d", RS, FT);
		}
		else if (opcode.equals("MFC0") || opcode.equals("MFC1"))
		{
			int FS = word >>> 21 & 0b11111;
			int RT = word >>> 16 & 0b11111;

			return String.format("R%d, F%d", FS, RT);
		}
		else if (opcode.equals("MOVF"))
		{
			int RS = word >>> 21 & 0b11111;
			int cc = word >>> 18 & 0b111;
			int RD = word >>> 11 & 0b11111;
			
			return String.format("R%d, R%d, $%04X", RD, RS, cc);
		}

		String s = "";
		Format format = OpcodeFormats.getFormat(opcode);

		int RS, RT, RD, i, base;
		long offset;

		switch (format)
		{
		case R:
			RS = word >>> 21 & 0b11111;
			RT = word >>> 16 & 0b11111;
			RD = word >>> 11 & 0b11111;

			s = String.format("R%d, R%d, R%d", RD, RS, RT);
			break;

		case I:
			RS = word >>> 21 & 0b11111;
			RT = word >>> 16 & 0b11111;
			i = word & 0xFFFF;

			s = String.format("R%d, R%d, $%04X", RT, RS, i);
			break;

		case J:
			offset = (word & 0x3FFFFFF) << 2;

			s = String.format("$%08X", offset);
			break;

		case B:
			RS = word >>> 21 & 0b11111;
			RT = word >>> 16 & 0b11111;
			offset = ((short) (1 + (word & 0xFFFF)) << 2) + (programCounter << 2);

			s = String.format("R%d, R%d, $%08X", RS, RT, offset);
			break;

		case BZ:
			RS = word >>> 21 & 0b11111;
			offset = ((short) (1 + (word & 0xFFFF)) << 2) + (programCounter << 2);
			
			System.out.println(String.format("last 2 bytes: %04X, shifted left: %X, plus one: %X, "
					+ "program counter: %06X, sum: %06X", 
					word & 0xFFFF, (word & 0xFFFF) << 2, 1 + (word & 0xFFFF) << 2,
					programCounter << 2, ((1 + (word & 0xFFFF)) << 2) + (programCounter << 2)));

			s = String.format("R%d, $%08X", RS, offset);
			break;
			
		case SHIFT:
			RT = word >>> 16 & 0b11111;
			RD = word >>> 11 & 0b11111;
			int sa = word >>> 6 & 0b11111;
			
			s = String.format("R%d, R%d, %d", RD, RT, sa);
			break;

		case OFS:
			base = word >>> 21 & 0b11111;
			RT = word >>> 16 & 0b11111;
			offset = word & 0xFFFF;

			s = String.format("R%d, $%04X(R%d)", RT, offset, base);
			break;

		case OFSCOMP:
			int cc = word >>> 16 & 0b11111;
			offset = word & 0xFFFF;

			s = String.format("%02X, $%04X", cc, offset);
			break;

		case ONEREG:
			RS = word >>> 21 & 0b11111;

			s = String.format("R%d", RS);
			break;

		case TWOREG:
			RS = word >>> 21 & 0b11111;
			RT = word >>> 16 & 0b11111;

			s = String.format("R%d, R%d", RS, RT);
			break;

		case RD:
			RD = word >>> 11 & 0b11111;

			s = String.format("R%d", RD);
			break;

		case REGI:
			RT = word >>> 16 & 0b11111;
			i = word & 0xFFFF;

			s = String.format("R%d, $%04X", RT, i);
			break;

		case FPR:
			RS = word >>> 6 & 0b11111;
			RT = word >>> 16 & 0b11111;
			RD = word >>> 11 & 0b11111;

			s = String.format("F%02d, F%02d, F%02d", RS, RT, RD);
			break;

		case FPOFS:
			base = word >>> 21 & 0b11111;
			offset = word >>> 16 & 0b11111;
			RD = word >>> 6 & 0b11111;

			s = String.format("R%d, R%d(R%d)", RD, offset, base);
			break;
			
		case FPTWOREG:
			RS = word >>> 11 & 0b11111;
			RD = word >>> 6  & 0b11111;
			
			s = String.format("F%d, F%d", RD, RS);
			break;

		case FPCOMP:
			RT = word >>> 16 & 0b11111;
			RS = word >>> 11 & 0b11111;
			cc = word >>> 8 & 0b111;
			
			s = String.format("%X, F%d, F%d", cc, RS, RT);
			break;
			
		case MTFP:
			RT = word >>> 16 & 0b11111;
			RS = word >>> 11 & 0b11111;
				
			s = String.format("R%d, F%d", RT, RS);
			break;
			
		case TRAP:
			RT = word >>> 16 & 0b11111;
			RS = word >>> 11 & 0b11111;
			int code = word >>> 6 & 0x3FF;
				
			s = String.format("R%d, R%d, %X", RT, RS, code);
			break;
			
		case TRAPI:
			i = word & 0xFFFF;
			RS = word >>> 21 & 0b11111;
				
			s = String.format("R%d, $%X", RS, i);
			break;
			
		case V:
			int VReg3 = word >>> 16 & 0b11111;
			int VReg2 = word >>> 10 & 0b11111;
			int VReg1 = word >>> 6  & 0b11111;

			s = String.format("F%d, F%d, F%d", VReg1, VReg2, VReg3);
			break;

		default:
			break;
		}

		return s;
	}
	
	private static String replaceRegisterNames(String inst)
	{
		String result = inst;
		
		Matcher m = Pattern.compile("R[\\d]+").matcher(inst);
		while (m.find())
		{
			String reg = inst.substring(m.start(), m.end());
			
			int regI = Integer.parseInt(reg.substring(1));
			String regName = regnames.get(regI);
			
			result = result.replaceAll(reg, regName);
		}
		
		return result;
	}
}
