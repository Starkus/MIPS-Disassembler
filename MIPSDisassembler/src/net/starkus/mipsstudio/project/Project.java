package net.starkus.mipsstudio.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Project {

	File rootFolder;
	String name;
	
	ProjectFile projectFile;
	File ROMFile;
	
	
	public Project(String name, File rootFolder){
		
		/*if (!rootFolder.isDirectory())
		{
			throw new IllegalArgumentException("rootFolder is not a directory!");
		}*/
		
		this.name = name;
		this.rootFolder = rootFolder;
		
		projectFile = new ProjectFile(rootFolder, name + ProjectFile.EXTENSION);
	}
	
	public boolean createProject() throws IOException
	{
		if (!rootFolder.exists())
			if (!rootFolder.mkdirs())
				return false;
		
		if (projectFile.exists())
		{
			throw new IOException("Project already exists");
		}
		
		if (!projectFile.createNewFile())
			return false;
		
		
		
		return true;
	}
	
	public void importROMFile(File source) throws IOException
	{
		File newFile = new File(rootFolder, source.getName());
		newFile.createNewFile();
		
		FileInputStream is = new FileInputStream(source);
		FileOutputStream os = new FileOutputStream(newFile);
		
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		
		try
		{
			sourceChannel = is.getChannel();
			destChannel = os.getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
		}
		finally
		{
			sourceChannel.close();
			destChannel.close();
			
			is.close();
			os.close();
		}
	}
	
	
	public File getRootFolder()
	{
		return rootFolder;
	}

	public void setRootFolder(File rootFolder)
	{
		this.rootFolder = rootFolder;
	}

	public File getROMFile()
	{
		return ROMFile;
	}

	public void setROMFile(File rOMFile)
	{
		ROMFile = rOMFile;
	}
}
