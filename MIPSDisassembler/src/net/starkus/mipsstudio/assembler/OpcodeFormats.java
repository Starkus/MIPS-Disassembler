package net.starkus.mipsstudio.assembler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.starkus.mipsstudio.MainApp;

public class OpcodeFormats {

	private static String formatsFile;
	private static Map<String, Format> opcodeToFormat;
	
	static{
		try
		{
			formatsFile = MainApp.getResourceAsString("formats.txt");
			opcodeToFormat = new HashMap<>();

			Format currentFormat = null;
			
			for (String line : formatsFile.split("\n"))
			{
				line = line.replace("\r", "");

				if (line.endsWith(":"))
				{
					Format f = Format.valueOf(line.substring(0, line.length()-1));
					currentFormat = f;
				}
				else
				{
					opcodeToFormat.put(line, currentFormat);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} 
	}
	
	public static Format getFormat(String opcode)
	{		
		if (!opcodeToFormat.containsKey(opcode))
		{
			throw new NullPointerException("Missing " + opcode + " in formats.txt");
		}
		return opcodeToFormat.get(opcode);
	}
	
	
	public enum Format
	{
		R, I, J, B, BZ, SHIFT, OFS, OFSCOMP, ONEREG, TWOREG, RD, REGI, 
		FPR, FPOFS, FPCOMP, FPTWOREG, MTFP, 
		TRAP, TRAPI, 
		V;
	}
}
