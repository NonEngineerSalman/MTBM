package com.example.demo;

import com.example.demo.utils.DatabaseConnection;
import com.example.demo.utils.FXMLScene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;

public class EditScreeningController {

    @FXML private TableView<Movie> editScreening_tableView;
    @FXML private TableColumn<Movie, String> editScreening_col_movieTitle;
    @FXML private TableColumn<Movie, String> editScreening_col_genre;
    @FXML private TableColumn<Movie, String> editScreening_col_duration;
    @FXML private TableColumn<Movie, String> editScreening_col_current;

    @FXML private ComboBox<String> editScreening_current;
    @FXML private TextField editScreening_search;
    @FXML private Button editScreening_updateBtn;
    @FXML private Button editScreening_deleteBtn;
    @FXML private Label editScreening_title;
    @FXML private ImageView editScreening_imageView;
    @FXML private AnchorPane editScreening_form;
    @FXML private Label username;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        editScreening_col_movieTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        editScreening_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        editScreening_col_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        editScreening_col_current.setCellValueFactory(new PropertyValueFactory<>("status")); // using date as placeholder


        editScreening_current.setItems(FXCollections.observableArrayList("Upcoming", "Now Showing","End Showing"));

        loadMoviesFromDatabase();

        editScreening_tableView.setOnMouseClicked(e -> {
            Movie movie = editScreening_tableView.getSelectionModel().getSelectedItem();
            if (movie != null) {
                editScreening_title.setText(movie.getTitle());
                editScreening_current.setValue(getCurrentStatus(movie.getId()));
                Image img = new Image("file:" + movie.getPosterPath(), true);
                editScreening_imageView.setImage(img);
            }
        });
        editScreening_updateBtn.setOnAction(e -> handleUpdateStatus());
    }

    private void loadMoviesFromDatabase() {
        movieList.clear();
        try (Connection conn = new DatabaseConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM movies")) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("duration"),
                        rs.getString("date"),
                        rs.getString("poster_path"),
                        rs.getString("status")
                );
                movieList.add(movie);
            }
            editScreening_tableView.setItems(movieList);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentStatus(int id) {
        try (Connection conn = new DatabaseConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT status FROM movies WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "Unknown";
    }

    @FXML
    void handleUpdateStatus() {
        Movie selected = editScreening_tableView.getSelectionModel().getSelectedItem();
        String status = editScreening_current.getValue();
        if (selected != null && status != null) {
            try (Connection conn = new DatabaseConnection().getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE movies SET status = ? WHERE id = ?")) {
                stmt.setString(1, status);
                stmt.setInt(2, selected.getId());
                stmt.executeUpdate();
                showAlert("Success", "Status updated.");
                selected.setStatus(status);
                editScreening_tableView.refresh();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update status.");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Navigation
    @FXML
    void handleAddMovieBtn(ActionEvent event) {
        switchScene("/com/example/demo/addmovies.fxml", event);
    }

    @FXML
    void handleAvailableMovieBtn(ActionEvent event) {
        switchScene("/com/example/demo/availablemovies.fxml", event);
    }

    @FXML
    void handleCustomersBtn(ActionEvent event) {
        switchScene("/com/example/demo/customers.fxml", event);
    }

    @FXML
    void handleDashboardBtn(ActionEvent event) {
        switchScene("/com/example/demo/dashboard.fxml", event);
    }

    @FXML
    void handleEditScreeningBtn(ActionEvent event) {
        switchScene("/com/example/demo/editscreening.fxml", event);
    }

    @FXML
    void handleSignoutBtn(ActionEvent event) {
        switchScene("/com/example/demo/login.fxml", event);
    }

    private void switchScene(String fxmlPath, ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load(fxmlPath);
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
