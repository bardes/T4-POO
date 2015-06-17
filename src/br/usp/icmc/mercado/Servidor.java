package br.usp.icmc.mercado;

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
}
