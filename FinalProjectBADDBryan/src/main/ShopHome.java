package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

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

public class ShopHome extends Application {

    Scene scene;
    VBox layout;
    ListView<HBox> productList = new ListView<>();
    HBox header, filter;
    Label welcomeLabel;
    TextField searchBar;
    Button searchButton, myCartButton, logOutButton, applyButton;
    ComboBox<String> ccb;
    Image gogoquery;

    PreparedStatement pst;
    private Vector<msitem> barang;

    private String firstname;

    private HashMap<String, String> msg2;

    // Mencari produk sesuai nama dan memberikan gambar
    private void gambarproduk() {
        msg2 = new HashMap<String, String>();
        msg2.put("Wireless Mouse", "https://drive.google.com/uc?id=1Vqc-Z6EyMcFJ2FOhv_H1oRYOPIExxRH4");
        msg2.put("Mechanical Keyboard", "https://drive.google.com/uc?id=1gqpvPbnFmOJoxcP-5dLsYR6C2Tf4mg8W");
        msg2.put("USB-C Cable", "https://drive.google.com/uc?id=194M0Ap9bAYmcH4DVQ0TDiF3oDvDumy8V");
        msg2.put("Batik Shirt", "https://drive.google.com/uc?id=1kG2RfvXtJrK0NRTHzUmnVpmTuCzsPD8u");
        msg2.put("Instant Coffee", "https://drive.google.com/uc?id=1RzDotQGJdBqgmP5Od0qMXdpAJh_Swm-c");
    }

    public ShopHome() {
        this.firstname = "elgankenlie";
        this.barang = new Vector<msitem>();
    }

    public ShopHome(String firstname) {
        this.firstname = firstname;
        this.barang = new Vector<msitem>();
    }

