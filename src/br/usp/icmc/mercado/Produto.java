package br.usp.icmc.mercado;
import javafx.beans.property.*;
import java.util.*;

public class Produto 
{
    String fornecedor;
    long validade;
    private final SimpleStringProperty nome = new SimpleStringProperty("");
    private final SimpleLongProperty preco = new SimpleLongProperty();
    private final SimpleLongProperty estoque = new SimpleLongProperty();
    private final SimpleLongProperty disponivel = new SimpleLongProperty();
    
    public Produto(){}
        
    public Produto(String nome, String fornecedor, String validade, long preco, long estoque, long disponivel)
    {
        setNome(nome);
        setPreco(preco);
        setEstoque(estoque);
        setDisponivel(disponivel);
        this.fornecedor = fornecedor;
        this.validade = Long.parseLong(validade);
    }

    String getFornecedor() {return fornecedor;}
    long getValidade() {return validade;}
    public final String getNome(){return nome.get();}
    public final long getPreco() {return preco.get();}
    public final long getEstoque() {return estoque.get();}
    public final long getDisponivel() {return disponivel.get();}
    public final void setNome(String Nome) {nome.set(Nome);}
    public final void setPreco(long Preco) {preco.set(Preco);}
    public final void setEstoque(long Estoque) {estoque.set(Estoque);}
    public final void setDisponivel(long Disponivel) {disponivel.set(Disponivel);}
    
    public long estoca(long qtd)
    {
        estoque.add(qtd);
        return estoque.get();
    }

    public boolean reduzEstoque(long qtde)
    {
        if(estoque.get() <= 0)
            return false;

        estoque.subtract(qtde);
        return true;
    }
    
    public void carregaDados(Stack<String> dados)
    {
        validade = Long.parseLong(dados.pop());
        estoque.set(Long.parseLong(dados.pop()));
        preco.set(Long.parseLong(dados.pop()));
        fornecedor = dados.pop();
        nome.set(dados.pop());
    }

    public Stack<String> pegaDados()
    {
        Stack<String> s = new Stack<>();
        s.push(nome.get());
        s.push(Long.toString(preco.get()));
        s.push(Long.toString(estoque.get()));
        return s;
    }


}
