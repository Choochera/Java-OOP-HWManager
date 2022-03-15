import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Class extends Day{

	private String name;
	private ArrayList<String> assignments;
	private File file;
	
	public Class(String name) {
		this.name = name;
		assignments = new ArrayList<String>();
		file = new File(new File("").getAbsolutePath() + "/" + this.getDirectory().getName() + "/" + name + ".txt");
		if(!file.exists()) {
			this.createFile(file);
		}
		else {
			this.updateAssignments();
		}
	}
	
	public static void deleteClass(Class target) {
		try {
			Files.deleteIfExists(Paths.get(target.getFile().getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void addAssignment(String assignment) {
		assignments.add(assignment);
		updateFile();
		
	}
	
	public void removeAssignment(String assignment) {
		assignments.remove(assignments.indexOf(assignment));
		updateFile();
	}
	
	public void updateAssignments() {
		try {
			Scanner reader = new Scanner(file);
			assignments.clear();
			if(reader.hasNextLine()) {
				String fullLine = reader.nextLine();
				String[] words = fullLine.split("~");
				for(String temp : words) {
					assignments.add(temp);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateFile() {
		FileWriter writer;
		if(file.exists()){
			try {
			writer = new FileWriter(file);
			for(int i = 0; i < assignments.size(); i++) {
				writer.write(assignments.get(i) + "~");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getAssignments() {
		return assignments;
	}

	public void setAssignments(ArrayList<String> assignments) {
		this.assignments = assignments;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String toString() {
		return name;
	}
}
