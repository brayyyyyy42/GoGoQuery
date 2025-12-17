package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Managergogoquery extends Application {

    BorderPane root;
    Scene scene;

    private void initi() {
        // Menu Bar Setup
        MenuBar mb = new MenuBar();
        Menu manager = new Menu("Menu");

        MenuItem addItem = new MenuItem("Add Item");
        MenuItem mq = new MenuItem("Manage Queue");
        MenuItem logout = new MenuItem("Log Out");

        manager.getItems().addAll(addItem, mq, logout);
        mb.getMenus().add(manager);

        // Welcome Label
        Label welcomeLabel = new Label("Welcome to GoGoQuery Manager 2.0");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Setting up the layout
        root = new BorderPane();
        root.setTop(mb);
        root.setCenter(welcomeLabel); // Center the welcome label

        scene = new Scene(root, 600, 400);

        // Event Handlers
        addItem.setOnAction(e -> redirectadd("Add Item Page"));
        mq.setOnAction(e -> redirectque("Manage Queue Page"));
        logout.setOnAction(e -> redirectlogout("Log Out Page"));
    }

    private void redirectadd(String page) {
AddItemPage add = new AddItemPage();
        
        Stage regs = new Stage();
        ((Stage) scene.getWindow()).close();
        try {
            add.start(regs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
        private void redirectlogout(String page) {
        
LoginPage LogOutPage = new LoginPage();
        
        Stage regss = new Stage();
        ((Stage) scene.getWindow()).close();
        try {
            LogOutPage.start(regss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        private void redirectque(String page) {
        	QueueManagementPage qm = new QueueManagementPage();
        	Stage regsss = new Stage();
        			((Stage)scene.getWindow()).close();
        	try {
        		qm.start(regsss);
        	}catch(Exception e) {
        		e.printStackTrace();
        	}
        }
    
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initi();
        primaryStage.setTitle("GoGoQuery Manager 2.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
