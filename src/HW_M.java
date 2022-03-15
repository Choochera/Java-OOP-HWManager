
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.scene.control.CheckBox;

public class HW_M extends Application{

	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) {
		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(10, 10, 10, 10));
		
		
		//Read files for already established days
		ListView<Day> list = new ListView<Day>();
		Label listLabel = new Label("DATE", list);
		listLabel.setContentDisplay(ContentDisplay.BOTTOM);
		File folder = new File(new File("").getAbsolutePath());
		File[] files = folder.listFiles();
		for(File temp : files) {
			if(temp.isDirectory() && temp.getName().matches("(.*)-(.*)-(.*)"))list.getItems().add(new Day(temp.getName()));
		}
		
		
		//Format list of days
		list.setMaxSize(110, 200);
		list.setPadding(new Insets(10, 10, 10, 10));
		layout.setLeft(listLabel);
		
		//Bottom buttons/Text
		Text error = new Text("");
		VBox sep = new VBox(5d);
		sep.setAlignment(Pos.BASELINE_CENTER);
		error.setFill(Color.RED);
		HBox bottom = new HBox(100d);
		bottom.setPadding(new Insets(10, 10, 10, 10));
		Button addDay = new Button("Add Day");
		addDay.setMinWidth(100);
		Button addClass = new Button ("Add Class");
		addClass.setMinWidth(100);
		Button addAssignment = new Button("Add Assignment");
		addAssignment.setMinWidth(100);
		bottom.setAlignment(Pos.BASELINE_CENTER);
		bottom.getChildren().addAll(addDay, addClass, addAssignment);
		sep.getChildren().addAll(bottom, error);
		layout.setBottom(sep);
		
		//Add day functionality
		addDay.setOnAction(event -> {
			Day temp = new Day();
			if(!temp.Exists())list.getItems().add(temp);
			
		});
		
		//Add class functionality
		addClass.setOnAction(event -> {
			Day selected = list.getSelectionModel().getSelectedItem();
			if(selected != null) {
				error.setText("");
				TextField input = new TextField();
				Label boxLabel = new Label("Input class name: ", input);
				boxLabel.setContentDisplay(ContentDisplay.RIGHT);
				StackPane pane = new StackPane();
				pane.getChildren().add(boxLabel);
				Scene window = new Scene(pane, 400, 100);
				Stage secondary = new Stage();
				secondary.setScene(window);
				secondary.setTitle("Add Class to " + selected.getDate());
				secondary.show();
				pane.setOnKeyPressed(enter -> {
					if(enter.getCode().equals(KeyCode.ENTER)) {
						Day temp = new Day();
						selected.addClass(new Class(input.getText()));
						secondary.close();
					}
				});
			}else error.setText("Must select a day");
		});
		
		
		//Interior borderpane
		BorderPane interior = new BorderPane();
		layout.setCenter(interior);
		
		//Side buttons
				Button deleteClass = new Button("Delete Class");
				deleteClass.setPrefWidth(125);
				Button deleteDay = new Button("Delete Day");
				deleteDay.setPrefWidth(125);
				Button deleteAssignment = new Button("Delete Assignment");
				deleteAssignment.setPrefWidth(125);
				VBox sideButts = new VBox(50d);
				sideButts.setAlignment(Pos.BASELINE_CENTER);
				sideButts.setPadding(new Insets(10, 10, 10, 10));
				sideButts.getChildren().addAll(deleteDay, deleteClass, deleteAssignment);
				interior.setRight(sideButts);
				
		//Adds list day-selection functionality
		list.setOnMouseClicked(event -> {
			if(list.getSelectionModel().getSelectedItem() == null) {
				Image logo = new Image("file:image/logo.png");
				ImageView view = new ImageView(logo);
				layout.setCenter(view);
				
			}
			else {
			layout.setCenter(interior);
			ListView<Class> classList = new ListView<Class>();
			Label CLLabel = new Label("CLASS", classList);
			CLLabel.setContentDisplay(ContentDisplay.BOTTOM);
			classList.setMaxSize(110, 200);
			classList.setPadding(new Insets(10, 10, 10, 10));
			
			ListView<Text> assignmentField = new ListView<Text>();
			ListView<CheckBox> assignmentDone = new ListView<CheckBox>();
			
			HBox centerInterior = new HBox();
			Label fieldLabel = new Label("ASSIGNMENT", assignmentField);
			Label done = new Label("Done?", assignmentDone);
			fieldLabel.setContentDisplay(ContentDisplay.BOTTOM);
			done.setContentDisplay(ContentDisplay.BOTTOM);
			
			assignmentField.setMaxSize(400, 200);
			assignmentDone.setMaxSize(50, 200);
			assignmentField.setEditable(false);
			centerInterior.getChildren().addAll(fieldLabel, done);
			interior.setCenter(centerInterior);
			 
			//Display assignments in textArea
			classList.setOnMouseClicked(DisplayAss -> {
				Class selectedClass = classList.getSelectionModel().getSelectedItem();
				if(selectedClass == null) {
					
				}
				else {
				Scanner reader;
				try {
					if(selectedClass != null && assignmentField.getItems().isEmpty()) {
						reader = new Scanner(new File(selectedClass.getFile().getAbsolutePath()));
						if(reader.hasNextLine()) {
							String fullLine = reader.nextLine();
							assignmentField.getItems().clear();
							String[] temp = fullLine.split("~");
							for (String word : temp) {
								assignmentField.getItems().add(new Text("\u2022 " + word));
								assignmentDone.getItems().add(new CheckBox());
							}
						}
						reader.close();
					}
					}catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				//Cross out functionality
				for(CheckBox temp : assignmentDone.getItems()) {
					 temp.setOnAction(check -> {
						 if(temp.isSelected())assignmentField.getItems().get(assignmentDone.getItems().indexOf(temp)).setStrikethrough(true);
						 else assignmentField.getItems().get(assignmentDone.getItems().indexOf(temp)).setStrikethrough(false);
					 });
				 }
				}
			});
			
			Day selected = list.getSelectionModel().getSelectedItem();
			if(selected != null) {
				classList.getItems().clear();
				for(Class temp : selected.getClasses()){
					classList.getItems().add(temp);
				}
			}
			
			interior.setLeft(CLLabel);
			//Delete class functionality
			deleteClass.setOnAction(delClass ->{
				Class selectedClass = classList.getSelectionModel().getSelectedItem();
				Class.deleteClass(selectedClass);
				selected.updateClasses();
				classList.getItems().remove(selectedClass);
			});
			
			//Delete day functionality
			deleteDay.setOnAction(delDay -> {
				Day temp = list.getSelectionModel().getSelectedItem();
				temp.getClasses().clear();
				temp.updateClasses();
				File[] array = temp.getDirectory().listFiles();
				for(File f : array) {
					f.delete();
				}
				if(temp.getDirectory().delete())list.getItems().remove(temp);
			});
			
			//Delete assignment functionality
			deleteAssignment.setOnAction(delAss -> {
				Class selectedClass = classList.getSelectionModel().getSelectedItem();
				assignmentDone.getItems().remove(selectedClass.getAssignments().indexOf(assignmentField.getSelectionModel().getSelectedItem().getText().substring(2)));
				selectedClass.getAssignments().remove(assignmentField.getSelectionModel().getSelectedItem().getText().substring(2));
				selectedClass.updateFile();
				selectedClass.updateAssignments();
				assignmentField.getItems().remove(assignmentField.getSelectionModel().getSelectedItem());
			});
			
			//Add assignment functionality
			addAssignment.setOnAction(addAss -> {
				Class selectedClass = classList.getSelectionModel().getSelectedItem();
				if(selectedClass != null) {
					error.setText("");
					TextField input = new TextField();
					Label boxLabel = new Label("Input Assignment Information: ", input);
					boxLabel.setContentDisplay(ContentDisplay.RIGHT);
					StackPane pane = new StackPane();
					pane.getChildren().add(boxLabel);
					Scene window = new Scene(pane, 400, 100);
					Stage secondary = new Stage();
					secondary.setScene(window);
					secondary.setTitle("Add Assignment to " + selectedClass.getName());
					secondary.show();
					pane.setOnKeyPressed(enter -> {
						if(enter.getCode().equals(KeyCode.ENTER)) {
							selectedClass.addAssignment(input.getText());
							secondary.close();
						}
					});
				}else error.setText("Must select a class");
			
			});
			}
		});
		
	
		
		
		Scene scene = new Scene(layout, 800, 310);
		primaryStage.setTitle("Homework Manager");
		primaryStage.setMaxWidth(700);
		primaryStage.setMaxHeight(350);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
