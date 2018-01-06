package net.starkus.mipsstudio.view;

import java.io.File;
import java.util.function.Consumer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import net.starkus.mipsstudio.project.Project;

public class ProjectExplorer extends TreeView<File> {
	
	private final ObjectProperty<Project> project;
	
	private Consumer<File> openFileConsumer;
	
	
	public ProjectExplorer(Project project)
	{
		this.project = new SimpleObjectProperty<Project>(project);
		
		setRoot(new TreeItem<File>(project.getRootFolder()));
		populateTree(getRoot());
		
		setCellFactory(column -> {
			TreeCell<File> cell = new TreeCell<>();
			cell.treeItemProperty().addListener((obs, oldv, newv) -> {
				if (newv == null)
				{
					cell.setText("NULL");
					return;
				}
				else if (newv.getValue().equals(project.getRootFolder()))
				{
					cell.setText(newv.getValue().getAbsolutePath());
				}
				else
				{
					cell.setText(newv.getValue().getName());
				}
				
				if (newv.getValue().isFile())
					cell.setOnMouseClicked(e -> {
						if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2)
						{
							openFileConsumer.accept(newv.getValue());
						}
					});
			});
			
			return cell;
		});
	}
	
	private void populateTree(TreeItem<File> item)
	{
		for (File file : item.getValue().listFiles())
		{
			TreeItem<File> child = new TreeItem<File>(file);
			item.getChildren().add(child);
			
			if (file.isDirectory())
				populateTree(child);
		}
	}

	public Consumer<File> getOpenFileConsumer()
	{
		return openFileConsumer;
	}

	public void setOpenFileConsumer(Consumer<File> openFileConsumer)
	{
		this.openFileConsumer = openFileConsumer;
	}
}
