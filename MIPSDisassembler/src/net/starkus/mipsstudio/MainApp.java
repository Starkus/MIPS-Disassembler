package net.starkus.mipsstudio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.starkus.appify.files.RecentFilesManager;
import net.starkus.mipsstudio.project.Project;
import net.starkus.mipsstudio.view.MainWindowController;

public class MainApp extends Application {

	
	private static Stage mainStage;
	
	private static RecentFilesManager<File> recents = new RecentFilesManager<>();
	
	private static Project currentProject;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		mainStage = primaryStage;
		
		currentProject = new Project("Proyecto!", new File("C:/Users/emili/proyecto/"));
		
		try	{
			MainWindowController controller = loadMainWindow();
			
			if (!getParameters().getRaw().isEmpty())
				Platform.runLater(() -> {
					controller.setFile(new File(getParameters().getUnnamed().get(0)));
				});
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	private MainWindowController loadMainWindow() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("resources/MainWindow.fxml"));
		AnchorPane pane = (AnchorPane) loader.load();
		
		Scene scene = new Scene(pane);
		
		mainStage.setScene(scene);
		mainStage.setTitle("IDK yet");
		
		
		mainStage.show();
		
		return loader.getController();
	}

	
	public static String getResourcePath()
	{
		return "/net/starkus/mipsstudio/resources/";
	}
	
	public static InputStream getResourceAsStream(String name)
	{
		return MainApp.class.getResourceAsStream(getResourcePath() + name);
	}
	
	public static String getResourceAsString(String name) throws IOException
	{
		InputStream is = getResourceAsStream(name);
		
		byte[] b = new byte[is.available()];
		is.read(b);
		
		String s = new String(b);
		
		return s;
	}
	
	
	public static Stage getMainStage()
	{
		return mainStage;
	}
	
	public static RecentFilesManager<File> getRecentFilesManager()
	{
		return recents;
	}
	
	public static void setCurrentProject(Project project)
	{
		currentProject = project;
	}
	public static Project getCurrentProject()
	{
		return currentProject;
	}
	
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
