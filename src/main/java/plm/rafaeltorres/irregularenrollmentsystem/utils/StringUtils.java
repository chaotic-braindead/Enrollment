package plm.rafaeltorres.irregularenrollmentsystem.utils;

public class StringUtils {
    public static String addSpacesOnPascalCase(String s){
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray()){
            if(Character.isUpperCase(c))
                sb.append(" ");
            sb.append(c);
        }
        return sb.toString();
    }
    public static String snakeCaseToCamelCase(String s){
        StringBuilder sb = new StringBuilder();
        boolean upper = true;
        for(char c : s.toCharArray()){
            if(c == '_'){
                upper = true;
                continue;
            }
            if(upper){
                sb.append(Character.toUpperCase(c));
                upper = false;
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
