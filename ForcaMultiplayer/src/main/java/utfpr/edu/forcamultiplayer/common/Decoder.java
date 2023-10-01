
package utfpr.edu.forcamultiplayer.common;

import com.google.gson.Gson;


public class Decoder {
    
    public static InputData decoder(String input){
        return new Gson().fromJson(input, InputData.class);
    }
}
