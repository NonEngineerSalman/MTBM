package com.example.demo;

import com.example.demo.utils.FXMLScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField SignUp_email;

    @FXML
    private AnchorPane SignUp_form;

    @FXML
    private Label label;

    @FXML
    private Button signUp_Btn;

    @FXML
    private Hyperlink signUp_alreadyHaveAccount;

    @FXML
    private PasswordField signUp_password;

    @FXML
    private TextField signUp_username;

    @FXML
    private Hyperlink signin_createAccount;

    @FXML
    private AnchorPane signin_form;

    @FXML
    private Button signin_loginBtn;

    @FXML
    private PasswordField signin_password;

    @FXML
    private TextField signin_username;

    @FXML
    void loginHandle(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/register.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void creatAccount(ActionEvent event) {
        FXMLScene fxmlScene = FXMLScene.load("/com/example/demo/register.fxml");
        Scene scene = new Scene(fxmlScene.getRoot());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
