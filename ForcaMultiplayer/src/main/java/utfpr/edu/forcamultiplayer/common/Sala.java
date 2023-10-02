package utfpr.edu.forcamultiplayer.common;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;



public class Sala {
    private String token;
    private List<SocketChannel> clientes = new ArrayList();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<SocketChannel> getClientes() {
        return clientes;
    }

    public void setClientes(List<SocketChannel> clientes) {
        this.clientes = clientes;
    }
    
    
}
