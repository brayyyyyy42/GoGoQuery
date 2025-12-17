package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import utils.Connect;
import models.msuser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class LoginPage extends Application implements EventHandler<KeyEvent> {

    Scene scene;
    Label login, title1, title2, emails, pw, reg;
    Hyperlink reglink;
    VBox vb;
    BorderPane bp;
    TextField email;
    PasswordField pass;
    Button loginb;
    GridPane gp;
    Alert alert;
    Vector<msuser> datauser;  
    Connect connect;
    VBox titles;
    Text textGo1 = new Text("Go ");
    

    Text textGo2 = new Text("Go ");
    

    Text textQuery = new Text("Query");
   

    
    TextFlow logo = new TextFlow(textGo1, textGo2, textQuery);
    

    private void initialize() {
        login = new Label("Login");
        emails = new Label("Email : ");
        reg = new Label(" Are you new here? ");
        reglink = new Hyperlink("Register here ");
        pw = new Label("Password : ");
        vb = new VBox();
        bp = new BorderPane();
        email = new TextField();
        pass = new PasswordField();
        loginb = new Button("Login");
        scene = new Scene(bp, 500, 300);
      
        gp = new GridPane();
        datauser = new Vector<>();  
        connect = Connect.getInstance();
    }

    private void layouting() {
    	
    	
        gp.setAlignment(Pos.CENTER);
        gp.add(login, 0, 0, 2, 1);
        gp.add(emails, 0, 1);
        gp.add(email, 1, 1);
        gp.add(pw, 0, 2);
        gp.add(pass, 1, 2);
        gp.add(loginb, 1, 3);

        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(20);

        HBox registerBox = new HBox();
        registerBox.setAlignment(Pos.CENTER);
        registerBox.setSpacing(0);
        registerBox.getChildren().addAll(reg, reglink);

        logo.setTextAlignment(TextAlignment.CENTER);
        vb.getChildren().addAll(logo, gp, registerBox);
        bp.setCenter(vb);
        
        
    }

    private void styling() {
        bp.setStyle("-fx-background-color: linear-gradient(to right, #000000, #333333);");
        textGo1.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: white;");
        textGo2.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: white;");
        textQuery.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: orange;");
        
        login.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: orange;");
        emails.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        pw.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        reg.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        reglink.setStyle("-fx-font-size: 12px; -fx-text-fill: orange; -fx-underline: true;");
        email.setStyle("-fx-background-color: white;");
        pass.setStyle("-fx-background-color: white;");
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setPadding(new javafx.geometry.Insets(20));
        loginb.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }

    private void setMouseEvent() {
        loginb.setOnAction(e -> validate());
       
        reglink.setOnAction(e -> RegisterPage());
        
        
    }
    
   
    
    

    private void RegisterPage() {
        RegisterPage reg = new RegisterPage();
        
        Stage regs = new Stage();
        ((Stage) scene.getWindow()).close();
        try {
            reg.start(regs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validate() {
        String emails = email.getText().trim();
        String passs = pass.getText().trim();

        if (emails.isEmpty() || passs.isEmpty()) {
            showAlertEmpty();
        } else if (validasilogin(emails, passs)) {
        	
        	String role = getrole(emails, passs);
        	String Firstname = ExtractFirstName(emails);
        	
        	Platform.runLater(() -> {
        		if(role.equalsIgnoreCase("shopper")) {
           		 ShopHome home = new ShopHome(Firstname);
                    try {
        				home.start(new Stage());
        				((Stage) scene.getWindow()).close();
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
           	}else if(role.equalsIgnoreCase("manager")) {
           		Managergogoquery managerhome = new Managergogoquery();
           		 
                    try {
        				managerhome.start(new Stage());
        				((Stage) scene.getWindow()).close();
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
           	}
              
            else {
               showAlertwrong();
           }
        	});
        }
        
    }

       
        	
        
    
    private String ExtractFirstName(String email) {
    	String fn = email.split("@")[0];
    	fn = fn.substring(0, 1).toUpperCase() + fn.substring(1).toLowerCase();
    	return fn;
    }
    
    private String getrole(String email, String password) {
        for (msuser user : datauser) {
            if (user.getUserEmail().equals(email) && user.getUserPassword().equals(password)) {
//           debug     System.out.println("Role ditemukan: " + user.getUserRole());
                return user.getUserRole();
            }
        }
        return null;
    }


    private void showAlertEmpty() {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid login");
        alert.setHeaderText("Log in Failed");
        alert.setContentText("Please fill out all fields");
        alert.showAndWait();
    }

    private void showAlertwrong() {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid login");
        alert.setHeaderText("Wrong credentials");
        alert.setContentText("You entered a wrong email or password");
        alert.showAndWait();
    }

    private void getData() {
        String query = "SELECT * FROM msuser";
        connect.rs = connect.execQuery(query);

        try {
            while (connect.rs.next()) {
                Integer UserID = connect.rs.getInt("UserID");
                java.sql.Date UserDOB = connect.rs.getDate("UserDOB");
                String UserEmail = connect.rs.getString("UserEmail");
                String UserPassword = connect.rs.getString("UserPassword");
                String UserRole = connect.rs.getString("UserRole");
                String UserGender = connect.rs.getString("UserGender");

//                debug
//                System.out.println("User found: " + UserEmail + " with password: " + UserPassword);

                msuser user = new msuser(UserID, UserDOB, UserEmail, UserPassword, UserRole, UserGender);
                datauser.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validasilogin(String email, String password) {
        boolean inivalid = false;
        // Periksa data dalam Vector yang telah diambil sebelumnya
        for (msuser user : datauser) {
//        debug    System.out.println("Checking email: " + email + " with user email: " + user.getUserEmail());
//    debug        System.out.println("Checking password: " + password + " with user password: " + user.getUserPassword());
            if (user.getUserEmail().equalsIgnoreCase(email) && user.getUserPassword().equals(password)) {
//             debug   System.out.println("Login berhasil untuk email: " + email);
                inivalid = true;
                break;
            }
        }

        if (!inivalid) {
            showAlertwrong();
        }
        return inivalid;
    }


        
    
    
    public void setkey() {
    	email.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					validate();
				}
			}
		});
    
	pass.setOnKeyPressed(new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			if(event.getCode().equals(KeyCode.ENTER)) {
				validate();
			}
		}
	});
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        getData();  
        layouting();
        styling();
        setMouseEvent();
        setkey();
        primaryStage.setScene(scene);
        primaryStage.show();
        
        	
        
    }

	@Override
	public void handle(KeyEvent event) {
		
	}

	public static int getUserId() {
		return 0;
	}

	

	
}
