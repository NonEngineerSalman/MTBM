package com.example.demo;

import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.FXMLScene;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.itextpdf.text.Document;                    // iText PDF document



public class AvailableMoviesController {

    @FXML private Button availableMovies_buyBtn, availableMovies_clearBtn, availableMovies_receiptBtn, availableMovies_selectMovieBtn;
    @FXML private TableColumn<Movie, String> availableMovies_col_movieTitle, availableMovies_col_genre, availableMovies_col_showingDate;
    @FXML private TableView<Movie> availableMovies_tableView;
    @FXML private Label availableMovies_movieTitle, availableMovies_genre, availableMovies_date;
    @FXML private Label availableMovies_normalClass_price, availableMovies_specialClass_price, availableMovies_total;
    @FXML private Spinner<Integer> availableMovies_normalClass_quantity, availableMovies_specialClass_quantity;
    @FXML private ImageView availableMovies_imageView;
    @FXML private AnchorPane availableMovies_form;
    @FXML private Label username;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();

    private int normalPrice = 150;
    private int specialPrice = 300;

    private Movie lastPurchasedMovie;
    private int lastNormalQty = 0;
    private int lastSpecialQty = 0;
    private int lastTotal = 0;

    @FXML
    public void initialize() {
        availableMovies_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        availableMovies_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        availableMovies_col_showingDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        availableMovies_tableView.setItems(movieList);

        availableMovies_normalClass_price.setText("₹" + normalPrice);
        availableMovies_specialClass_price.setText("₹" + specialPrice);

        availableMovies_normalClass_quantity.setValueFactory(new IntegerSpinnerValueFactory(0, 10, 0));
        availableMovies_specialClass_quantity.setValueFactory(new IntegerSpinnerValueFactory(0, 10, 0));

        loadMoviesFromDatabase();
        setupSelectionListener();
        setupQuantityListeners();

        availableMovies_buyBtn.setOnAction(e -> handleBuyNow());
        availableMovies_receiptBtn.setOnAction(e -> showReceipt());
    }