    private void initialize() {
        header = new HBox(10);
        searchBar = new TextField();
        searchBar.setPromptText("Search items in GoGo - Query Store");
        searchBar.setPrefColumnCount(30);

        searchButton = new Button("Search");
        myCartButton = new Button("My Cart");
        logOutButton = new Button("Log Out");

        // Welcome
        welcomeLabel = new Label("Welcome, " + firstname);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.LIGHT, 48));
        welcomeLabel.setTextFill(Color.WHITE);

        // Filter
        filter = new HBox(30);
        ccb = new ComboBox<>();
        ccb.getItems().addAll("Select a category", "Electronics", "Accessories", "Clothing", "Beverages", "bryan");
        applyButton = new Button("Apply");

        // Main layout
        layout = new VBox(10);
        scene = new Scene(layout, 1000, 600);
    }

    private void loadallitem() {
        String query = "SELECT * FROM msitem WHERE ItemStock > 0";
        Connection con = Connect.getInstance().getConnection();

        try {
            pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            barang.clear();

            while (rs.next()) {
                Integer ItemID = rs.getInt("ItemID");
                String ItemName = rs.getString("ItemName");
                Float ItemPrice = rs.getFloat("ItemPrice");
                String ItemDesc = rs.getString("ItemDesc");
                String ItemCategory = rs.getString("ItemCategory");
                Integer ItemStock = rs.getInt("ItemStock");

                msitem items = new msitem(ItemID, ItemName, ItemPrice, ItemDesc, ItemCategory, ItemStock);
                barang.add(items);
            }
            updateproduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void layouting() {
        // Layout Header
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(new Label("Go Go Query"), searchBar, searchButton, spacer, myCartButton, logOutButton);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2d2d44;");
        header.setAlignment(Pos.CENTER_LEFT);

        gogoquery = new Image("https://drive.google.com/uc?id=1NFisJmGQ0Vix2XwLXM06cONAYL6FnxPW");
        ImageView gambar = new ImageView(gogoquery);
        gambar.setFitWidth(150);
        gambar.setFitHeight(50);
        header.getChildren().set(0, gambar);

        searchBar.setStyle("-fx-background-color: #3d3d5c; -fx-text-fill: white;");
        searchButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white;");
        myCartButton.setStyle("-fx-background-color: #2d2d44; -fx-text-fill: white;");
        logOutButton.setStyle("-fx-background-color: #ff4c4c; -fx-text-fill: white;");

        // Layout Filter Section
        VBox filterBox = new VBox(10);
        filterBox.setPadding(new Insets(20));
        filterBox.setStyle("-fx-background-color: #2d2d44; -fx-border-radius: 5; -fx-background-radius: 5;");

        Text filterTitle = new Text("Filter");
        filterTitle.setFill(Color.WHITE);
        filterTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label categoryLabel = new Label("Category");
        categoryLabel.setTextFill(Color.WHITE);

        ccb.setStyle("-fx-background-color: #3d3d5c; -fx-text-fill: rgba(255, 255, 255, 0.8);");

        applyButton.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: black;");

        filterBox.getChildren().addAll(filterTitle, categoryLabel, ccb, applyButton);

        // Layout Products
        productList.setPadding(new Insets(10));
        productList.setStyle("-fx-background-color: #2d2d44; -fx-background-radius: 10;");
        productList.setFocusTraversable(false);

        // Main Layout
        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(filterBox, productList);
        HBox.setHgrow(productList, Priority.ALWAYS);

        layout.getChildren().addAll(header, welcomeLabel, mainLayout);
        layout.setStyle("-fx-background-color: #1a1a2e;");
    }

    private HBox createProductItem(String title, Float price, String stock, String imageUrl, int itemId) {
        HBox productItem = new HBox(10);
        productItem.setPadding(new Insets(10));
        productItem.setStyle("-fx-background-color: #3d3d5c; -fx-background-radius: 10;");

        ImageView productImage;
        try {
            productImage = new ImageView(new Image(imageUrl));
            productImage.setFitWidth(100);
            productImage.setFitHeight(100);
        } catch (Exception e) {
            productImage = new ImageView(new Image("https://drive.google.com/uc?id=1Vqc-Z6EyMcFJ2FOhv_H1oRYOPIExxRH4")); //Default image when new product added
        }

        VBox productDetails = new VBox(5);

        Text productTitle = new Text(title);
        productTitle.setFill(Color.WHITE);
        productTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text productPrice = new Text("INA$s " + price);
        productPrice.setFill(Color.YELLOW);
        productPrice.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Text productStock = new Text(stock + " Left");
        productStock.setFill(Color.RED);
        productStock.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        productDetails.getChildren().addAll(productTitle, productPrice, productStock);

        productItem.getChildren().addAll(productImage, productDetails);

     
        productItem.setOnMouseClicked(e -> {
           
			productdetail(itemId); 
        });

        return productItem;
    }

    private void productdetail(int itemId) {
        ProductDetail pd = new ProductDetail(itemId);  
        try {
            pd.start(new Stage()); 
            ((Stage) scene.getWindow()).close();  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEvents() {
        searchButton.setOnAction(e -> {
            searchfilter();
        });

        applyButton.setOnAction(e -> {
            if (ccb.getSelectionModel().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a category.");
            } else {
                filtercategory(ccb.getValue());
            }
        });

        logOutButton.setOnAction(e -> {
            System.out.println("Logging out...");
            logout();
        });
        
        myCartButton.setOnAction(e-> {
        	Cart keranjang = new Cart();
        	try {
                keranjang.start(new Stage());
                ((Stage) scene.getWindow()).close();
            } catch (Exception g) {
                g.printStackTrace();
            }
        });
    }

    private void filtercategory(String category) {
        productList.getItems().clear();

        Text prod = new Text("Showing products for category '" + category + "'");
        prod.setFill(Color.WHITE);
        prod.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HBox header = new HBox(prod);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #3d3d5c; -fx-background-radius: 10;");
        productList.getItems().add(header);

        for (msitem bar : barang) {
            if (bar.getItemCategory().equals(category)) {
                String stock = String.valueOf(bar.getItemStock());
                String url = msg2.getOrDefault(bar.getItemName(), "https://drive.google.com/uc?id=1Vqc-Z6EyMcFJ2FOhv_H1oRYOPIExxRH4");
                productList.getItems().add(createProductItem(bar.getItemName(), bar.getItemPrice(), stock, url, bar.getItemID()));

            }
        }
    }

    private void logout() {
        LoginPage logout = new LoginPage();
        try {
            logout.start(new Stage());
            ((Stage) scene.getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchfilter() {
        String search = searchBar.getText().trim();
        Connection connect = Connect.getInstance().getConnection();
        String query = "SELECT * FROM msitem WHERE ItemName LIKE ?";

        try {
            Connect.getInstance();
            pst = connect.prepareStatement(query);

            // To search any items that contains String and substring from search input
            pst.setString(1, "%" + search + "%");

            ResultSet rs = pst.executeQuery();
            barang.clear();

            while (rs.next()) {
                Integer ItemID = rs.getInt("ItemID");
                String ItemName = rs.getString("ItemName");
                Float ItemPrice = rs.getFloat("ItemPrice");
                String ItemDesc = rs.getString("ItemDesc");
                String ItemCategory = rs.getString("ItemCategory");
                Integer ItemStock = rs.getInt("ItemStock");

                msitem item = new msitem(ItemID, ItemName, ItemPrice, ItemDesc, ItemCategory, ItemStock);
                barang.add(item);

                if (barang.isEmpty()) {
                    System.out.println("No search found for " + searchBar.getText().trim() + " result");
                } else {
                    for (msitem item1 : barang) {
                        String stock = "Stock: " + item1.getItemStock();
                        String url = msg2.getOrDefault(item1.getItemName(), "https://drive.google.com/uc?id=1Vqc-Z6EyMcFJ2FOhv_H1oRYOPIExxRH4");
                        productList.getItems().add(createProductItem(
                        		item1.getItemName(), 
                        		item1.getItemPrice(), 
                        		stock, 
                        		msg2.get(item1.getItemName()),
                        		item1.getItemID()));
;
                    }
                }
            }
            updateproduct();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateproduct() {
        productList.getItems().clear();

        Text productTitle = new Text("Showing " + barang.size() + " products for '" + searchBar.getText().trim() + "'");
        productTitle.setFill(Color.WHITE);
        productTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        HBox header = new HBox(productTitle);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #3d3d5c; -fx-background-radius: 10;");
        productList.getItems().add(header);

        for (msitem items : barang) {
            String stok = String.valueOf(items.getItemStock());
            String url = msg2.getOrDefault(items.getItemName(), "https://drive.google.com/uc?id=1Vqc-Z6EyMcFJ2FOhv_H1oRYOPIExxRH4");
            productList.getItems().add(createProductItem(items.getItemName(), items.getItemPrice(), stok, url, items.getItemID()));

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
            initialize();
            gambarproduk();
            loadallitem();
            layouting();

            setEvents();

            primaryStage.setTitle("brayen cape debug");
            primaryStage.setScene(scene);
            primaryStage.show();
  
    }
}
