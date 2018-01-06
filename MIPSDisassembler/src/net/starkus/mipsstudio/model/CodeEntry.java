package net.starkus.mipsstudio.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CodeEntry {

	private final IntegerProperty address = new SimpleIntegerProperty();
	private final IntegerProperty hex = new SimpleIntegerProperty();
	private final ObjectProperty<Instruction> instruction = new SimpleObjectProperty<>();
	private final ReadOnlyStringWrapper assembly = new ReadOnlyStringWrapper();
	
	
	public CodeEntry()
	{
		instruction.addListener((obs, oldv, newv) -> assembly.set(newv.makeString((int) address.get())));
		address.addListener((obs, oldv, newv) -> assembly.set(instruction.get().makeString((int) newv)));
	}
	
	public CodeEntry(int address)
	{
		this.address.set(address);
	}
	
	public CodeEntry(int address, int hex, Instruction instruction)
	{
		this.address.set(address);
		this.hex.set(hex);
		this.instruction.set(instruction);

		this.instruction.addListener((obs, oldv, newv) -> {
			updateAssembly(newv, address);
		});
		this.address.addListener((obs, oldv, newv) -> {
			updateAssembly(instruction, newv.intValue());
		});
		
		updateAssembly(instruction, address);
	}
	
	
	private void updateAssembly(Instruction instruction, int address)
	{
		if (instruction != null)
			assembly.set(instruction.makeString(address));
		else
			assembly.set("?");
	}

	
	public int getAddress()
	{
		return address.get();
	}
	public void setAddress(int value)
	{
		address.set(value);
	}
	public IntegerProperty addressProperty()
	{
		return address;
	}
	
	public int getHex()
	{
		return hex.get();
	}
	public void setHex(int value)
	{
		hex.set(value);
	}
	public IntegerProperty hexProperty()
	{
		return hex;
	}
	
	public Instruction getInstruction()
	{
		return instruction.get();
	}
	public void setInstruction(Instruction value)
	{
		instruction.set(value);
	}
	public ObjectProperty<Instruction> instructionProperty()
	{
		return instruction;
	}
	
	public String getAssembly()
	{
		return assembly.get();
	}
	public ReadOnlyStringProperty assemblyProperty()
	{
		return assembly.getReadOnlyProperty();
	}
}
