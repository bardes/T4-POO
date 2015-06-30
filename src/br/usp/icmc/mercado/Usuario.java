package br.usp.icmc.mercado;

import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;

public class Usuario implements Registro
{
    //! Número de iterações da função hash aplicada na digestão
    private static final int itrs = 1000;

    //! Gerador aleatório para produzir sais e tokens
    private static final Random rndGen = new Random(
            System.currentTimeMillis());

    // Dados de um usuário
    private String id;
    private String nome;
    private String digesto;
    private String sal;
    private String end1, end2;
    private String email;
    private String tel;
    private String token;

    String pegaId() {return id;}
    String pegaNome() {return nome;}
    String pegaDigesto() {return digesto;}
    String pegaSal() {return sal;}
    String pegaEnd1() {return end1;}
    String pegaEnd2() {return end2;}
    String pegaEmail() {return email;}
    String pegaTel() {return tel;}
    String pegaToken() {return token;}

    /**
     * Cria um usuário vazio.
     */
    public Usuario()
    {
        token = null;
    }

    /**
     * Cria um novo usuário à partir dos dados no map.
     */
    public static Usuario criaNovo(Map<String, String> dados)
    {
        Usuario novo = new Usuario();

        // Copia os dados básicos
        novo.id = dados.get("id");
        novo.nome = dados.get("name");
        novo.end1 = dados.get("addr1");
        novo.end2 = dados.get("addr2");
        novo.email = dados.get("email");
        novo.tel = dados.get("phone");

        // Gera um sal aleatório de 32 bytes
        byte[] s = new byte[32];
        rndGen.nextBytes(s);
        novo.sal = Base64.encodeBase64String(s);

        // Digere a senha usando o sal + hash
        novo.digesto = digere(dados.get("pass"), novo.sal);

        return novo;
    }

    /**
     * Testa se o token dado é valido para esse usuário.
     */
    public boolean validaToken(String t)
    {
        if(token == null || token.isEmpty()) return false;
        return token.equals(t) ? true : false;
    }

    /**
     * Digere uma mensagem usando um sal e diversas iterações de sha384.
     */
    public static String digere(String msg, String sal)
    {
        assert itrs > 0;

        // Decodifica e concatena o sal na mensagem
        byte[] s = Base64.decodeBase64(sal);
        byte[] concat = new byte[msg.length() + s.length];
        System.arraycopy(msg.getBytes(), 0, concat, 0, msg.length());
        System.arraycopy(s, 0, concat, msg.length(), s.length);

        // Aplica diversas iterações da função de hash
        for(int i = 0; i < itrs; ++i)
            concat = DigestUtils.sha384(concat);
        
        // Aplica uma última iteração e converte pra hexadecimal
        return Base64.encodeBase64String(concat);
    }

    /**
     * Faz login desse usuário.
     *
     * @return Token usado pra identificar o usuário após um login bem
     * sucedido, ou null em caso de falha.
     */
    public String login(String senha)
    {
        // Verifica os dados necessários antes de usá-los
        if(senha == null || sal == null || digesto == null)
            return null;

        // Valida a senha com base na "digestão" dela
        if(digere(senha, sal).equals(digesto)) {
            byte[] tok = new byte[16];
            rndGen.nextBytes(tok);
            return token = Base64.encodeBase64String(tok);
        }

        // Se não bateu retorna null
        return null;
    }

    /**
     * Faz logout invalidando o token atual.
     */
    public void logout()
    {
        token = null;
    }

    public Stack<String> pegaDados()
    {
        Stack<String> s = new Stack<String>();
        
        s.push(id);
        s.push(nome);
        s.push(digesto);
        s.push(sal);
        s.push(end1);
        s.push(end2);
        s.push(email);
        s.push(tel);
        return s;
    }

    public void carregaDados(Stack<String> dados)
    {
        tel = dados.pop();
        email = dados.pop();
        end2 = dados.pop();
        end1 = dados.pop();
        sal = dados.pop();
        digesto = dados.pop();
        nome = dados.pop();
        id = dados.pop();
    }
}
