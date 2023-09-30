package utfpr.edu.forcamultiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    private ServerSocket socket;
    
    public Servidor (ServerSocket socket){
        this.socket = socket;
    }
    
    public void iniciarSessao() throws IOException{
        while(!socket.isClosed()){
            /*Nesse ponto o servidor aguarda uma coneção quando um cliente
             se conecta o .accept retorna um socket*/
            //O cliente usa o Ojeto conexao para se conectar
            Socket conecao = socket.accept();
            System.out.println("Um cliente se conectou!");
            
            GerenciadorCliente gc = new GerenciadorCliente(conecao);
            /*Uma thread pode receber um objeto que contenha a interface RUNNABLE
            a thread ficará responsavel por rodar essa instância de forma separada
            e independente do resto do código*/
            Thread t = new Thread(gc);
            t.start();
            
        }//fim while
    }//fim iniciar sessão
    
    public void fecharSessao() throws IOException{
        if(socket != null){
            socket.close();
        }
    }
    
    public static void main(String[] args) throws IOException{
        ServerSocket socket = new ServerSocket(8080);//seta a porta
        Servidor servidor = new Servidor(socket);//instancia um objeto
        System.out.println("Servidor subiu!");
        servidor.iniciarSessao();//chama o metodo iniciar sessao
    }
    
    
}