package br.usp.icmc.mercado;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.csv.*;

/**
 * Classe responsável por receber as conexões e lidar com o banco de dados.
 * 
 * É responsabilidade do servidor manter as operações sincronizadas através
 * do uso de travas, que garante que apenas uma thread faça operações críticas
 * por vez.
 */

public class Servidor
{
    private LinkedHashMap<String, Usuario> usuarios;
    private LinkedHashMap<String, Produto> produtos;
    private ServerSocket ss;

    //TODO Especificar um caminho para os dados
    public Servidor()
    {
        usuarios = new LinkedHashMap<String, Usuario>();
        produtos = new LinkedHashMap<String, Produto>();

        // Tenta carregar os dados antes de criar o servidor.
        try {
            carregaDados();
        } catch (FileNotFoundException _e) {
            System.err.println("AVISO: Nenhum arquivo de dados encontrado. "
                    + "Criando banco de dados vazio.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stack<String> empilhaCSVRecord(CSVRecord r)
    {
        Stack<String> empilhado = new Stack<String>();
        for(String dado : r)
            empilhado.add(dado);
        return empilhado;
    }

    /**
     * Carrega todos os dados do disco
     */
    public void carregaDados() throws IOException
    {
        // Abrindo o arquivo
        CSVParser parser = CSVFormat.RFC4180.parse(
                           new FileReader("usuarios.csv"));

        // Esvazia os usuarios atuais
        usuarios.clear();

        // Percorre cada registro no arquivo
        for(CSVRecord r : parser)
        {
            Usuario novo = new Usuario();
            novo.carregaDados(empilhaCSVRecord(r));
            usuarios.put(r.get(1), novo);
        }

        // Abrindo o arquivo
        parser = CSVFormat.RFC4180.parse(new FileReader("produtos.csv"));

        // Esvazia os produtos atuais
        produtos.clear();

        // Percorre cada registro no arquivo
        for(CSVRecord r : parser)
        {
            Produto novo = new Produto();
            novo.carregaDados(empilhaCSVRecord(r));
            produtos.put(r.get(1), novo);
        }
    }

    /**
     * Escreve todos os dados no disco.
     */
    public void escreveDados() throws IOException
    {
        Registro.escreveRegistros(new FileWriter("produtos.csv"),
                produtos.values());
        Registro.escreveRegistros(new FileWriter("usuarios.csv"),
                usuarios.values());
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
     * Verifica se um par (usuario, token) é válido.
     */
    public boolean autentica(String id, String token) {
        if(!usuarios.containsKey(id))
            return false;
        else
            return usuarios.get(id).validaToken(token);
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
        if(produtos.containsKey(p.pegaNome()))
            return Mensagem.ERRO("Produto já cadastrado!");

        produtos.put(p.pegaNome(), p);
        return Mensagem.OK();
    }

    /**
     * Altera a quantidade em estoque de um produto.
     *
     * Essa função só pode ser usada para *aumentar* a quantidade disponível
     * de algum produto. Se um número negativo for passado nada acontece.
     */
    public Mensagem estocaProduto(String nome, long qtd)
    {
        // Checa se existe
        if(!produtos.containsKey(nome))
            return Mensagem.ERRO("Produto inexistente!");

        produtos.get(nome).estoca(qtd);
        Mensagem m = new Mensagem("STOCK");
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
    public Mensagem compraProduto(String prod)
    {
        // Checa se existe
        if(!produtos.containsKey(prod))
            return Mensagem.ERRO("Produto inexistente!");

        // Tenta reduzir a qtd em estoque
        if(produtos.get(prod).reduzEstoque())
            return Mensagem.OK();
        else
            return Mensagem.ERRO("Produto exgotado!");
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
