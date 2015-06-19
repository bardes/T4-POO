package br.usp.icmc.mercado;
import java.io.*;
import java.util.*;

/**
 * Classe responsável por receber as conexões e lidar com o banco de dados.
 * 
 * O servidor é uma classe SINGLETON, assim todas as threads que estão lidando
 * com os possíveis vários clientes lidam sempre com o mesmo servidor. Além
 * disso operações que devem ser sincronizadas podem usar a trava do server,
 * que garante que apenas uma thread faça operações críticas por vez.
 */

public class Servidor
{
    public static void main(String[] args) throws Exception {
        Mensagem m = Mensagem.le(new BufferedReader(new FileReader(args[0])));
        System.out.println(m == null ? "Falhou!" : m);

        for(Map.Entry<String, String> e : m.variaveis.entrySet()) {
            System.out.println(">>> " + e.getKey() + "=" + e.getValue());
        }
    }
}
