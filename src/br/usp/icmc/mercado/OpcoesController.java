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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sushi
 */
public class OpcoesController implements Initializable {
    @FXML
    private Button buy;
    @FXML
    private AnchorPane window;
    @FXML
    private Button cadastro;
    @FXML
    private Button listar;

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
    private void buy(ActionEvent event) throws IOException {
        Scene scn = window.getScene();
        Stage menu = (Stage) scn.getWindow();
        Stage stage = new Stage();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/res/ui/Comprar.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void opServer(ActionEvent event) throws IOException {
        Scene scn = window.getScene();
        Stage menu = (Stage) scn.getWindow();
        Stage stage = new Stage();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/res/ui/Operacoes.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void listar(ActionEvent event) throws IOException {
        Scene scn = window.getScene();
        Stage menu = (Stage) scn.getWindow();
        Stage stage = new Stage();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/res/ui/Listar.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();        
    }
    
}
