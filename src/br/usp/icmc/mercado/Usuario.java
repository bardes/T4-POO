package br.usp.icmc.mercado;

import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Base64;

public class Usuario implements Registro
{
    private static final Random rndGen = new Random(
            System.currentTimeMillis());

    private String id;
    private String nome;
    private String digesto;
    private String sal;
    private String end1, end2;
    private String email;
    private String tel;
    private String token;

    /**
     * Testa se o token dado é valido para esse usuário.
     */
    public boolean validaToken(String t)
    {
        return (token != null && token.equals(t)) ? true : false;
    }

    /**
     * Digere uma mensagem usando um sal e diversas iterações de sha384.
     */
    public static String digere(String msg, String sal, int itrs)
    {
        // No mínimo uma iteração
        if(itrs < 1) itrs = 1;

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
        if(digere(senha, sal, 1000).equals(digesto)) {
            byte[] tok = new byte[16];
            rndGen.nextBytes(tok);
            return Base64.encodeBase64String(tok);
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
