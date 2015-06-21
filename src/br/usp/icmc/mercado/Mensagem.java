package br.usp.icmc.mercado;
import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Representa uma mensagem trocada entre o servidor e um cliente.
 */
public class Mensagem
{
    public String comando;
    public Map<String, String> variaveis;

    private Mensagem() {
        comando = null;
        variaveis = new HashMap<String, String>();
    }

    private static void esvazia(BufferedReader entrada) throws IOException {
        String s;
        while((s = entrada.readLine()) != null)
            if(s.equals("")) return;
    }

    public static Mensagem le(BufferedReader entrada) throws IOException {
        Mensagem msg = new Mensagem();

        do { // Ignora quebras de linha iniciais
            msg.comando = entrada.readLine();
            if(msg.comando == null) return null;
        } while(msg.comando.equals(""));

        // Valida o comando
        if(!Pattern.matches("^[A-Z][A-Z_]*$", msg.comando)) {
            esvazia(entrada);
            return null;
        }

        // Lê as variáveis
        String linha;
        Pattern varDef = Pattern.compile("^([A-Za-z_][\\w]*):(\\p{Print}*)$");
        do {
            linha = entrada.readLine();
            if(linha == null)
                return null;
            else if (linha.equals(""))
                break;

            Matcher m = varDef.matcher(linha);
            if(!m.matches()) {
                esvazia(entrada);
                return null;
            }

            // Guarda a variável achada no map
            msg.variaveis.put(m.group(1), m.group(2));
        } while(true);

        return msg;
    }

    @Override
    public String toString() {
        return "<Mensagem: " + comando + " +" + variaveis.size() + " vars>";
    }
}