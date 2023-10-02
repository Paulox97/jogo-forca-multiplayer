
package utfpr.edu.forcamultiplayer.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
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
    
    public static void sendData(SocketChannel socket, InputData input, Selector select){
        try {
            var message = Enconder.enconder(input);
            ByteBuffer byteBufer = ByteBuffer.wrap(message.getBytes());
            socket.register(select, SelectionKey.OP_WRITE, byteBufer);
        } catch(Exception e){
            
        }
        
    }
    
}
