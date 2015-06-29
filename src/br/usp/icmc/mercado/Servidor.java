package br.usp.icmc.mercado;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Classe responsável por receber as conexões e lidar com o banco de dados.
 * 
 * É responsabilidade do servidor manter as operações sincronizadas através
 * do uso de travas, que garante que apenas uma thread faça operações críticas
 * por vez.
 */

public class Servidor
{
    private Map<String, Usuario> usuarios;
    private Map<String, Produto> produtos;
    private ServerSocket ss;

    //TODO Especificar um caminho para os dados
    public Servidor()
    {
        usuarios = new HashMap<String, Usuario>();
        produtos = new HashMap<String, Produto>();

        // Tenta carregar os dados antes de criar o servidor.
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

    /**
     * Executa o processo do servidor.
     *
     * Essa função só retorna quando alguém chama o método para().
     */
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
                if(ss.isClosed()) { // Servidor deve parar
                    break;
                } else { // Foi uma falha de IO
                    e.printStackTrace();
                    continue;
                }
            }

            // Passa o Soket para uma thread lidar com a conexão
            executor.execute(new Manipulador(s, this));
        }

        // Espera todos os manipuladores terminarem
        System.out.println("AVISO: Aguardando todos os clientes sairem " +
                           "antes de fechar.");
        executor.shutdown();
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
     * Tenta fazer login do usuário especificado.
     */
    public Mensagem login(String id, String senha)
    {
        if(!usuarios.containsKey(id))
            return Mensagem.ERRO("Usuário e/ou senha inválidos.");

        String tok = usuarios.get(id).login(senha);

        if(tok == null)
            return Mensagem.ERRO("Usuário e/ou senha inválidos.");
        
        Mensagem m = new Mensagem();
        m.comando = "OK";
        m.variaveis.put("token", tok);
        return m;
    }

    /**
     * Tenta fazer logut do usuário especificado.
     *
     * Se o usuário exixtir e o token for válido será realizado logut.
     * Caso contrário nada acontece.
     */
    public Mensagem logout(String id, String token)
    {
        if(!usuarios.containsKey(id))
            return Mensagem.ERRO("Usuário e/ou token inválidos.");

        if(!usuarios.get(id).validaToken(token))
            return Mensagem.ERRO("Usuário e/ou token inválidos.");

        usuarios.get(id).logout();
        return Mensagem.OK();
    }

    /**
     * Adiciona um usuário.
     */
    public Mensagem adicionaUsuario(Usuario u)
    {
        if(usuarios.containsKey(u.pegaId()))
            return Mensagem.ERRO("Usuário já existe!");

        usuarios.put(u.pegaId(), u);
        return Mensagem.OK();
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
    public Mensagem listaProdutos(String filtro)
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

        // Tenta rodar o Servidor
        Servidor s = new Servidor();
        try {
            s.roda(Integer.parseInt(args[0]));
        } catch (NumberFormatException _e) {
            System.out.println("A porta especificada tem que ser um número!");
            System.exit(2);
        } catch (InterruptedException _e) {
            System.err.println("AVISO: Interrompido durante a terminação do "
                    + "servidor. Dados podem ter sido perdidos!");
        }

        // Antes de fechar escreve os dados no disco
        try {
            s.escreveDados();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
