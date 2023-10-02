package utfpr.edu.forcamultiplayer.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import utfpr.edu.forcamultiplayer.common.Decoder;
import utfpr.edu.forcamultiplayer.common.Operacao;
import utfpr.edu.forcamultiplayer.common.SendData;


public class Servidor {

    public static void main(String[] args) throws IOException{
        serverChanel();
    }

    private static void serverChanel(){
        try{
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);

            // Crie um seletor para lidar com eventos de E/S assíncronos
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Servidor ouvindo na porta 8080...");

            while (true) {
                // Aguarde eventos de E/S
                selector.select();

                // Obtém as chaves de seleção disponíveis
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // Aceita uma nova conexão
                        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Nova conexão aceita de " + clientChannel.getRemoteAddress());
                    } else if (key.isReadable()) {
                        // Lê dados do cliente
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientChannel.read(buffer);

                        if (bytesRead == -1) {
                            // A conexão foi encerrada pelo cliente
                            clientChannel.close();
                            System.out.println("Conexão encerrada por " + clientChannel.getRemoteAddress());
                        } else if (bytesRead > 0) {
                            buffer.flip();
                            byte[] data = new byte[bytesRead];
                            buffer.get(data);
                            String message = new String(data);
                            var inputData = Decoder.decoder(message);
                            var outPut = Operacao.operacao(inputData, clientChannel);
                            SendData.sendData(clientChannel, outPut, selector);
                        }
                    }
                    
                    keyIterator.remove();
                }
            }
        } catch (Exception ex){

        }

    }
}