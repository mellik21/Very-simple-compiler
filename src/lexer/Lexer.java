/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Lexer {

    private Hashtable<String, Keyword> words = new Hashtable<>();
    private Hashtable<String, Keyword> borders = new Hashtable<>();
    private ArrayList<String> variables = new ArrayList<>();
    private ArrayList<String> numbers = new ArrayList<>();
    private HashMap<String,Integer> varPlusValues = new HashMap<>();

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


    public Token scan() throws InterruptedException {
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
                result.add("(2," + (Tag.L_P - 300) + ")");
                return new Token('{');
            case '}':
                result.add("(2," + (Tag.R_P - 300) + ")");
                return new Token('}');
            case ':':
                result.add("(2," + (Tag.COLON - 300) + ")");
                return new Token(':');
            case ',':
                result.add("(2," + (Tag.COMMA - 300) + ")");
                return new Token(',');
            case '=':
                result.add("(2," + (Tag.EQSYM - 300) + ")");
                return new Token('=');
            case ';':
                result.add("(2," + (Tag.SEMICOLON - 300) + ")");
                return new Token(';');
            case '~':
                result.add("(2," + (Tag.UNARY - 300) + ")");
                return new Token('~');
            case '%':
                result.add("(1," + (Tag.NUM - 100) + ")");
                return Keyword.NUM;
            case '!':
                result.add("(1," + (Tag.REAL - 100) + ")");
                return Keyword.REAL;
            case '$':
                result.add("(1," + (Tag.BOOLEAN - 100) + ")");
                return Keyword.BOOLEAN;
            default:
                break;
        }

        if (Character.isDigit(peek) || peek=='.') {

            StringBuilder sb = new StringBuilder("");
            while (peek != ' ' && Character.isDigit(peek) && peek !='.') {
                sb.append(peek);
                read();
            }
            if(peek == 'E' || peek == 'e'){
                sb.append(peek);
                if(peek == '+' || peek == '-'){
                    sb.append(peek);
                    while (peek != ' ' && Character.isDigit(peek)) {
                        sb.append(peek);
                        read();
                    }
                    if(peek!=' '){
                        error("Неверный формат числовой строки!");
                    }
                }
            }
           while (peek!=' ' && peek != '.'){
               sb.append(peek);
               read();
           }

            System.out.println("READ :"+sb.toString());

            if (peek != '.') {
                switch (sb.charAt(sb.length() - 1)) {
                    case 'B':
                    case 'b':

                        return checkNumber(sb, 2);
                    case 'D':
                    case 'd':

                        return checkNumber(sb, 10);
                    case 'O':
                    case 'o':

                        return checkNumber(sb, 8);
                    case 'H':
                    case 'h':
                        try {
                            String s = sb.toString();
                            sb.deleteCharAt(sb.length()-1);
                            int convert = Integer.parseInt(sb.toString(), 16);
                            if (!numbers.contains(s)) {
                                numbers.add(s);
                            }
                            result.add("(4," + (numbers.lastIndexOf(s) + 1) + ")");
                            return new Num(s);
                        } catch (NumberFormatException e) {
                            System.out.println("Wrong number format : " + sb.toString());
                            System.exit(0);
                        }
                }


                if (!numbers.contains(sb.toString())) {
                    try{
                        int res = Integer.parseInt(sb.toString());
                        numbers.add(sb.toString());
                        result.add("(4," + (numbers.lastIndexOf(sb.toString()) + 1) + ")");
                    }catch (NumberFormatException e){
                        error("Неверный формат числовой строки!");
                    }

                }

                return new Num(sb.toString());
            }else{
                System.out.println(" it s .");
                sb.append(peek);//Num.
                read();
                if( peek == ' '){
                    error(" Неверный формат числовой строки! ");
                }
                while (Character.isDigit(peek)){
                    sb.append(peek);
                    read();
                }

                if(peek == 'E' || peek == 'e'){
                    sb.append(peek);
                    read();
                    if(peek == '+' || peek == '-'){
                        sb.append(peek);
                        read();
                        while (peek != ' ' && Character.isDigit(peek)) {
                            sb.append(peek);
                            read();
                        }
                        if(peek!=' '){
                            error("Неверный формат числовой строки!");
                        }
                    }
                }
                System.out.println(sb.toString());
                numbers.add(sb.toString());
                result.add("(4," + (numbers.lastIndexOf(sb.toString()) + 1) + ")");
                return new Num(sb.toString());

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

        if (Character.isLetter((int) peek) || peek == '(' || peek == ')' || peek == '*') {

            StringBuilder b = new StringBuilder();
            while (peek != ' ' && peek!=',') {
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

            System.out.println("READ :" +s);

            if (variables.contains(s)) {
                result.add("(3," + (variables.lastIndexOf(s) + 1) + ")");
                return new Keyword(s, Tag.ID);
            }
            Keyword w = words.get(s);
            if (w != null) {
                result.add("(1," + (w.tag - 100) + ")");
                return w;
            }

            w = borders.get(s);
            if (w != null) {
                result.add("(2," + (w.tag - 300) + ")");
                return w;
            }

            //maybe it's 16
            if(s.endsWith("H") || s.endsWith("h")) {
              b.deleteCharAt(b.length()-1);

                try {
                    int convert = Integer.parseInt(b.toString(), 16);
                    if (!numbers.contains(b.toString())) {
                        numbers.add(b.toString());
                    }
                    result.add("(4," + (numbers.lastIndexOf(b.toString()) + 1) + ")");
                    return new Num(b.toString());
                } catch (NumberFormatException ignored) {
                    System.out.println("EX");
                }
            }

            if(s.equals("(")){
                return new Token('(');
            }
            if(s.equals(")")){
                return new Token(')');
            }

            w = new Keyword(s, Tag.ID);
            words.put(s, w);


            return new Keyword(s, Tag.ID);

        }

        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    public void addVariable(String s){
        variables.add(s);
    }

    public boolean findVariable(String name) {
        return variables.contains(name);
    }

    public Token checkNumber(StringBuilder sb, int system) throws InterruptedException {
        String s = sb.toString();
        sb.deleteCharAt(sb.length() - 1);
        int res = 0;
        try {
            res = Integer.parseInt(sb.toString(), system);
            if (!numbers.contains(s)) {
                numbers.add(s);
            }
            result.add("(4," + (numbers.lastIndexOf(s) + 1) + ")");

        } catch (NumberFormatException e) {
          error("Неверный формат числовой строки!");
        }
        return new Num(s);
    }
    public void error(String s) throws InterruptedException {
        setError(s);
        stop();
        this.wait();
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

    public void stop(){
        peek = (char) program[program.length-1];
    }

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String findWord(Token t){
        System.out.println("GO FIND SOME "+t.tag+"\n WORDS");

        for (Keyword keyword : words.values()){
        //    System.out.println(keyword.toString());
            if(keyword.tag == t.tag){

                return keyword.lexeme;
            }
        }
        System.out.println("BOARDS");
        for (Keyword keyword : borders.values()){
           // System.out.println(keyword.toString());
            if((keyword.tag == t.tag)){

                return keyword.lexeme;
            }
        }
        return String.valueOf((char)(t.tag));
    }

    public void addToResult(String s){
        result.add(s);
    }

    public void  addVarToVar(String s){
        varPlusValues.put(s,0);
    }
    public void addValueToVar(String s, int type){
        varPlusValues.put(s,type);
    }
}
