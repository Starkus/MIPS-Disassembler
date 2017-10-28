package net.starkus.mipsstudio.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CodeEntry {

	private final LongProperty address = new SimpleLongProperty();
	private final LongProperty hex = new SimpleLongProperty();
	private final StringProperty assembly = new SimpleStringProperty();
	
	
	public CodeEntry()
	{
		
	}
	
	public CodeEntry(long address, long hex, String assembly)
	{
		this.address.set(address);
		this.hex.set(hex);
		this.assembly.set(assembly);
	}

	
	public long getAddress()
	{
		return address.get();
	}
	public void setAddress(long value)
	{
		address.set(value);
	}
	public LongProperty addressProperty()
	{
		return address;
	}
	
	public long getHex()
	{
		return hex.get();
	}
	public void setHex(int value)
	{
		hex.set(value);
	}
	public LongProperty hexProperty()
	{
		return hex;
	}
	
	public String getAssembly()
	{
		return assembly.get();
	}
	public void setAssembly(String value)
	{
		assembly.set(value);
	}
	public StringProperty assemblyProperty()
	{
		return assembly;
	}
}
