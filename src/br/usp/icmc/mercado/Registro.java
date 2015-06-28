package br.usp.icmc.mercado;

import org.apache.commons.csv.*;
import java.util.*;
import java.io.*;

/**
 * Representa um registro que pode ser lido/escrito num arquivoi csv.
 *
 * **Aviso:** Cuidado com a ordem dos dados na pilha. Para carregar o
 * Registro de volta na memória os dados serão desempilhados em ordem
 * reversa à que foram adicionados.
 */
public interface Registro
{
    /**
     * Gera uma pilha com os dados de um registro.
     *
     * Essa pilha deve conter todos os dados necessários para
     * criar e recuperar um registro válido.
     *
     * @see #carregaDados()
     */
    Stack<String> pegaDados();

    /**
     * Carrega os dados de um registro.
     *
     * Essa função sobrescreve os dados atuais do registro com os dados
     * encontrados na pilha. A pilha deve ser organizada da mesma forma
     * que a pilha gerada pela função #pegaDados().
     *
     * @see #pegaDados()
     */
    void carregaDados(Stack<String> dados);

    /**
     * Escreve uma sequencia de registros em formato CSV.
     */
    static void
    escreveRegistros(Appendable dest, Iterable<Registro> regs)
    throws FileNotFoundException, IOException
    {
        // Abre o arquivo e cria um "printer" para manipular a saída
        CSVPrinter saida = new CSVPrinter(dest, CSVFormat.RFC4180);

        // Escreve cada registro no arquivo
        for(Registro r : regs) {
            saida.print(r.getClass().getName()); // Escreve o tipo do obj.
            saida.printRecord(r.pegaDados());    // Seguido pelos dados
        }

        saida.close();
    }


    /**
     * Carrega uma sequencia de registros no formato CSV.
     */
    static void
    carregaRegistros (Reader fonte, LinkedHashMap<String, Registro> regs)
    throws FileNotFoundException, IOException
    {
        // Cria um parser para ler o arquivo .csv
        CSVParser parser = CSVFormat.RFC4180.parse(fonte);

        // Esvazia os registros atuais
        regs.clear();

        // Percorre cada registro no arquivo
        for(CSVRecord r : parser)
        {
            // Tenta instanciar um ojeto do tipo lido no arquivo.
            Object novoObj; 
            try {
                novoObj = Class.forName(r.get(0)).newInstance();
            } catch (Exception e) {
                System.err.println("Aviso: Ignorando registro inválido.");
                e.printStackTrace();
                continue;
            }

            if(novoObj instanceof Registro) {
                Registro novo = (Registro) novoObj;
                novo.carregaDados(empilhaCSVRecord(r));
                regs.put(r.get(1), novo);
            } else {
                System.err.println("Aviso: Registro ignorado! " + novoObj + 
                        " não implementa a interface Registro.");
            }
        }
    }

    static Stack<String> empilhaCSVRecord(CSVRecord r)
    {
        Stack<String> empilhado = new Stack<String>();
        for(String dado : r)
            empilhado.add(dado);
        return empilhado;
    }
}