    private void loadMoviesFromDatabase() {
        movieList.clear();
        String sql = "SELECT * FROM movies";
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movieList.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("duration"),
                        rs.getString("date"),
                        rs.getString("poster_path")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSelectionListener() {
        availableMovies_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                availableMovies_movieTitle.setText(newVal.getTitle());
                availableMovies_genre.setText(newVal.getGenre());
                availableMovies_date.setText(newVal.getDate());

                if (newVal.getPosterPath() != null) {
                    availableMovies_imageView.setImage(new Image("file:" + newVal.getPosterPath()));
                }

                availableMovies_normalClass_quantity.getValueFactory().setValue(0);
                availableMovies_specialClass_quantity.getValueFactory().setValue(0);
                availableMovies_total.setText("₹ 0");
            }
        });
    }

    private void setupQuantityListeners() {
        ChangeListener<Number> listener = (obs, oldVal, newVal) -> calculateTotal();
        availableMovies_normalClass_quantity.valueProperty().addListener(listener);
        availableMovies_specialClass_quantity.valueProperty().addListener(listener);
    }

    private void calculateTotal() {
        int normalQty = availableMovies_normalClass_quantity.getValue();
        int specialQty = availableMovies_specialClass_quantity.getValue();
        int total = (normalQty * normalPrice) + (specialQty * specialPrice);

        availableMovies_total.setText("₹ " + total);
    }

    private void handleBuyNow() {
        Movie selected = availableMovies_tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a movie first.");
            return;
        }

        int normalQty = availableMovies_normalClass_quantity.getValue();
        int specialQty = availableMovies_specialClass_quantity.getValue();

        if (normalQty == 0 && specialQty == 0) {
            showAlert(Alert.AlertType.WARNING, "Please select ticket quantities.");
            return;
        }

        int total = (normalQty * normalPrice) + (specialQty * specialPrice);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Purchase");
        confirm.setHeaderText("Confirm Ticket Purchase");
        confirm.setContentText(
                "Movie: " + selected.getTitle() + "\n" +
                        "Normal Tickets: " + normalQty + " × ₹" + normalPrice + "\n" +
                        "Special Tickets: " + specialQty + " × ₹" + specialPrice + "\n" +
                        "Total: ₹" + total + "\n\nProceed with purchase?"
        );

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                savePurchaseToDatabase(selected.getId(), normalQty, specialQty, total);
                lastPurchasedMovie = selected;
                lastNormalQty = normalQty;
                lastSpecialQty = specialQty;
                lastTotal = total;

                showAlert(Alert.AlertType.INFORMATION, "Purchase Successful!");
                availableMovies_normalClass_quantity.getValueFactory().setValue(0);
                availableMovies_specialClass_quantity.getValueFactory().setValue(0);
                availableMovies_total.setText("₹ 0");
            }
        });
    }

    private void savePurchaseToDatabase(int movieId, int normalQty, int specialQty, int total) {
        String sql = "INSERT INTO purchases (movie_id, normal_qty, special_qty, total_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ps.setInt(2, normalQty);
            ps.setInt(3, specialQty);
            ps.setInt(4, total);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showReceipt() {
        if (lastPurchasedMovie == null) {
            showAlert(Alert.AlertType.WARNING, "No recent purchase found.");
            return;
        }

        Stage receiptStage = new Stage();
        VBox receiptBox = new VBox(10);
        receiptBox.setStyle("-fx-padding: 20; -fx-background-color: white;");
        receiptBox.getChildren().addAll(
                new Label("🎟️ Movie Ticket Receipt"),
                new Label("Movie: " + lastPurchasedMovie.getTitle()),
                new Label("Date: " + lastPurchasedMovie.getDate()),
                new Label("Normal Tickets: " + lastNormalQty + " × ₹" + normalPrice),
                new Label("Special Tickets: " + lastSpecialQty + " × ₹" + specialPrice),
                new Label("Total Paid: ₹" + lastTotal)
        );

        Button printBtn = new Button("Print");
        Button exportBtn = new Button("Export to PDF");
        receiptBox.getChildren().addAll(printBtn, exportBtn);

        printBtn.setOnAction(e -> printNode(receiptBox));
        exportBtn.setOnAction(e -> exportReceiptToPDF(receiptBox));

        receiptStage.setScene(new Scene(receiptBox));
        receiptStage.setTitle("Receipt");
        receiptStage.show();
    }

    private void printNode(Node node) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            job.printPage(pageLayout, node);
            job.endJob();
        }
    }

    private void exportReceiptToPDF(Node node) {
        try {
            WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Receipt");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                File tempImage = new File("temp_receipt.png");
                ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(snapshot, null), "png", tempImage);

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance(tempImage.getAbsolutePath());
                img.scaleToFit(500, 700);
                document.add(img);
                document.close();
                tempImage.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Navigation
    @FXML void handleAddMovieBtn(ActionEvent event) { switchScene(event, "/com/example/demo/addmovies.fxml"); }
    @FXML void handleAvailableMovieBtn(ActionEvent event) { switchScene(event, "/com/example/demo/availablemovies.fxml"); }
    @FXML void handleCustomersBtn(ActionEvent event) { switchScene(event, "/com/example/demo/customers.fxml"); }
    @FXML void handleDashboardBtn(ActionEvent event) { switchScene(event, "/com/example/demo/dashboard.fxml"); }
    @FXML void handleEditScreeningBtn(ActionEvent event) { switchScene(event, "/com/example/demo/editscreening.fxml"); }
    @FXML void handleSignoutBtn(ActionEvent event) { switchScene(event, "/com/example/demo/login.fxml"); }

    private void switchScene(ActionEvent event, String path) {
        FXMLScene fxmlScene = FXMLScene.load(path);
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
