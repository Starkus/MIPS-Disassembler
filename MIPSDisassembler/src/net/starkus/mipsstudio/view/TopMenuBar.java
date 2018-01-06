package net.starkus.mipsstudio.view;

import java.io.File;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import net.starkus.appify.files.FileMenu;
import net.starkus.mipsstudio.MainApp;

public class TopMenuBar extends MenuBar
{
	private FileMenu<File> fileMenu;

	private MenuItem importROMCmd;
	
	private MenuItem openCmd;
	private MenuItem exitCmd;
	
	
	public TopMenuBar()
	{
		fileMenu = new FileMenu<File>("File");
		buildFileMenu();
		
		getMenus().add(fileMenu);
	}
	
	private void buildFileMenu()
	{
		importROMCmd = new MenuItem("Import ROM...");
		
		openCmd = new MenuItem("Open");
		
		exitCmd = new MenuItem("Exit");
		exitCmd.setOnAction(e -> System.exit(0));
		
		fileMenu.setBeforeItems(new MenuItem[] {importROMCmd, openCmd});
		fileMenu.setAfterItems(new MenuItem[] {exitCmd});
		fileMenu.setRecentsManager(MainApp.getRecentFilesManager());
	}
	
	public FileMenu<File> getFileMenu()
	{
		return fileMenu;
	}

	public void setFileMenu(FileMenu<File> fileMenu)
	{
		this.fileMenu = fileMenu;
	}
	
	

	public MenuItem getImportROMCmd()
	{
		return importROMCmd;
	}

	public void setImportROMCmd(MenuItem importROMCmd)
	{
		this.importROMCmd = importROMCmd;
	}

	public MenuItem getOpenCmd()
	{
		return openCmd;
	}

	public void setOpenCmd(MenuItem openCmd)
	{
		this.openCmd = openCmd;
	}

	public MenuItem getExitCmd()
	{
		return exitCmd;
	}

	public void setExitCmd(MenuItem exitCmd)
	{
		this.exitCmd = exitCmd;
	}
}
