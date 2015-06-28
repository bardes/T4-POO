package br.usp.icmc.mercado;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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
    //! Instância do singleton dessa classe
    private static final Servidor singleton = new Servidor();

    private Map<String, String> usuarios;
    private Map<String, String> produtos;
    private ServerSocket ss;

    private Servidor()
    {
        if(singleton != null)
            throw new IllegalStateException("Singleton violado!");
    
        try {
            carregaDados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega todos os dados do disco
     */
    public void carregaDados() throws IOException
    {
    }

    /**
     * Escreve todos os dados no disco.
     */
    public void escreveDados() throws IOException
    {
    }

    public void roda(int porta) throws InterruptedException
    {
        try {
            ss = new ServerSocket(porta);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Cria um executor pra cuidar dos manipuladores
        ExecutorService executor = Executors.newCachedThreadPool();

        while(!ss.isClosed()) {
            // Espera uma conexão ou sai se o socket fechar
            Socket s = null;
            try {
                s = ss.accept();
            } catch (IOException e) {
                if(!ss.isClosed())
                    e.printStackTrace();
                continue;
            }

            // Passa o Soket para uma thread lidar com a conexão
            executor.execute(new Manipulador(s));
        }

        // Interrompe todas as threads
        executor.shutdownNow();

        // Espera até 30s antes de fechar pra dar tempo de terminar todas as
        // requisições em aberto.
        if(!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            System.err.println("AVISO: Não conseguiu fechar todas as "
                               + "conexões antes de sair!");
        }
    }

    /**
     * Para o servidor "graciosamente".
     */
    public void para()
    {
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pega o singleton do servidor.
     */
    static public Servidor pegaServidor()
    {
        return singleton;
    }

    /**
     * Tenta fazer login do usuário especificado.
     */
    public Mensagem login(String id, String senha)
    {
        return null;
    }

    /**
     * Tenta fazer logut do usuário especificado.
     *
     * Se o usuário exixtir e o token for válido será realizado logut.
     * Caso contrário nada acontece.
     */
    public Mensagem logout(String id, String token)
    {
        return null;
    }

    /**
     * Adiciona um usuário.
     */
    public Mensagem adicionaUsuario(Usuario u)
    {
        return null;
    }

    /**
     * Adiciona um produto.
     */
    public Mensagem adicionaProduto(Produto p)
    {
        return null;
    }

    /**
     * Altera a quantidade em estoque de um produto.
     *
     * Essa função só pode ser usada para *aumentar* a quantidade disponível
     * de algum produto. Se um número negativo for passado nada acontece.
     */
    public Mensagem estocaProduto(String nome)
    {
        return null;
    }

    /**
     * Pega uma lista de produtos.
     *
     * @param filtro Indica quais produtos deve ser listados. Pode ser: "ALL",
     * "AVAILABlE" ou "UNAVAILABLE"
     * @param dest Lista onde os produtos devem ser adicionados.
     */
    public Mensagem listaProdutos(String filtro, List<Produto> dest)
    {
        return null;
    }

    /**
     * Tenta realizar a compra de um produto.
     */
    public Mensagem compraProduto(String id, String token, String prod)
    {
        return null;
    }

    public static void main(String[] args) {
        // Checa os argumentos
        if(args.length != 1) {
            System.out.println("Uso: java -jar T4.jar <porta>");
            System.exit(1);
        }

        // Tenta rodar o Server
        try {
            Servidor.pegaServidor().roda(Integer.parseInt(args[0]));
        } catch (NumberFormatException _e) {
            System.out.println("A porta especificada tem que ser um número!");
            System.exit(2);
        } catch (InterruptedException _e) {
            System.err.println("AVISO: Interrompido durante a terminação do "
                    + "servidor. Dados podem ter sido perdidos!");
        }

        // Antes de fechar escreve os dados no disco
        try {
            Servidor.pegaServidor().escreveDados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
