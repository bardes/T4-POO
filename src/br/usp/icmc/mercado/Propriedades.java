/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.icmc.mercado;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author sushi
 */
public class Propriedades implements Serializable {
  
   
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty preco = new SimpleStringProperty();
    private final StringProperty estoque = new SimpleStringProperty();
    private final StringProperty disponivel = new SimpleStringProperty();
  
     
    
    public Propriedades(String nome, String preco, String estoque, String disponivel) {
      this.nome.set(nome);
      this.estoque.set(estoque);
      this.preco.set(preco);
      this.disponivel.set(disponivel);

    }

    public final StringProperty nomeProperty() {
      return this.nome;
    }

    public final String getNome() {
      return this.nomeProperty().get();
    }

    public final void setNome(String nome) {
      this.nomeProperty().set(nome);
    }

    public final StringProperty estoqueProperty() {
      return this.estoque;
    }

    public final String getEstoque() {
      return this.estoqueProperty().get();
    }

    public final void setEstoque(String estoque) {
      this.estoqueProperty().set(estoque);
    }

    public final StringProperty precoProperty() {
      return this.preco;
    }

    public final String getPreco() {
      return this.precoProperty().get();
    }

    public final void setPreco(String preco) {
      this.precoProperty().set(preco);
    }
    
    public final StringProperty disponivelProperty() {
      return this.disponivel;
    }

    public final String getDisponivel() {
      return this.disponivelProperty().get();
    }

    public final void setDisponivel(String disponivel) {
      this.disponivelProperty().set(disponivel);
    }

}
