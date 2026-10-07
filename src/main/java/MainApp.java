import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    // 2. Create ObservableList to hold Customer objects[cite: 1, 2]
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Manager");

        // 1. Build form with name field and province list[cite: 1, 2]
        Label nameLabel = new Label("Customer Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        nameLabel.setLabelFor(nameField);

        ComboBox<String> provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll(
            "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka", 
            "Muchinga", "Northern", "North-Western", "Southern", "Western"
        );
        provinceBox.setPromptText("Choose a province");

        Button saveButton = new Button("Save Customer");
        saveButton.setDefaultButton(true); // 'Enter' activates save[cite: 2]

        Button deleteButton = new Button("Delete Selected");

        Label statusLabel = new Label();

        HBox formBox = new HBox(10, nameLabel, nameField, provinceBox, saveButton, deleteButton);
        formBox.setPadding(new Insets(10));

        // 3. Add TableView with name and province columns[cite: 1, 2]
        TableView<Customer> table = new TableView<>();
        table.setItems(customerList);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        table.getColumns().addAll(nameCol, provinceCol);

        // 4. Validate input, then add customer[cite: 1, 2]
        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            if (name.isEmpty()) {
                statusLabel.setText("Please enter a customer name.");
                nameField.requestFocus();
                return;
            }

            if (province == null) {
                statusLabel.setText("Please select a province.");
                provinceBox.requestFocus();
                return;
            }

            customerList.add(new Customer(name, province));
            statusLabel.setText("Customer saved successfully.");
            nameField.clear();
            provinceBox.setValue(null);
        });

        // 5. Confirm deletion of a selected customer[cite: 1, 2]
        deleteButton.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                statusLabel.setText("Please select a customer to delete.");
                return;
            }

            ButtonType deleteBtn = new ButtonType("Delete");
            Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, 
                "Are you sure you want to delete this customer?", 
                deleteBtn, 
                ButtonType.CANCEL
            );
            alert.setHeaderText("Confirm Deletion");

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == deleteBtn) {
                customerList.remove(selected);
                statusLabel.setText("Customer deleted.");
            }
        });

        VBox layout = new VBox(10, formBox, table, statusLabel);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 650, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 6. Focus field on startup for keyboard access[cite: 2]
        nameField.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}