
package utfpr.edu.forcamultiplayer.server.utils;

import java.util.Random;


public class Utils {
    
    public static String gerarCaracteres() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            if (i % 2 == 0) {
                // Gerar um número aleatório entre 0 e 9 e adicioná-lo à string
                int numero = random.nextInt(10);
                sb.append(numero);
            } else {
                // Gerar uma letra maiúscula aleatória (ASCII 65-90) e adicioná-la à string
                char letra = (char) (random.nextInt(26) + 65);
                sb.append(letra);
            }
        }

        return sb.toString();
    }
    
}
