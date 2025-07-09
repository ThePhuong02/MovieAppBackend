package movieapp.webmovie.utils;

public class NumberUtil {
    public static boolean isNumber(String value){
        if(value == null) return false;
        try{
            Long number = Long.parseLong(value);
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
