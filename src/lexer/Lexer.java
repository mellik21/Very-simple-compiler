/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

public class Lexer {

    private Hashtable<String, Keyword> words = new Hashtable<>();
    private Hashtable<String, Keyword> borders = new Hashtable<>();
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();

    private ArrayList<String> result = new ArrayList<>();

    private byte[] program;

    private char peek = ' ';

    public static int line = 1;

    public Lexer(byte[] program) {
        this.program = new byte[program.length];
        System.arraycopy(program, 0, this.program, 0, program.length);
    }

    private int index = 0;

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

        reserveBoarder(Keyword.L_P);
        reserveBoarder(Keyword.R_P);
        reserveBoarder(Keyword.COLON);
        reserveBoarder(Keyword.COMMA);
        reserveBoarder(Keyword.EQSYM);
        reserveBoarder(Keyword.SEMICOLON);
        reserveBoarder(Keyword.UNARY);
    }

    public Lexer() {
        reserveKeywords();
        reserveBoarders();
    }


    private void read() {
        if (index == program.length) {

        } else {
            peek = (char) program[index];
            //  System.out.println(peek + "  " + index);
            index++;
        }
    }


    public Token scan() {
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

        switch (peek) {
            case '{':
                result.add("(2,"+(Tag.L_P-300)+")");
                return new Token('{');
            case '}':
                result.add("(2,"+(Tag.R_P-300)+")");
                return new Token('}');
            case ':':
                result.add("(2,"+(Tag.COLON-300)+")");
                return new Token(':');
            case ',':
                result.add("(2,"+(Tag.COMMA-300)+")");
                return new Token(',');
            case '=':
                result.add("(2,"+(Tag.EQSYM-300)+")");
                return new Token('=');
            case ';':
                result.add("(2,"+(Tag.SEMICOLON-300)+")");
                return new Token(';');
            case '~':
                result.add("(2,"+(Tag.UNARY-300)+")");
                return new Token('~');
            case '%':
                result.add("(1,"+(Tag.NUM-100)+")");
                return Keyword.NUM;
            case '!':
                result.add("(1,"+(Tag.REAL-100)+")");
                return Keyword.REAL;
            case '$':
                result.add("(1,"+(Tag.BOOLEAN-100)+")");
                return Keyword.BOOLEAN;
            default:
                break;
        }

        if (Character.isDigit(peek)) {

            StringBuilder sb = new StringBuilder("");
            while (peek != ' ') {
                sb.append(peek);
                read();
            }
            System.out.println(sb.toString());

            numbers.add(sb.toString());
            result.add("(4,"+ (numbers.lastIndexOf(sb.toString())+1) +")");
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
                            int convert = Integer.parseInt(sb.toString(), 16);
                            return new Num(convert);
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong number format : " + sb.toString());
                            System.exit(0);
                        }
                }

                return new Num(Integer.parseInt(sb.toString()));
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

        if (Character.isLetter((int) peek) || peek == '(' || peek == ')' || peek == '*') {

            StringBuilder b = new StringBuilder();
            while (peek != ' ') {
                b.append(peek);
                read();
                if (index == program.length - 1) {
                    b.append(peek);
                    read();
                    b.append(peek);
                    break;
                }
            }
            String s = b.toString();
            System.out.println(s);

            if(variables.contains(s)){
                result.add("(3,"+(variables.lastIndexOf(s)+1)+")");
                return new Keyword(s, Tag.ID);
            }
            Keyword w = words.get(s);
            if (w != null) {
                result.add("(1,"+(w.tag - 100)+")");
                return w;
            }

            w = borders.get(s);
            if (w != null) {
                result.add("(2,"+(w.tag - 300)+")");
                return w;
            }

            w = new Keyword(s, Tag.ID);
            words.put(s, w);
            variables.add(s);
            result.add("(3,"+(variables.lastIndexOf(s)+1)+")");
            return new Keyword(s, Tag.ID);
        }


        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    public boolean findVariable(String name) {
        return words.containsKey(name);
    }

    public Token checkNumber(String s, int system) {
        int result = 0;
        try {
            result = Integer.parseInt(s, system);

        } catch (NumberFormatException e) {
            System.out.println("Wrong number format : " + s);
            System.exit(0);
        }
        return new Num(result);
    }

    public Hashtable<String, Keyword> getWords() {
        return words;
    }

    public Hashtable<String, Keyword> getBorders() {
        return borders;
    }

    public ArrayList<String> getVariables() {
        return variables;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public byte[] getProgram() {
        return program;
    }

    public void setProgram(byte[] program) {
        this.program = program;
    }

    public int getIndex() {
        return index;
    }

    public int getProgramLenght() {
        return program.length;
    }

    public ArrayList<String> getResult() {
        return result;
    }
}
