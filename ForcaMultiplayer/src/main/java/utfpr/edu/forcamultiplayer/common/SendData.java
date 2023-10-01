
package utfpr.edu.forcamultiplayer.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;


public class SendData {
    
    public static void enviarLetra(InputData input, BufferedWriter buffer, Socket socket){
        try{
            var data = Enconder.enconder(input);
            //quando o enviar mensagem for executado pela primeira vez
            buffer.write(data);
            buffer.newLine();
            buffer.flush();
        }catch(IOException e){
            //fechaTudo(socket, null, buffer);
        }
    }//fim enviar mensagem
    
}
