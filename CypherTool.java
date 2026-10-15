import java.util.Scanner;

public class CypherTool {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {


        System.out.println("Welcome to the Cypher Tool!");
        System.out.println("Please enter your input:\n" +
                "Select cypher:\n" +
                "1. ROT13\n" +
                "2. Atbash\n" +
                "3. Vigenere");

        String cipher = getInput();

    while(!cipher.equals("1") && !cipher.equals("2") && !cipher.equals("3")){
        System.out.println("Invalid input");
        System.out.println("Please enter your input:\n" +
                "Select cypher:\n" +
                "1. ROT13\n" +
                "2. Atbash\n" +
                "3. Vigenere");
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
        switch(cipher){


            case "1":
                if(oper.equals("1")) {
                    System.out.println("Your encrypted input is: " + encryptRot13(input));
                    break;
                }
                else {
                    System.out.println("Your decrypted input is: " + decryptRot13(input));
                    break;
                }
            case "2":
                if(oper.equals("1")) {
                    System.out.println("Your encrypted input is: " + encryptAtbash(input));
                    break;
                }
                else {
                    System.out.println("Your decrypted input is: " + decryptAtbash(input));
                    break;
                }
            case "3":
                System.out.println("Enter your phrase:");
                String phrase = getInput();
                if(oper.equals("1")) {
                    System.out.println("Your encrypted input is: " + encryptVigenere(input, phrase));
                    break;
                }
                else {
                    System.out.println("Your decrypted input is: " + decryptVigenere(input, phrase));
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

    public static String encryptRot13(String s) {
        String res = "";
        for(char c:s.toCharArray()) {
            c = Character.toLowerCase(c);
            if(c>='a' && c<='m') res+= (c+=13);
            else if(c>'m'&&c<='z') res+=(c-=13);
            else res+=c;
        }
        return res;
    }
    public static String decryptRot13(String s) {
        String res = "";
        for(char c:s.toCharArray()) {
            c = Character.toLowerCase(c);
            if(c>='a' && c<='m') res+= (c-=13);
            else if(c>'m'&&c<='z') res+=(c+=13);
            else res+=c;
        }
        return res;
    }

    public static String encryptAtbash(String s) {
        String res = "";
        for(char c:s.toCharArray()) {
            c = Character.toLowerCase(c);
            if(c>='a' && c<='z'){
            res+= (char) ('a' + 'z' - c);
            }
            else res+=c;
        }
        return res;
    }


    public static String decryptAtbash(String s) {

        return encryptAtbash(s);
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
