package utfpr.edu.forcamultiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class ClienteForca {
    private BufferedReader receber;
    private BufferedWriter enviar;
    
    private String username;
    private Socket socket;
    
    public ClienteForca(String username, Socket socket) {
        try{
            this.username = username;
            this.socket = socket;
            this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.enviar = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){
            fechaTudo(socket, receber, enviar);
        }        
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
    }
    
    public void enviarLetra(){
        try{
            //quando o enviar mensagem for executado pela primeira vez
            enviar.write(username);
            enviar.newLine();
            enviar.flush();
            Scanner scan = new Scanner(System.in);
            if(socket.isConnected()){
                String letraUsuario = scan.next();
                scan.nextLine();
                while (letraUsuario.length() > 1){
                    System.out.println("VOCÊ DIGITOU MAIS DE UMA LETRA, POR GENTILEZA DIGITE SOMENTE UMA: ");
                    letraUsuario = scan.next();
                    scan.nextLine();
                }
                enviar.write(username+": "+ letraUsuario);
                enviar.newLine();
                enviar.flush();
            }
        }catch(IOException e){
            fechaTudo(socket, receber, enviar);
        }
    }//fim enviar mensagem
    
    public void receberLetra(){
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

    public static void main(String[] args) throws IOException {
        
        //Pegando o nome do usuário que irá participar do jogo
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Digite seu nome de usuário: ");
        String username = scan.nextLine();
        Socket socket = new Socket("localhost",8080);
        ClienteForca clienteForca = new ClienteForca(username, socket);
        clienteForca.receberLetra();
        
        
        //Instancias e variaveis necessárias para o jogo
        
        Palavras palavra = new Palavras();
        Dificuldade dificuldade = new Dificuldade();
        LetrasJogador letraEscolhida = new LetrasJogador();
        Caracteres caracteres = new Caracteres();
        StringBuilder palavraEscolhida = new StringBuilder();
        Set<Character> letrasDigitadas = new HashSet<>();
        VidaJogador vidaJogador = new VidaJogador();
        
        int escolhaJogador;
        int qtdVidas = 6;
        String aux;
        char letra;
        
        //***********Inicio do jogo**************
        
        //Seleção de dificuldade do jogo
        escolhaJogador = dificuldade.escolhaDificuldade();
        
        System.out.println("---------------------------------------");
        System.out.println("           INICIANDO O JOGO");
        System.out.println("---------------------------------------");
        
        //Sortea a palavra de acordo com a dificuldade escolhida pelo jogado e transfoma em minúscula
        aux = palavra.sorteador(escolhaJogador).toLowerCase();
        
        //System.out.println(aux);

        //prepara a variável de resposta com "_" do tamanho da palavra escolhida
        palavraEscolhida = caracteres.conversorCaracteres(aux);
        
        boolean palavraDescoberta = false;
        
        //Looping principal enquanto o jogo não acabar       
        while (palavraDescoberta == false){
            //recebe a letra digitada pelo jogador e converte para minúscula
            letra = Character.toLowerCase(letraEscolhida.LetraEscolhida());
            //verifica se a letra digitada já foi digitada antes
            if(letrasDigitadas.contains(letra)){ 
                System.out.println("Letra já digitada");
                continue;
            }
            //adiciona a letra digitada ao histórico de letras
            letrasDigitadas.add(letra); 
            clienteForca.enviarLetra();
            //verifica se a palavra contém a letra digitada
            palavraEscolhida = caracteres.acertoDeCaracteres(aux, palavraEscolhida, letra);
            
            
            
            qtdVidas = vidaJogador.vidasDoJogador(aux, letra, qtdVidas);
            //Se acabar as vidas, o jogo termina
            if (qtdVidas == 0){
                System.out.println("QUE PENA, VOCÊ PERDEU O JOGO");
                //System.out.println("-------------------------------------------------");
                //System.out.println("GOSTARIA DE JOGAR NOVAMENTE?");
                
                break;
            }
            
            //Mostra a situação da respota e as letras digitadas
            System.out.println("-------------------------------------------------");
            System.out.println(palavraEscolhida);
            System.out.println("-------------------------------------------------");
            System.out.println("LETRAS DIGITADAS: " + letrasDigitadas);
          
            //Se a variável de resposta for igual a palavra escolhida, o jogo termina
            if (palavraEscolhida.toString().equals(aux)){
                System.out.println("-------------------------------------------------");
                System.out.println("PARABÉNS VOCÊ VENCEU O JOGO!");
                break;
            }
        }
    }
}
