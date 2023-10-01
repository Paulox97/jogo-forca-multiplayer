
package utfpr.edu.forcamultiplayer.common;

import com.google.gson.Gson;

public class Enconder {
    
    public static String enconder(InputData input){
        return new Gson().toJson(input);
    }
}
