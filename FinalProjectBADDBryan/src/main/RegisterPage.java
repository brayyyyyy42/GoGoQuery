package main;

import java.time.LocalDate;
import java.util.Vector;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.msuser;
import utils.Connect;

public class RegisterPage extends Application   {

    VBox root;
    GridPane formGrid;
    Label title, formTitle, emailLabel, passwordLabel, confirmPasswordLabel, dobLabel, genderLabel, tcb;
    TextField emailField;
    PasswordField passwordField, confirmPasswordField;
    DatePicker dobPicker;
    RadioButton maleButton, femaleButton;
    ToggleGroup genderGroup;
    CheckBox termsCheckBox;
    Button registerButton;
    Hyperlink loginLink;
    Alert alert;
    Scene scene;
    Connect connect;
    Vector<msuser> data;
    String saveemail, savepw, savegender;
    Date savedate;
    

    private void initialize() {
        // Scene initialization
        title = new Label("Go Go Query");
        formGrid = new GridPane();
        formTitle = new Label("Register");
        emailLabel = new Label("Email:");
        emailField = new TextField();
        passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();
        dobLabel = new Label("Date of Birth:");
        dobPicker = new DatePicker();
        connect = connect.getInstance();

        genderLabel = new Label("Gender:");
        maleButton = new RadioButton("Male");
        femaleButton = new RadioButton("Female");
        genderGroup = new ToggleGroup();
        maleButton.setToggleGroup(genderGroup);
        femaleButton.setToggleGroup(genderGroup);

        termsCheckBox = new CheckBox("I accept the ");
        tcb = new Label("terms and condition");

        registerButton = new Button("Register");

        loginLink = new Hyperlink("Already have an account? Sign in here!");

        // Ensure the alert is initialized
        alert = new Alert(Alert.AlertType.ERROR);
        
    }

    private void layouting() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(title, formGrid);

        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        formGrid.setMaxWidth(400);

        HBox genderBox = new HBox(10, maleButton, femaleButton);

        HBox signBox = new HBox();
        signBox.setAlignment(Pos.CENTER);
        signBox.setSpacing(0);
        signBox.getChildren().addAll(termsCheckBox, tcb);
        tcb.setStyle("-fx-font-size: 12px; -fx-text-fill: blue; -fx-underline: true;");

        formGrid.add(formTitle, 0, 0, 2, 1);
        GridPane.setHalignment(formTitle, HPos.CENTER);
        formGrid.add(emailLabel, 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(passwordLabel, 0, 2);
        formGrid.add(passwordField, 1, 2);
        formGrid.add(confirmPasswordLabel, 0, 3);
        formGrid.add(confirmPasswordField, 1, 3);
        formGrid.add(dobLabel, 0, 4);
        formGrid.add(dobPicker, 1, 4);
        formGrid.add(genderLabel, 0, 5);
        formGrid.add(genderBox, 1, 5);
        formGrid.add(signBox, 1, 6);
        formGrid.add(registerButton, 1, 7);
        formGrid.add(loginLink, 1, 8);
    }

    private void styling() {
        root.setStyle("-fx-background-color: #2E2F38;");

        title.setFont(Font.font("Arial", 30));
        title.setTextFill(Color.ORANGE);

        formGrid.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10;");

        formTitle.setFont(Font.font("Arial", 20));
        formTitle.setTextFill(Color.BLACK);

        registerButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 5 20 5 20;");

        loginLink.setStyle("-fx-text-fill: #FFA500;");
    }

    private void setEvents() {
        registerButton.setOnAction(e -> {
        System.out.println("tes aja");
        validateForm();
    });
        loginLink.setOnAction(e -> LoginPage());
    }
    
    private void setKeyevent() {
    	emailField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle( KeyEvent event) {
				
				if(event.getCode().equals(KeyCode.ENTER)) {
					showAlert("All fields must be filled");
				}
				
				
			}
    	});
    
    	
    	passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					validateForm();;
				}
			}
		});
    	
    	confirmPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle( KeyEvent event) {
				
				if(event.getCode().equals(KeyCode.ENTER)) {
					showAlert("All fields must be filled");
				}
				
				
			}
			
			
    	});
    	
    	
    			
    			
    }

    private void LoginPage() {
        LoginPage login = new LoginPage();
        ((Stage) scene.getWindow()).close();
        Stage log = new Stage();

        try {
            login.start(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkEmptyFields(String email, String pw, String confpw, LocalDate dob, String gender) {
        return email.isEmpty() || pw.isEmpty() || confpw.isEmpty() || dob == null || gender == null;
    }

    private void validateForm() {
        String email = emailField.getText().trim();
        String pw = passwordField.getText().trim();
        String confpw = confirmPasswordField.getText().trim();
        LocalDate dob = dobPicker.getValue();
        if(dob != null) {
        	Date datee = Date.valueOf(dob);
        }
        Date datee = Date.valueOf(dob);
        
       Toggle gendertog = genderGroup.getSelectedToggle();
       String gender = null;
       if(gendertog != null) {
    	   gender = ((RadioButton) gendertog).getText();
       }
        boolean term = termsCheckBox.isSelected();

        if (checkEmptyFields(email, pw, confpw, dob, "")) {
            showAlert("All fields must be filled");
        } else if (!email.endsWith("@gomail.com")) {
            showAlert("Email must end with @gomail.com");
        } else if (!isValidEmailCharacter(email)) {
            showAlert("No special character is allowed other than ‘@’, ‘_’, or ‘.’");
        } else if (!isEmailUnique(email)) {
            showAlert("Email is already taken, please use another address");
        } else if (!isAlphanumeric(pw)) {
            showAlert("Password must be alphanumeric");
        } else if (!pw.equals(confpw)) {
            showAlert("Passwords do not match");
        } else if (dob.isAfter(LocalDate.now().minusYears(17))) {
            showAlert("User must be at least 17 years old");
        } else if (gender == null) {
            showAlert("Select your gender");
        } else if (!term) {
            showAlert("You must agree to the terms and conditions");
        } else {
        	
        	
        	saveemail = email;
        	savepw = pw;
        	savegender = gender;
        	savedate = datee;
        	
//        	Date date= Date.valueOf(dob);
//            registdata(email, pw, gender, date, role);
            SelectRolePage();
        }
        
       
        
    }

    private void showAlert(String message) {
        alert.setTitle("Register Failed");
        alert.setHeaderText("Register Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmailCharacter(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean isEmailUnique(String email) {
       
        try {
            ResultSet rs = connect.execQuery("SELECT * FROM msuser WHERE UserEmail = '" + email + "'");
            return !rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isAlphanumeric(String pw) {
        return pw.matches("[a-zA-Z0-9]+");
    }

    private void SelectRolePage() {

        RolePage role = new RolePage(saveemail, savepw, savegender, savedate);
        ((Stage)scene.getWindow()).close();
        Stage roles = new Stage();

        try {
        	
            role.start(roles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   
    

    @Override
    public void start(Stage stage) {
        try {
            initialize();
            layouting();
            styling();
            setEvents();
            setKeyevent();

            scene = new Scene(root, 1280, 720); // Ensure root is set before scene creation
            stage.setScene(scene);
            stage.setTitle("Go Go Query");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            System.exit(1); // Exit the application with an error code
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
