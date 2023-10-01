
package utfpr.edu.forcamultiplayer.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


class GerenciadorCliente implements Runnable{
    private Socket socket;
    /*Criamos um array para cada instancia dessa clase, o objetivo principal desse array
    é ficar de olho em cada cliente, para quando um cliente mandar uma mensagem possamos 
    percorrer o array e mandar a mensagem para todos os outros clientes do array*/
    public static ArrayList<GerenciadorCliente> clientes = new ArrayList<>();
    
    private BufferedReader receber;
    private BufferedWriter enviar;
    
    private String username;

    public GerenciadorCliente(Socket socket) {
        try{
            this.socket = socket;
            //Stream e buffer para leitura e escrita na rede
            this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.enviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //ao atribuir um valor para a String username, ela será lida pelo buffer
            this.username = receber.readLine();
            clientes.add(this);
        }catch(IOException e){
            fechaTudo(socket, receber, enviar);
        }//fim do trycatch
    }//fim do construtor

    @Override
    public void run() {
        /*Toda vez que estamos ouvindo alguma mensagem nosso programa
        bloqueia o restante do código, porém precisamos ser capaz de continuar a enviar mensagens 
        e ainda assim ficar ouvindo, para isso precisamos trabalhar com Threads, 
        do qual são capazes de executar o nosso código em blocos*/
        String msg;
        while(socket.isConnected()){
            try{
                msg = receber.readLine();
                transmitir(msg);
                System.out.println("TESTANDO O run");
            }catch(IOException e){
                fechaTudo(socket, receber, enviar);
            }
        }//fim while
    }

    private void transmitir(String msg) {
        //Para cada cliente no loop do while no método Run cria-se uma iteração
        for(GerenciadorCliente cliente:clientes){//for each entre todos clientes
            try{
                if(!cliente.username.equals(username)){//ignora o cliente que envio a mensagem
                    cliente.enviar.write(msg);//Serialização da mensagem e envia pela rede
                    cliente.enviar.newLine();//Quando o cliente apertar ENTER o programa entende que 
                    //a msg foi finalizada, dessa forma o buffer se adapta ao tamanho da mensagem
                    //e não fica sobrecarregado, e depois de enviado se esvazia automaticamente
                    cliente.enviar.flush();
                    System.out.println("TESTANDO O TRANSMITIR");
                }//fim IF
            }catch(IOException e){
                fechaTudo(socket, receber, enviar);
            }
        }//fim do for
        
    }
    
    public void removerCliente(){
        clientes.remove(this);
        transmitir("SSERVIDOR: "+username+" saiu da sala!");
    }
    
    public void fechaTudo(Socket socket, BufferedReader receber, BufferedWriter enviar){
        try{
            if(socket != null){
                socket.close();
            }
            if(enviar != null){
                enviar.close();
            }
            if(receber != null){
                receber.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }//fim trycatch
    }//fim fechatudo
}
