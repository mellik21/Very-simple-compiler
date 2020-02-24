/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class Lexer {

    private Hashtable<String, Keyword> words = new Hashtable<>();
    private Hashtable<String, Keyword> borders = new Hashtable<>();
    private ArrayList<String> variables = new ArrayList<>();

    private char peek = ' ';

    public static int line = 1;


    private void reserveWord(Keyword kword) {
        words.put(kword.lexeme, kword);
    }

    private void reserveBoarder(Keyword kword) {
        borders.put(kword.lexeme, kword);
    }

    private void reserveKeywords() {
        reserveWord(Keyword.BEGIN);
        reserveWord(Keyword.END);

        reserveWord(Keyword.FALSE);
        reserveWord(Keyword.TRUE);

        reserveWord(Keyword.DO);
        reserveWord(Keyword.WHILE);
        reserveWord(Keyword.IF);
        reserveWord(Keyword.ELSE);
        reserveWord(Keyword.BREAK);

        reserveWord(Keyword.END_ELSE);
        reserveWord(Keyword.INPUT);
        reserveWord(Keyword.OUTPUT);
        reserveWord(Keyword.VAR);
        reserveWord(Keyword.LET);

        reserveWord(Keyword.FOR);
        reserveWord(Keyword.THEN);
        reserveWord(Keyword.IF);
        reserveWord(Keyword.ELSE);

        reserveWord(Keyword.NUM);
        reserveWord(Keyword.REAL);
        reserveWord(Keyword.BOOLEAN);

    }

    private void reserveBoarders() {

        reserveBoarder(Keyword.L_СOMMENT);
        reserveBoarder(Keyword.R_COMMENT);
        reserveBoarder(Keyword.N_EQUAL);
        reserveBoarder(Keyword.EQUAL);
        reserveBoarder(Keyword.L_EQUAL);
        reserveBoarder(Keyword.LT);
        reserveBoarder(Keyword.GT);
        reserveBoarder(Keyword.G_EQUAL);
        reserveBoarder(Keyword.PLUS);
        reserveBoarder(Keyword.MINUS);
        reserveBoarder(Keyword.OR);
        reserveBoarder(Keyword.MULT);
        reserveBoarder(Keyword.DIV);
        reserveBoarder(Keyword.AND);

    }

    public Lexer() {
        reserveKeywords();
        reserveBoarders();
    }


    private void read() throws IOException {
        peek = (char) System.in.read();
    }


    private boolean read(char c) throws IOException {
        read();
        if (peek != c) {
            return false;
        }
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        read();
        for (; ; read()) {
            if (peek == ' ' || peek == '\t') {
                //continue;
            } else if (peek == '\n') {
                line = line + 1;
            } else {
                break;
            }
        }

        switch (peek){
            case '(':
                return new Token('(');
            case ')':
                return new Token(')');
            case '{':
                return new Token('{');
            case '}':
                return new Token('}');
            case ':':
                return new Token(':');
            case ',':
                return new Token(',');
            case '=':
                return new Token('=');
            case ';':
                return new Token(';');
            case '~':
                return new Token('~');
            case '%':
                return Keyword.NUM;
            case '!':
                return Keyword.REAL;
            case '$':
                return Keyword.BOOLEAN;
            default:
                break;
        }

        if (Character.isDigit(peek)) {

            StringBuilder sb = new StringBuilder("");
            while (peek!=' '){
                sb.append(peek);
                read();
            }
            System.out.println(sb.toString());
            if (peek != '.') {
                switch (sb.charAt(sb.length() - 1)) {
                    case 'B':
                    case 'b':
                        sb.deleteCharAt(sb.length() - 1);
                        return checkNumber(sb.toString(), 2);
                    case 'D':
                    case 'd':
                        sb.deleteCharAt(sb.length() - 1);
                        return checkNumber(sb.toString(), 10);
                    case 'O':
                    case 'o':
                        sb.deleteCharAt(sb.length() - 1);
                        return checkNumber(sb.toString(), 8);
                    case 'H':
                    case 'h':
                        try {
                            int convert = Integer.parseInt(sb.toString(),16);
                            System.out.println(convert);
                            return new Num(convert);
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong number format : " + sb.toString());
                            System.exit(0);
                        }
                }
            }
/*
            float x = v, d = 10;
            for (; ; ) {
                read();
                if (!Character.isDigit(peek)) {
                    break;
                }
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(x);
        }
*/
        }

        if (Character.isLetter((int) peek)) {

            StringBuilder b = new StringBuilder();
            do {
                b.append(peek);
                read();
            } while (Character.isLetterOrDigit((int) peek) || peek == '_');
            String s = b.toString();
            System.out.println(s);
            Keyword w = (Keyword) words.get(s);
            if (w != null) {
                return w;
            }

            w = (Keyword)borders.get(s);
            if (w != null) {
                return w;
            }

            w = new Keyword(s, Tag.ID);
            words.put(s, w);

            return new Keyword(s, Tag.ID);
        }



        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    public boolean findVariable(String name){
        return words.containsKey(name);
    }

    public Token checkNumber(String s, int system){
        int result=0;
        try {
            result = Integer.parseInt(s,system);
            System.out.println(" Найдена система счисления: "+system+"  для числа "+s);
        }catch (NumberFormatException e){
            System.out.println("Wrong number format : "+s);
            System.exit(0);
        }
        return new Num(result);
    }
}
