package net.starkus.mipsstudio.project;

import java.io.File;

public class ProjectFile extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static final String EXTENSION = ".msp";
	
	public String projectName;

	
	public ProjectFile(File parent, String child)
	{
		super(parent, child);
	}
	
	public ProjectFile(String pathname)
	{
		super(pathname);
	}
}
