package utfpr.edu.forcamultiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
    private BufferedReader receber;
    private BufferedWriter enviar;
    
    private String username;
    private Socket socket;

    public Cliente(String username, Socket socket) {
        try{
            this.username = username;
            this.socket = socket;
            this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.enviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            fechaTudo(socket, receber, enviar);
        }        
    }
    //esse método envia uma mensagem para o gerenciador de cliente que distribuira a mensagem
    //para os outros cliente do ArrayList
    
    public void enviarMsg(){
        try{
            //quando o enviar mensagem for executado pela primeira vez
            enviar.write(username);
            enviar.newLine();
            enviar.flush();
            Scanner scan = new Scanner(System.in);
            while(socket.isConnected()){
                String msg = scan.nextLine();
                enviar.write(username+": "+msg);
                enviar.newLine();
                enviar.flush();
            }
        }catch(IOException e){
            fechaTudo(socket, receber, enviar);
        }
    }//fim enviar mensagem
    
    public void receberMsg(){
        new Thread(new Runnable(){
            @Override
                public void run(){
                    String msgDoChat;
                    while(socket.isConnected()){
                        try{
                            msgDoChat = receber.readLine();
                            System.out.println(msgDoChat);
                        }catch(IOException e){
                            fechaTudo(socket, receber, enviar);
                        }//fim trycatch
                    }//fim while
                }//run
            }//runnable
        ).start();//Thread
    }//fim receber msg
    
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
    }
    
    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("Digite seu nome de usuário: ");
        String username = scan.nextLine();
        Socket socket = new Socket("localhost",8080);
        Cliente cliente = new Cliente(username, socket);
        cliente.receberMsg();//bloco com thread, sempre irá executar a parte
        cliente.enviarMsg();
    }
    
    
}
