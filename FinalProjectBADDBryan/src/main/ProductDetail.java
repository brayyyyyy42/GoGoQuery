package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.msitem;
import utils.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDetail extends Application {

    private msitem selectedItem;
    private int currentCartQuantity;
    private Spinner<Integer> quantitySpinner;
    private Button addToCartButton;
    private BorderPane layout;
    private VBox content;
    private Scene scene;
    private int itemId; 

    // Constructor accepting either msitem or ItemID
    public ProductDetail(Object nuelibab) {
        if (nuelibab instanceof msitem) {
            this.selectedItem = (msitem) nuelibab;
        } else if (nuelibab instanceof Integer) {
            this.itemId = (int) nuelibab;
        }
    }

    private int getCurrentUserId() {
        return 1;  
    }

    private void initialize() {
        layout = new BorderPane();
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1a1a2e;");

        content = new VBox(15);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.TOP_LEFT);

        currentCartQuantity = 0;

       
        if (selectedItem == null && itemId > 0) {
            fetchItemDetails();
        }
    }

    private void fetchItemDetails() {
        Connection connection = Connect.getInstance().getConnection();
        try {
            String query = "SELECT * FROM msitem WHERE ItemID = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, itemId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                selectedItem = new msitem(
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getFloat("itemPrice"),
                        rs.getString("itemDesc"),
                        rs.getString("itemCategory"),
                        rs.getInt("itemStock")
                );
                layouting();
            } else {
                showAlert(Alert.AlertType.ERROR, "Item Not Found", "No item found with ID: " + itemId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "dah la.");
        }
    }

    private void layouting() {
        if (selectedItem == null) {
            return;
        }

        HBox header = createHeader();
        layout.setTop(header);

        ImageView productImage = new ImageView(new Image("https://storage.googleapis.com/a1aa/image/VkoVbcWMQcZPJNEsD6ibPZRBuTyzkHajQPsYOJhfgDy9GX9JA.jpg"));
        productImage.setFitWidth(300);
        productImage.setFitHeight(300);

        VBox productDetails = createProductDetailsLayout();
        
        HBox mainContent = new HBox(20, productImage, productDetails);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(20));

        layout.setCenter(mainContent);
    }

    private HBox createHeader() {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2d2d44;");

        ImageView logo = new ImageView(new Image("https://drive.google.com/uc?id=1NFisJmGQ0Vix2XwLXM06cONAYL6FnxPW"));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);

        TextField searchField = new TextField();
        searchField.setPromptText("Search Items in GoGoQuery Store");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-color: #3d3d5c; -fx-text-fill: white;");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cartButton = new Button("My Cart");
        cartButton.setStyle("-fx-background-color: #3d3d5c; -fx-text-fill: white;");
cartButton.setOnAction(e->{
	Cart cart = new Cart();
	Stage regs = new Stage();
    ((Stage) scene.getWindow()).close();
    try {
        cart.start(regs);
    } catch (Exception f) {
        f.printStackTrace();
    }
});
        Button logoutButton = new Button("Log Out");
        logoutButton.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white;");
logoutButton.setOnAction(e->{
	
	LoginPage logout = new LoginPage();
	Stage regs = new Stage();
    ((Stage) scene.getWindow()).close();
    try {
        logout.start(regs);
    } catch (Exception f) {
        f.printStackTrace();
    }
});
        header.getChildren().addAll(logo, searchField, searchButton, spacer, cartButton, logoutButton);
        return header;
    }

    private VBox createProductDetailsLayout() {
        Text itemName = new Text(selectedItem.getItemName());
        itemName.setFill(Color.WHITE);
        itemName.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text itemPrice = new Text("INA$ " + selectedItem.getItemPrice());// INA = Indonesian $ new currency? i dont know, tired of this 
        itemPrice.setFill(Color.YELLOW);
        itemPrice.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text itemCategory = new Text("Category: " + selectedItem.getItemCategory());
        itemCategory.setFill(Color.WHITE);
        itemCategory.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        Text itemDescription = new Text("\n\nSpecification:\n" + selectedItem.getItemDesc());
        itemDescription.setFill(Color.LIGHTGRAY);
        itemDescription.setFont(Font.font("Arial", FontWeight.NORMAL, 16));

        Text itemStock = new Text("Stock: " + selectedItem.getItemStock());
        itemStock.setFill(Color.RED);
        itemStock.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        quantitySpinner = new Spinner<>(1, selectedItem.getItemStock(), 1);
        quantitySpinner.setEditable(true);

        HBox spinnerBox = new HBox(10, new Label("Set item quantity"), quantitySpinner);
        spinnerBox.setAlignment(Pos.CENTER_LEFT);

        addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: #1a1a2e;");
        addToCartButton.setDisable(selectedItem.getItemStock() <= 0);  // Disable button if out of stock

        addToCartButton.setOnAction(e -> handleAddToCart());

        return new VBox(10, itemName, itemPrice, itemCategory, itemDescription, itemStock, spinnerBox, addToCartButton);
    }

    private void handleAddToCart() {
        int quantityToAdd = quantitySpinner.getValue();

        if (quantityToAdd < 1 || quantityToAdd > selectedItem.getItemStock()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Please select a quantity between 1 and " + selectedItem.getItemStock() + ".");
            return;
        }

        currentCartQuantity += quantityToAdd;
        addToCartInDatabase(selectedItem.getItemID(), quantityToAdd);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Added " + quantityToAdd + " " + selectedItem.getItemName() + "(s) to your cart.");
    }

    private void addToCartInDatabase(int itemId, int quantity) {
        Connection connection = Connect.getInstance().getConnection();
        try {
            int userId = getCurrentUserId(); // Get the logged-in user's ID

            if (!userExists(userId, connection)) {
                showAlert(Alert.AlertType.ERROR, "User Not Found", "The user does not exist in the system.");
                return;
            }

            String query = "INSERT INTO mscart (ItemID, UserID, Quantity) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE Quantity = Quantity + ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, itemId);
            pst.setInt(2, userId);
            pst.setInt(3, quantity);
            pst.setInt(4, quantity);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error while adding item to the cart.");
        }
    }

    private boolean userExists(int userId, Connection connection) {
        try {
            String query = "SELECT 1 FROM msuser WHERE UserID = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) {
        initialize();
        scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Product Detail");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
