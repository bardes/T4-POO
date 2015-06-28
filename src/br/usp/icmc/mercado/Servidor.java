package br.usp.icmc.mercado;
import java.io.*;
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

    /**
     * Tenta fazer login do usuário especificado.
     */
    public Mensagem login(String id, String senha)
    {
        return null;
    }

    /**
     * Verifica se o token dado é válido para esse usuário.
     */
    public boolean validaToken(String id, String token)
    {
        return false;
    }

    /**
     * Tenta fazer logut do usuário especificado.
     *
     * Se o usuário exixtir e o token for válido será realizado logut.
     * Caso contrário nada acontece.
     */
    public void logout(String id, String token)
    {
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

    /**
     * Pega o singleton do servidor.
     */
    static public Servidor pegaServidor()
    {
        return singleton;
    }

    private Servidor()
    {
        if(singleton != null)
            throw new IllegalStateException("Singleton violado!");


    }

    public static void main(String[] args) {
        // Checa os argumentos
        if(args.length != 1) {
            System.out.println("Uso: java -jar T4.jar <porta>");
            System.exit(1);
        }

        // Tenta criar o ServerSocket
        int porta;
        ServerSocket ss;
        try {
            porta = Integer.parseInt(args[0]);
            ss = new ServerSocket();
        } catch (NumberFormatException _e) {
            System.out.println("A porta especificada tem que ser um número!");
            System.exit(2);
        } catch (IOException e) {
            System.out.println("Não conseguiu criar o servidor.");
            System.out.println(e);
            System.exit(2);
        }

        // Cria um manipulador de threads.
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        do {
            // Espera uma conexão ou sai se o socket fechar
            Socket s;
            try {
                s = ss.accept();
            } catch (IOException e) {
                if(s.isClosed()) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Passa o Soket para uma thread lidar com a conexão
            executor.execute(new Manipulador(s));

        } while(true);

        // Fecha as conexões em aberto
        executor.shutDownNow();
        if(!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.err.println("AVISO: Não conseguiu fechar todas as conexões"
                    + " antes de sair!");
        }

        // Fecha o servidor
    }
}
