import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
public class Tokenizer{
    static String key_word[] = {"class", "constructor", "function","method", "field", "static", "var", "int", "char",
            "boolean", "void", "true","false", "null", "this", "let", "do", "if", "else", "while", "return"};
    private final static String sybs = "{}()[].,;+-*/&|<>=~";
    static char[] syblist = {'{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'};
    static String token_words = "";
    static Scanner in;
    static ArrayList<String> tokens = new ArrayList<String>();
    static Iterator iterate_lines = tokens.iterator();
    Tokenizer(String path) throws FileNotFoundException {
        boolean multi_comments = false;
        boolean comments = false;
        in = new Scanner(new File(path));
        ArrayList<String> list = new ArrayList<String>();

        while (in.hasNext()) {

            // Strips comments
            do {
                comments = false;
                token_words = in.next();

                if (token_words.startsWith("//")) {
                    comments = true;
                    token_words = in.nextLine();
                }

                if (token_words.startsWith("/*") && multi_comments == false) {
                    multi_comments = true;

                } else if (token_words.endsWith("*/") && multi_comments == true) {
                    multi_comments = false;
                    token_words = in.next();
                }

            } while (multi_comments || comments); // End comment stripping

            boolean containsSymbol = false;
            for (int i = 0; i < syblist.length; i++) {
                // if token is alone break
                if (token_words.equals(syblist[i] + "")) {
                    break;

                } else if (token_words.contains(syblist[i] + "")) {
                    containsSymbol = true;
                    int prev = 0;
                    for (int j = 0; j < token_words.length(); j++) {
                        for (int k = 0; k < syblist.length; k++) {
                            if (token_words.charAt(j) == syblist[k]) {
                                if (prev != j) {
                                    list.add(token_words.substring(prev, j));
                                }
                                list.add(syblist[k] + "");
                                prev = j + 1;
                            }
                        }

                        if (prev < token_words.length() && j == token_words.length() - 1) {
                            list.add(token_words.substring(prev, token_words.length()));
                        }
                    }
                    break;
                }
            }

            if (!containsSymbol) {
                list.add(token_words);
            }

        }  // All tokens are added.
        in.close();
        tokens = list;
        iterate_lines = tokens.iterator();

    }

    boolean hasMoreTokens() {
        return iterate_lines.hasNext();
    }

    void advance() {
        if (iterate_lines.hasNext()) {
            token_words = (String) iterate_lines.next();

        }
    }

    String tokenType() {

        for (String keyword : key_word) {
            if (token_words.equals(keyword)) {
                return "KEYWORD";
            }
        }

        if (sybs.contains(token_words) && token_words != "") {
            return "SYMBOL";
        }

        // "regex. \\d+" is any digit from 0-9, + adds support for > 1 digit"
        if (token_words.matches("\\d+")) {
            return "INT_CONST";
        }

        // double quotes, \ is escape char
        if (token_words.startsWith("\"")) {
            return "STRING_CONST";
        } else {

            return "IDENTIFIER";
        }
    }

    String keyWord() {
        for (String keyword : key_word) {
            if (token_words.equals(keyword)) {
                return keyword;
            }
        }

        return "";
    }
    char symbol() {
        return token_words.charAt(0);
    }
    String identifier() {
        return token_words;
    }
    int intVal() {
        return Integer.parseInt(token_words);
    }
    String stringVal() {
        token_words = token_words.replaceAll("\"", "");
        return token_words;
    }
}
