import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Day {

	private String date;
	private ArrayList<Class> classes;
	private File directory;
	private boolean exists = false;
	
	public boolean Exists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public Day() {
		date = java.time.LocalDate.now() + "";
		classes = new ArrayList<Class>();
		directory = new File(new File("").getAbsolutePath() + "/" + date);
		if(!directory.isDirectory()) {
				directory.mkdir();
		}
		else {
			exists = true;
		}
	}
	
	public Day(String day) {
		date = day;
		directory = new File(new File("").getAbsolutePath() + "/" + date);
		exists = true;
		classes = new ArrayList<Class>();
		updateClasses();
	}
	
	public Day(ArrayList<Class> classes) {
		this.classes = classes;
	}
	
	public void addClass (Class name) {
		classes.add(name);
	}
	
	public void removeClass (Class name) {
		classes.remove(classes.indexOf(name));
	}
	
	public void updateClasses() {
		File[] files = directory.listFiles();
		classes.clear();
		for(File temp : files) {
			if(temp.isFile()) {
				classes.add(new Class(temp.getName().substring(0,temp.getName().indexOf('.'))));
			}
		}
	}
	public void createFile(File file) {
		try {
			FileWriter writer = new FileWriter(file);		
			writer.close();
		}
		catch(IOException g) {
			try {
				displayTray("File Write Error");
			} catch (MalformedURLException | AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void displayTray(String message) throws AWTException, MalformedURLException {
        //Obtain only one instance of the SystemTray object
        SystemTray tray = SystemTray.getSystemTray();

        //If the icon is a file
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        //Alternative (if the icon is on the classpath):
        //Image image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("icon.png"));

        TrayIcon trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
        //Let the system resize the image if needed
        trayIcon.setImageAutoSize(true);
        //Set tooltip text for the tray icon
        trayIcon.setToolTip("System tray icon demo");
        tray.add(trayIcon);

        trayIcon.displayMessage("Homework Manager", message, MessageType.INFO);
    }
	
	public void loadClasses() {
		
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Class> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Class> classes) {
		this.classes = classes;
	}
	
	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public String toString() {
		return date;
	}
	
	
}
