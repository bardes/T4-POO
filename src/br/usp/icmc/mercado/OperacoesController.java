/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.mercado;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author sushi
 */
public class OperacoesController implements Initializable {
    @FXML
    private Button save;
    @FXML
    private PasswordField password;
    @FXML
    private TextField email;
    @FXML
    private TextField ID;
    @FXML
    private TextField telefone;
    @FXML
    private TextField comp;
    @FXML
    private TextField End;
    @FXML
    private TextField Nome;
    
    

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
    private void save(ActionEvent event) {
        //descobrir como salvar os dados e implementar
                
        
    }
    
}
