package main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import utils.Connect;

public class RolePage extends Application {

    private BorderPane bp;
    private VBox vb, managerBox, shopperBox;
    private HBox mainLayout;
    private Label title, managerLabel, managerDesc, shopperLabel, shopperDesc;
    private StackPane managerIcon, shopperIcon;
    private Circle managerCircle, shopperCircle;
    private Button managerButton, shopperButton;
    private Scene scene;
    private Alert alert;
    private Connection connect;
    

 private String saveemail, savepw, savegender;
 private Date savedate;
  

	public RolePage(String saveemail, String savepw, String savegender, Date savedate) {
		super();
		this.saveemail = saveemail;
		this.savepw = savepw;
		this.savegender = savegender;
		this.savedate = savedate;
	}



	private void initialize() {
        // Main Layout
        bp = new BorderPane();

        // Title
        title = new Label("Go Go Query");

     // Manager Section
        managerCircle = new Circle(40);
        Image managerImage = new Image("https://drive.google.com/uc?id=1ASjMWERvnY5xsb2pgxI4pfLg9k7gURfz"); // Replace with your manager image path
        ImageView managerImageView = new ImageView(managerImage);
        managerImageView.setFitWidth(60);
        managerImageView.setFitHeight(60);
        managerIcon = new StackPane(managerCircle, managerImageView);

        managerLabel = new Label("Manager");
        managerDesc = new Label("Manage products and deliveries, be the ruler!");
        managerButton = new Button("Register as Manager");

        managerBox = new VBox();

        // Shopper Section
        shopperCircle = new Circle(40);
        Image shopperImage = new Image("https://drive.google.com/uc?id=1ASjMWERvnY5xsb2pgxI4pfLg9k7gURfz"); 
        ImageView shopperImageView = new ImageView(shopperImage);
        shopperImageView.setFitWidth(60);
        shopperImageView.setFitHeight(60);
        shopperIcon = new StackPane(shopperCircle, shopperImageView);

        shopperLabel = new Label("Shopper");
        shopperDesc = new Label("Search products, manage your cart, go shopping!");
        shopperButton = new Button("Register as Shopper");

        shopperBox = new VBox();

        // Main Container
        mainLayout = new HBox();
        vb = new VBox();
        scene = new Scene(bp, 800, 500);
    }

    private void layouting() {
        // Manager Box Layout
        managerBox.getChildren().addAll(managerIcon, managerLabel, managerDesc, managerButton);
        managerBox.setAlignment(Pos.CENTER);
        managerBox.setSpacing(10);

        // Shopper Box Layout
        shopperBox.getChildren().addAll(shopperIcon, shopperLabel, shopperDesc, shopperButton);
        shopperBox.setAlignment(Pos.CENTER);
        shopperBox.setSpacing(10);

        // Main Layout
        mainLayout.getChildren().addAll(managerBox, shopperBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(50);
        mainLayout.setPadding(new Insets(50));

        // Title and Main Layout
        vb.getChildren().addAll(title, mainLayout);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(20);

        // Add to BorderPane
        bp.setCenter(vb);
    }
    
    private void registdata(String role) { 
    	Connect connect = Connect.getInstance();
    	String query = "INSERT INTO msuser (UserEmail, UserPassword, UserGender, UserDOB, UserRole) VALUES(?, ?, ?, ?, ?)"; 
    	try { 
    		PreparedStatement pst = connect.execUpdate(query);
    		pst.setString(1, saveemail);
    		pst.setString(2, savepw);
    		pst.setString(3, savegender);
    		pst.setDate(4, savedate);;
    		pst.setString(5, role);
    	
    		pst.executeUpdate();            
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    private void styling() {
        // Background
        bp.setStyle("-fx-background-color: linear-gradient(to right, #000000, #333333);");

        // Title Styling
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.ORANGE);

        // Manager Section Styling
        managerCircle.setFill(Color.GRAY);
        managerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        managerLabel.setTextFill(Color.WHITE);
        managerDesc.setFont(Font.font("Arial", 12));
        managerDesc.setTextFill(Color.WHITE);
        managerDesc.setWrapText(true);
        managerButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");

        // Shopper Section Styling
        shopperCircle.setFill(Color.BROWN);
        shopperLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        shopperLabel.setTextFill(Color.WHITE);
        shopperDesc.setFont(Font.font("Arial", 12));
        shopperDesc.setTextFill(Color.WHITE);
        shopperDesc.setWrapText(true);
        shopperButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
    }

    private void setMouseEvent() {
    	
    	
        managerButton.setOnAction(e -> {
        	
        	
        	LoginPage login = new LoginPage();
            Stage managers = new Stage();
            
            registdata("Manager")
;            success();
            ((Stage) managerButton.getScene().getWindow()).close();
            try {
				login.start(managers);
			} catch (Exception ev) {
				
				ev.printStackTrace();
			}
            
        });

        shopperButton.setOnAction(e -> {
        	LoginPage login = new LoginPage();
           Stage shop = new Stage();
           
           registdata("Shopper");
           success();
           ((Stage)shopperButton.getScene().getWindow()).close();
           try {
        	   login.start(shop);
           }catch (Exception ei) {
        	   ei.printStackTrace();
           }
        });
    }

    	
    	
    
    private void success() {
    	alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Register information");
    	alert.setHeaderText("Register success! ");
    	alert.setContentText("Please log in with your newly created account.");
    	alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void start(Stage primaryStage) {
        initialize();
        layouting();
        styling();
        setMouseEvent();

        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
