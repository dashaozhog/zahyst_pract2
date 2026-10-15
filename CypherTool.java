import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class CypherTool {
    private static final Scanner scanner = new Scanner(System.in);
    private static  char[] alphabet = {};
    public static void main(String[] args) {


        System.out.println("Welcome to the Cypher Tool!");
        System.out.println("Please enter your input:\n" +
                "Select cypher:\n" +
                "1. Caesar\n" +
                "2. Vigenere");

        String cipher = getInput();

    while(!cipher.equals("1") && !cipher.equals("2") && !cipher.equals("3")){
        System.out.println("Invalid input");
        System.out.println("Please enter your input:\n" +
                "Select cypher:\n" +
                "1. ROT13\n" +
                "2. Vigenere");
        cipher = getInput();
    }

        System.out.println("Select operation:\n" +
                "1. Encrypt\n" +
                "2. Decrypt");

        String oper = getInput();

        while(!oper.equals("1") && !oper.equals("2")){
            System.out.println("Invalid input");
            System.out.println("Select operation:\n" +
                    "1. Encrypt\n" +
                    "2. Decrypt");
            oper = getInput();
        }
        String input = "";
        while(true){

            if(input.equals("EXIT")) {
                System.out.println("Exiting...");
                break;}
        System.out.println("Enter your input(EXIT to exit):");
            input = getInput();
         System.out.println("Enter your key:");
         String key = getInput();
         int keyInt = 0;
        switch(cipher){

            case "1":
                try{
                    keyInt = parseInt(key);
                }
                catch(Exception ex){
                    System.out.println("Для шифру Цезаря введіть число");
                    key = getInput();
                    keyInt = parseInt(key);
                }
                if(oper.equals("1")) {
                    System.out.println("Зашифроване повідомлення: " + encryptCaesar(input, keyInt));
                    break;
                }
                else {
                    System.out.println("Дешифроване повідомлення: " + decryptCaesar(input, keyInt));
                    break;
                }

            case "2":
                if(oper.equals("1")) {
                    System.out.println("Your encrypted input is: " + encryptVigenere(input, key));
                    break;
                }
                else {
                    System.out.println("Your decrypted input is: " + decryptVigenere(input, key));
                    break;
                }
                default:
                System.out.println("Invalid input");
                    System.out.println("Enter your input:");
                    input = getInput();

        }
        }
    }

    public static String getInput() {

        return scanner.nextLine();
    }

    public static String encryptCaesar(String s, int key) {
        String res = "";
        for(char c:s.toCharArray()) {
            c = Character.toLowerCase(c);
            if( (c+key)>'я' ) {res+=(('я'-c)+'а'+key);}
            else res+=(char)(c+key);
        }
        return res;
    }
    public static String decryptCaesar(String s, int key) {
        return encryptCaesar(s, key*-1);
    }


    public static String encryptVigenere(String s, String phrase){
        if (phrase == null || phrase.isEmpty()) return s;
        StringBuilder res = new StringBuilder();
        String key = phrase.toLowerCase();
        int keyIndex = 0;

        for(char c : s.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                int shift = key.charAt(keyIndex % key.length()) - 'a';
                res.append((char) ('a' + (c - 'a' + shift) % 26));
                keyIndex++;
            } else if (c >= 'A' && c <= 'Z') {
                int shift = key.charAt(keyIndex % key.length()) - 'a';
                res.append((char) ('A' + (c - 'A' + shift) % 26));
                keyIndex++;
            } else {
                res.append(c);
            }
        }
        return res.toString();
    }

    public static String decryptVigenere(String s, String phrase) {
        if (phrase == null || phrase.isEmpty()) return s;
        StringBuilder res = new StringBuilder();
        String key = phrase.toLowerCase();
        int keyIndex = 0;

        for(char c : s.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                int shift = key.charAt(keyIndex % key.length()) - 'a';

                res.append((char) ('a' + (c - 'a' - shift + 26) % 26));
                keyIndex++;
            } else if (c >= 'A' && c <= 'Z') {
                int shift = key.charAt(keyIndex % key.length()) - 'a';
                res.append((char) ('A' + (c - 'A' - shift + 26) % 26));
                keyIndex++;
            } else {
                res.append(c);
            }
        }
        return res.toString();
    }

}
