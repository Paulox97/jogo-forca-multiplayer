
package utfpr.edu.forcamultiplayer.common;

import java.nio.channels.SocketChannel;
import utfpr.edu.forcamultiplayer.server.utils.Utils;


public class Operacao {
    
    public static InputData operacao(InputData input, SocketChannel channel){
        switch (input.getOperacao()){
            case 1: //criar sala
                var token = Utils.gerarCaracteres();
                Salas.clients.put(channel.validOps(), token);
                var sala = new Sala();
                sala.setToken(token);
                sala.getClientes().add(channel);
                Salas.sala.add(sala);
                input.setToken(token);
                break;
            default :
                break;
                
        }
        return input;
    }
    
}
