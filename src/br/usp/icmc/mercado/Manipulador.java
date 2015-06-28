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
class Manipulador extends Thread
{
    Socket con;

    public Manipulador(Socket s)
    {
        super();
        con = s;
    }

    @Override
    public void interrupt()
    {
        synchronized(con) {
            // TODO enviar msg de desconn.
            try {
                con.close();
                System.err.println("AVISO: fechamento forçado da conexão " +
                                    con);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.interrupt();
    }

    @Override
    public void run()
    {
        // TODO synchronized para garantir que não vai fechar no meio de uma resposta
        Servidor s = Servidor.pegaServidor();
        
    }
}
