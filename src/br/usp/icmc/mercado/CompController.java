/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.mercado;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author sushi
 */
public class CompController{
    
    @FXML
    private TableView<Produto> products;
    @FXML
    private TextField inputnome;
    @FXML
    private TextField inputestoque;
    @FXML
    private TextField total;

    
    int index = 0;
    long vtotal = 0;
 
    @FXML
    private void comprar(ActionEvent event) {
        //TODO
        //efetuar compra
                
    }

    @FXML
    private void notificar(ActionEvent event) {
        //TODO
        //envia notificacao quando os produtos estiverem indisponiveis
    }

    @FXML
    private void add(ActionEvent event) {
        //adiciona os produtos na lista para compra
        //descobrir como receber dados e impkementar
        ObservableList<Produto> data = products.getItems();
                
        if(data.get(index).getDisponivel() == 4)
        {
            vtotal += data.get(index).getDisponivel();
            ++index;
        }
        total.setText(Long.toString(vtotal));
        
    }

   
}
