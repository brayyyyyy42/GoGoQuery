package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import utils.Connect;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddItemPage extends Application {

    private BorderPane root;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        scene = new Scene(root, 800, 600);

        Button openAddItemPopup = new Button("Add Item");
        openAddItemPopup.setOnAction(e -> showAddItemPopup(primaryStage));

        root.setCenter(openAddItemPopup);
        primaryStage.setTitle("Add Item Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddItemPopup(Window owner) {
        Stage popupStage = new Stage();
        popupStage.setTitle("Add Item Form");
        popupStage.initOwner(owner);

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        Label nameLabel = new Label("Item Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter item name (5-70 characters)");

        Label descLabel = new Label("Item Description:");
        TextArea descField = new TextArea();
        descField.setPromptText("Enter item description (10-255 characters)");
        descField.setWrapText(true);

        Label categoryLabel = new Label("Item Category:");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter category name");

        Label priceLabel = new Label("Item Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price (0.50 - 900,000)");

        Label quantityLabel = new Label("Item Quantity:");
        Spinner<Integer> quantitySpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 300, 1);
        quantitySpinner.setValueFactory(valueFactory);

        Button addItemButton = new Button("Add Item");
        addItemButton.setOnAction(e -> {
            if (validateForm(nameField, descField, categoryField, priceField, quantitySpinner)) {
                insertItemIntoDatabase(nameField.getText(), categoryField.getText(), descField.getText(), priceField.getText(), quantitySpinner.getValue());

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setHeaderText("Item Added Successfully");
                successAlert.setContentText("The item has been successfully added to the store's database.");
                successAlert.showAndWait();
                popupStage.close();
            }
        });

        form.getChildren().addAll(
                nameLabel, nameField,
                descLabel, descField,
                categoryLabel, categoryField,
                priceLabel, priceField,
                quantityLabel, quantitySpinner,
                addItemButton
        );

        Scene popupScene = new Scene(form, 400, 400);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private boolean validateForm(TextField nameField, TextArea descField, TextField categoryField, TextField priceField, Spinner<Integer> quantitySpinner) {
        String name = nameField.getText().trim();
        String description = descField.getText().trim();
        String category = categoryField.getText().trim();
        String priceInput = priceField.getText().trim();

        // Validate Empty Fields
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || priceInput.isEmpty()) {
            showAlert("All fields must be filled out.");
            return false;
        }

        // Validate Name Length
        if (name.length() < 5 || name.length() > 70) {
            showAlert("Item name must be between 5 and 70 characters.");
            return false;
        }

        // Validate Description Length
        if (description.length() < 10 || description.length() > 255) {
            showAlert("Item description must be between 10 and 255 characters.");
            return false;
        }

        // Validate Price
        try {
            double price = Double.parseDouble(priceInput);
            if (price < 0.50 || price > 900000) {
                showAlert("Item price must be between $0.50 and $900,000s.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Item price must be a valid number.");
            return false;
        }

        // Validate Quantity
        int quantity = quantitySpinner.getValue();
        if (quantity <= 0 || quantity > 300) {
            showAlert("Quantity must be a positive integer and cannot exceed 300.");
            return false;
        }

        return true;
    }

    private void insertItemIntoDatabase(String name, String category, String description, String priceStr, int quantity) {
        try {
            
            double price = Double.parseDouble(priceStr);

            Connection connection = Connect.getInstance().getConnection();
            
            String query = "INSERT INTO msitem (itemName, itemCategory, itemDesc, itemPrice, itemStock) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, category);
            pst.setString(3, description);
            pst.setDouble(4, price);
            pst.setInt(5, quantity);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Item added to the database.");
            } else {
                System.out.println("Failed to add the item.");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert("An error occurred while adding the item to the database.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Insert Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
