package br.usp.icmc.mercado;

import java.util.*;

public class Produto implements Registro
{
    String nome;
    String fornecedor;
    long preco;
    long estoque;
    long validade;

    String pegaNome() {return nome;}
    String pegaFornecedor() {return fornecedor;}
    long pegaEstoque() {return estoque;}
    long pegaPreco() {return preco;}
    long pegaValidade() {return validade;}

    public void estoca(long qtd)
    {
        estoque += qtd;
        // Impede quantidaes negativas
        estoque = estoque < 0 ? 0 : estoque;
    }

    public boolean reduzEstoque()
    {
        if(estoque <= 0)
            return false;

        estoque -= 1;
        return true;
    }

    public Produto() {}

    public Produto(String nome, String fornecedor, String preco,
            String estoque, String validade)
    {
        this.nome = nome;
        this.fornecedor = fornecedor;
        this.preco = Long.parseLong(preco);
        this.estoque = Long.parseLong(estoque);
        this.validade = Long.parseLong(validade);
    }

    public void carregaDados(Stack<String> dados)
    {
        validade = Long.parseLong(dados.pop());
        estoque = Long.parseLong(dados.pop());
        preco = Long.parseLong(dados.pop());
        fornecedor = dados.pop();
        nome = dados.pop();
    }

    public Stack<String> pegaDados()
    {
        Stack<String> s = new Stack<String>();
        s.push(nome);
        s.push(fornecedor);
        s.push(Long.toString(preco));
        s.push(Long.toString(estoque));
        s.push(Long.toString(validade));
        return s;
    }
}
