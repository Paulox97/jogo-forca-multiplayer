
package utfpr.edu.forcamultiplayer.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;


public class ReceiveData {
    public static void receberLetra(Socket socket, BufferedReader buffer){
        new Thread(new Runnable(){
            @Override
                public void run(){
                    while(socket.isConnected()){
                        try{
                            var returnData = Decoder.decoder(buffer.readLine());
                            System.out.println(returnData.getInput());
                        }catch(IOException e){
                            //fechaTudo(socket, buffer, enviar);
                        }//fim trycatch
                    }//fim while
                }//run
            }//runnable
        ).start();//Thread
    }//fim receber msg
}
