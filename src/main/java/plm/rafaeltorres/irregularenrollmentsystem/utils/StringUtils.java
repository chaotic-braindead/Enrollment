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
    public static String integerToPlace(int n){
        int lastDigit = n % 10;
        StringBuilder sb = new StringBuilder(n+"");
        switch(lastDigit){
            case 1:
                sb.append("st");
                break;
            case 2:
                sb.append("nd");
                break;
            case 3:
                sb.append("rd");
                break;
            default:
                sb.append("th");
        }
        return sb.toString();
    }
}
