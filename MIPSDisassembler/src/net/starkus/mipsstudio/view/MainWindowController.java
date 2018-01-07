package net.starkus.mipsstudio.view;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import net.starkus.appify.files.RecentFilesManager;
import net.starkus.mipsstudio.MainApp;
import net.starkus.mipsstudio.control.LazyLoadTableView;
import net.starkus.mipsstudio.model.CodeEntry;
import net.starkus.mipsstudio.model.Instruction;
import net.starkus.mipsstudio.save.JSONUtils;


public class MainWindowController {
	
	@FXML
	private VBox menuBarVBox;
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button refreshCmd;
	@FXML
	private TextField addressField;
	@FXML
	private Button goButton;
	
	@FXML
	private LazyLoadTableView<CodeEntry> codeTable;
	@FXML
	private TableColumn<CodeEntry, Number> addressColumn;
	@FXML
	private TableColumn<CodeEntry, Number> hexColumn;
	@FXML
	private TableColumn<CodeEntry, String> assemblyColumn;
	
	
	private File currentFile;
	
	private long programCounter = 0;
	
	
	@FXML
	void initialize()
	{
		TopMenuBar topMenuBar = new TopMenuBar();
		
		topMenuBar.getImportROMCmd().setOnAction(e -> importROM());
		topMenuBar.getOpenCmd().setOnAction(e -> openFile());
		topMenuBar.getFileMenu().setRecentEntryOnAction(e -> openFile(e.getFile()));
		
		menuBarVBox.getChildren().add(0, topMenuBar);
		
		
		ProjectExplorer projectExplorer = new ProjectExplorer(MainApp.getCurrentProject());
		projectExplorer.setOpenFileConsumer(f -> openFile(f));
		borderPane.setLeft(projectExplorer);
		
		
		refreshCmd.setOnAction(e -> readFile(0));
		
		goButton.setOnAction(e -> {
			int offset = Integer.parseInt(addressField.getText(), 16);
			//readFile(offset);
			codeTable.scrollTo(offset/4);
		});
		addressField.setOnAction(goButton.getOnAction());
		
		
		JSONUtils.Load();
		
		MainApp.getRecentFilesManager().setOnChange(e -> {
			JSONUtils.Save();
		});
		
		
		initTable();
	}
	
	void initTable()
	{
		addressColumn.setCellValueFactory(v -> v.getValue().addressProperty());
		hexColumn.setCellValueFactory(v -> {
			CodeEntry c = v.getValue();
			
			if (c.getAssembly() == null || c.getAssembly().isEmpty())
			{
				try
				{
					RandomAccessFile raFile = new RandomAccessFile(currentFile, "r");
					raFile.seek(c.getAddress());
					
					int hex = readHex(raFile);
					Instruction instruction = Instruction.fromWord(hex);
					CodeEntry codeEntry = new CodeEntry(c.getAddress(), hex, instruction);
					
					codeTable.getItems().set((int) (c.getAddress() / 4), codeEntry);
					
					raFile.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return v.getValue().hexProperty();
		});
		assemblyColumn.setCellValueFactory(v -> v.getValue().assemblyProperty());
		
		addressColumn.setCellFactory(c -> new HexWordTableCell());		
		hexColumn.setCellFactory(c -> new HexWordTableCell());
	}
	
	void importROM()
	{
		FileChooser fileChooser = new FileChooser();
		
		// Set initial dir to most recent file's folder
		RecentFilesManager<File> recents = MainApp.getRecentFilesManager();
		if (!recents.getRecents().isEmpty())
		{
			fileChooser.setInitialDirectory(recents.getRecents().get(0).getParentFile());
		}
		
		File openedFile = fileChooser.showOpenDialog(MainApp.getMainStage());
		
		if (openedFile == null)
			return;
		
		try
		{
			MainApp.getCurrentProject().importROMFile(openedFile);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void openFile()
	{
		FileChooser fileChooser = new FileChooser();
		
		// Set initial dir to most recent file's folder
		RecentFilesManager<File> recents = MainApp.getRecentFilesManager();
		if (!recents.getRecents().isEmpty())
		{
			fileChooser.setInitialDirectory(recents.getRecents().get(0).getParentFile());
		}
		
		File openedFile = fileChooser.showOpenDialog(MainApp.getMainStage());
		
		if (openedFile == null)
			return;
		
		openFile(openedFile);
	}
	
	void openFile(File file)
	{
		MainApp.getRecentFilesManager().registerRecent(file);
		currentFile = file;
		
		readFile(0);
	}
	
	void readFile(int offset)
	{
		try {
			RandomAccessFile raFile = new RandomAccessFile(currentFile, "r");
			
			raFile.seek(offset);
			programCounter = raFile.getFilePointer() / 4;
			
			codeTable.getItems().clear();
			
			for (int i=0; i < Math.min(raFile.length(), 8000); ++i)
			{
				int address = (int) (programCounter * 4);
				codeTable.getItems().add(new CodeEntry(address));
				programCounter++;
			}
			
			
			codeTable.setLoadMoreFunction(oldItems -> {
				List<CodeEntry> loaded = new ArrayList<>();
				
				int oldSize = oldItems.size();
				RandomAccessFile raf;
				try
				{
					raf = new RandomAccessFile(currentFile, "r");
					
					int currentLoadedSize = oldSize;
					long toAdd = Math.min(currentLoadedSize + 8000, raf.length());
					
					for (int i=currentLoadedSize; i < toAdd; ++i)
					{
						int address = (int) (programCounter * 4);
						loaded.add(new CodeEntry(address));
						programCounter++;
					}
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return loaded;
			});
			
			codeTable.setUpScrollBarListener(codeTable.getScrollBar());
			
			/*
				// Debug
				System.out.println(newv.doubleValue());

				// If scrolled to bottom
				if (newv.doubleValue() == scrollBar.getMax())
				{
					int oldSize = codeTable.getItems().size();
					RandomAccessFile raf;
					try
					{
						raf = new RandomAccessFile(currentFile, "r");
						
						int currentLoadedSize = codeTable.getItems().size();
						long toAdd = Math.min(currentLoadedSize + 8000, raf.length());
						
						for (int i=currentLoadedSize; i < toAdd; ++i)
						{
							int address = (int) (programCounter * 4);
							codeTable.getItems().add(new CodeEntry(address));
							programCounter++;
						}
						
						double newSize = codeTable.getItems().size();
						double added = newSize - oldSize;
						double x = added / newSize;
						
						System.out.println(x);
						
						scrollBar.setValue(1.0 - x);
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});*/
			
			raFile.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private int readWord(RandomAccessFile raFile) throws IOException
	{
		int l = raFile.readUnsignedByte();
		int h = raFile.readUnsignedByte();
		
		return l | (h << 8);
	}
	
	private int readHex(RandomAccessFile raFile) throws IOException
	{
		int upperHalf = readWord(raFile);
		int lowerHalf = readWord(raFile);
		int word = upperHalf << 16 | lowerHalf;
		
		return word;
	}
	
	private Instruction readInstruction(int hex)
	{
		Instruction inst = Instruction.fromWord(hex);
		
		programCounter++;
		return inst;
	}
	
	
	
	public void setFile(File file)
	{
		currentFile = file;
		readFile(0);
	}
	
	public File getFile()
	{
		return currentFile;
	}
	
	
	private class HexWordTableCell extends TableCell<CodeEntry, Number> {
		@Override
		protected void updateItem(Number item, boolean empty)
		{
			if (empty)
			{
				return;
			}
			
			setText(String.format("%08X", item.intValue()));
		}
	}
}
