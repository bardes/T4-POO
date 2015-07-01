/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.mercado;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sushi
 */
public class LoginController implements Initializable {
    @FXML
    private PasswordField pass;
    @FXML
    private Label ID;
    @FXML
    private Button login;
    @FXML
    private AnchorPane window;
    
    long token;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void login(ActionEvent event) throws IOException {
        
        //decobrir como salvar os dados e implementar
        
        
        
        Scene scn = window.getScene();
        Stage menu = (Stage) scn.getWindow();
        Stage stage = new Stage();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Opcoes.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
}
