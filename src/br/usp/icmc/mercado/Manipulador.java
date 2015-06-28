package br.usp.icmc.mercado;
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
    public Manipulador(Socket s)
    {
    }
}
