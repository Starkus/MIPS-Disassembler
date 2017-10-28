package net.starkus.mipsstudio.view;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.naming.InvalidNameException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.starkus.mipsstudio.MainApp;
import net.starkus.mipsstudio.assembler.Instructions;
import net.starkus.mipsstudio.model.CodeEntry;


public class MainWindowController {
	
	@FXML
	private MenuItem openCmd;
	
	@FXML
	private Button refreshCmd;
	@FXML
	private TextField addressField;
	@FXML
	private Button goButton;
	
	@FXML
	private TableView<CodeEntry> codeTable;
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
		openCmd.setOnAction(e -> openFile());
		refreshCmd.setOnAction(e -> readFile(0));
		
		goButton.setOnAction(e -> {
			long offset = Long.parseLong(addressField.getText(), 16);
			readFile(offset);
		});
		addressField.setOnAction(goButton.getOnAction());
		
		
		initTable();
	}
	
	void initTable()
	{
		addressColumn.setCellValueFactory(v -> v.getValue().addressProperty());
		hexColumn.setCellValueFactory(v -> v.getValue().hexProperty());
		assemblyColumn.setCellValueFactory(v -> v.getValue().assemblyProperty());
		
		addressColumn.setCellFactory(c -> new HexWordTableCell());		
		hexColumn.setCellFactory(c -> new HexWordTableCell());
	}
	
	void openFile()
	{
		FileChooser fileChooser = new FileChooser();
		currentFile = fileChooser.showOpenDialog(MainApp.getMainStage());
		
		if (currentFile == null)
			return;
		
		readFile(0x20160);
	}
	
	void readFile(long offset)
	{
		try {
			RandomAccessFile raFile = new RandomAccessFile(currentFile, "r");
			
			raFile.seek(offset);
			programCounter = raFile.getFilePointer() / 4;
			
			codeTable.getItems().clear();
			for (int i=0; i < 256; i++)
			{
				long address = programCounter * 4;
				int hex = readHex(raFile);
				String code = readInstruction(hex);
				
				//textArea.setText(textArea.getText() + address + "    " + ins + "\n");
				codeTable.getItems().add(new CodeEntry(address, hex, code));
			}
			
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
	
	private String readInstruction(int hex)
	{
		String ins = "";
		
		//ins += String.format("%08X", word) + "    ";
		
		try
		{
			ins = Instructions.parseInstruction(hex, programCounter);
		}
		catch (InvalidNameException e)
		{
			//e.printStackTrace();
			ins += "!";
		}
		
		
		programCounter++;
		return ins;
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
