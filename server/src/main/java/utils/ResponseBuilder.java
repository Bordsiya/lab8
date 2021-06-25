package utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for creating field {@link answers.Response} responseBody
 * @author NastyaBordun
 * @version 1.1
 */
public class ResponseBuilder {
    private static String stringBuilder = "";
    private static ArrayList<String> argsList = new ArrayList<>();

    public static void append(String ans){
        stringBuilder += ans;
    }

    public static void appendArgs(String... args){
        argsList.addAll(Arrays.asList(args));
    }

    public static void appendln(String ans){
        stringBuilder += ans;
        stringBuilder += '\n';
    }

    public static void appendError(String ans){
        stringBuilder += "Error: " + ans + '\n';
    }

    public static String getStringBuilder(){
        return stringBuilder;
    }

    public static String[] getArgsList(){
        String[] args = new String[argsList.size()];
        args = argsList.toArray(args);
        return args;
    }

    public static void clear(){
        stringBuilder = "";
        argsList.clear();
    }
}
