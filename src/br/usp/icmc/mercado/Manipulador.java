package br.usp.icmc.mercado;

import java.io.*;
import java.net.*;

/**
 * Classe responsável por lidar com as conexões de cada cliente.
 *
 * Para cada cliente que se conecta com o servidor é gerada uma instância desta
 * classe. Ela é resonsável por receber os comandos do cliente e invocar os
 * devidos comandos no servidor.
 */
class Manipulador implements Runnable
{
    private Socket con;
    private Servidor serv;
    private BufferedReader entrada;
    private Writer saida;

    public Manipulador(Socket con, Servidor serv)
    {
        this.con = con;
        this.serv = serv;

        try {
            entrada = new BufferedReader(new InputStreamReader(
                                           con.getInputStream()));
            saida = new OutputStreamWriter(con.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        if(con == null || entrada == null || saida == null) {
            System.err.println("ERRO: Manipulador inválido!");
            return;
        }

        while(!con.isClosed()) {
            // Lê a mensagem ou sai se a conexão foi fechada
            Mensagem m = null;
            try {
                m = Mensagem.le(entrada);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            // Verifica se conseguiu ler
            if(m == null) {
                try {
                    Mensagem.ERRO("Requisição inválida!").escreve(saida);
                } catch (SocketException e) { // Cliente desconectou
                    System.err.println("INFO: Cliente desconectou "
                            + "sem dar tchau :(");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            } else {
                try {
                    avaliaMensagem(m); // Tenta rodar o comando recebido
                } catch (SocketException _e) {
                    System.err.println("AVISO: Cliente desconectou sem "
                            + "receber a resposta!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } 
        
    }

    private void avaliaMensagem(Mensagem m) throws IOException
    {
        switch(m.comando) {
            case "LOGIN":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "pass")) break;

                serv.login(m.variaveis.get("id"),
                           m.variaveis.get("pass")).escreve(saida);
                break;

            case "LOGOUT":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "token")) break;
                serv.logout(m.variaveis.get("id"),
                            m.variaveis.get("token")).escreve(saida);
                break;

            case "ADD_USER":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "pass")) break;
                if(verificaVar(m, "name")) break;
                if(verificaVar(m, "addr1")) break;
                if(verificaVar(m, "addr2")) break;
                if(verificaVar(m, "email")) break;
                if(verificaVar(m, "phone")) break;
                serv.adicionaUsuario(Usuario.criaNovo(m.variaveis))
                                    .escreve(saida);
                break;

            case "ADD_PRODUCT":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "token")) break;
                if(verificaVar(m, "name")) break;
                if(verificaVar(m, "price")) break;
                if(verificaVar(m, "supplier")) break;
                if(verificaVar(m, "exp_date")) break;
                if(verificaVar(m, "stock")) break;

                if(verificaLogin(m.variaveis.get("id"),
                                 m.variaveis.get("token"))) break;
                
                Produto p = new Produto(m.variaveis.get("name"),
                                        m.variaveis.get("supplier"),
                                        m.variaveis.get("price"),
                                        m.variaveis.get("stock"),
                                        m.variaveis.get("exp_date"));

                serv.adicionaProduto(p).escreve(saida);
                break;

            case "STOCK_PRODUCT":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "token")) break;
                if(verificaVar(m, "name")) break;
                if(verificaVar(m, "add")) break;

                if(verificaLogin(m.variaveis.get("id"),
                                 m.variaveis.get("token"))) break;

                serv.estocaProduto(m.variaveis.get("name"),
                                   Long.parseLong(m.variaveis.get("add")))
                                   .escreve(saida);
                break;

            case "PRODUCT_LIST":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "token")) break;
                if(verificaVar(m, "filter")) break;

                if(verificaLogin(m.variaveis.get("id"),
                                 m.variaveis.get("token"))) break;

                serv.listaProdutos(m.variaveis.get("filter")).escreve(saida);
                break;

            case "BUY":
                if(verificaVar(m, "id")) break;
                if(verificaVar(m, "token")) break;
                if(verificaVar(m, "name")) break;

                if(verificaLogin(m.variaveis.get("id"),
                                 m.variaveis.get("token"))) break;

                serv.compraProduto(m.variaveis.get("name")).escreve(saida);
                break;

            case "BYE":
                try {
                    con.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "STOP":
                serv.para();
                break;

            default:
                Mensagem.ERRO("Comando desconhecido!").escreve(saida);
        }
    }

    private boolean verificaLogin(String usr, String tok) throws IOException
    {
        if(serv.autentica(usr, tok))
            return false;

        Mensagem.ERRO("Usuário e/ou token inválido.").escreve(saida);
        return true;
    }

    /**
     * Verifica se a variavel está presente e responde com um erro se não
     * estiver.
     */
    private boolean verificaVar(Mensagem m, String var) throws IOException
    {
        if(m.variaveis.containsKey(var)) {
            return false;
        } else {
            Mensagem.ERRO("Variável \"" + var
                        + "\" não foi encontrada.").escreve(saida);
            return true;
        }
    }
}
