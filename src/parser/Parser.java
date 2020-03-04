/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import inter.Expression;
import inter.stmt.*;
import lexer.Keyword;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

public class Parser {

    private final Lexer lexer;
    private Token look;
    private String error = null;

    public Parser(Lexer lexer) throws Throwable {
        this.lexer = lexer;
        move();
    }

    private void move() throws Throwable {
        if (lexer.getIndex() != lexer.getProgramLenght()-1) {
            look = lexer.scan();
        } else {
            match(Tag.END);
        }
    }

    private void match(int t) throws Throwable {
        if (look.tag == t) {
           System.out.println("PARSE :" + look.tag + " " + t);
            move();
        } else {
            error("Syntax Error! " + lexer.findWord(new Token(t)).toUpperCase() + " expected");
        }
    }


    private void error(String s) throws Throwable {
        error = s;
        lexer.setError(s);
        lexer.stop();
        this.wait();
    }

    public void start() throws Throwable {
        program();
    }

    private void program() throws Throwable { // PROG → BLOCK
        match(Tag.BEGIN);
        block();
        match(Tag.END);
    }

    private Statement block() throws Throwable { // BLOCK → { DECLS STMTS }
        match('{');
        decls();
        Statement s = stmts();
        match('}');
        return s;
    }

    //==========================================================================
    // what about var a,b,c,d : % ;
    private void decls() throws Throwable {
        int ids = 0;
        int coms = 0;
        String var = "";
        if (look.tag == Tag.VAR) {
            match(Tag.VAR);
            while (look.tag == Tag.ID) {
                var = ((Keyword) look).lexeme ;
                lexer.addVariable(((Keyword) look).lexeme);
                lexer.addToResult("(3," + (lexer.getVariables().lastIndexOf(((Keyword) look).lexeme) + 1) + ")");
                lexer.addVarToVar(((Keyword) look).lexeme);
                match(Tag.ID);
                ids++;
                if (look.tag == ',') {
                    match(',');
                    coms++;
                }
            }
            if (ids == 0 || (ids == coms)) {
                error("Variable expected!");
            } else if (ids - 1 > coms) {
                error("Comma expected!");
            }
            match(':');
            switch (look.tag) {
                case Tag.NUM:
                    lexer.addValueToVar(var,Tag.NUM);
                    match(Tag.NUM);
                    break;
                case Tag.REAL:
                    lexer.addValueToVar(var,Tag.REAL);
                    match(Tag.REAL);
                    break;
                case Tag.BOOLEAN:
                    lexer.addValueToVar(var,Tag.NUM);
                    match(Tag.BOOLEAN);
                    break;
                default:
                    error("Unknown type! ");
            }
            match(';');
        }
    }

    //=============================================================================

    private Statement stmts() throws Throwable { //STMTS →  STMTS STMT | ε
        if (look.tag == '}') {
            return null;
        } else {
            return new StatementSeq(stmt(), stmts());
        }
    }

    private Statement stmt() throws Throwable {
        Expression x;
        Statement s1;
        Statement s2;
        Statement savedstmt; // to save outer loop for break
        switch (look.tag) {
            case ')':
                return null;
            case Tag.VAR:
                decls();
            case ';':
                return Statement.Null;

            case Tag.IF: //STMT → if bool then STMT
                match(Tag.IF);
                x = bool();
                match(Tag.THEN);
                s1 = stmt();
                if (look.tag != Tag.ELSE) {
                    match(Tag.END_ELSE);
                    return new Statement();
                }
                match(Tag.ELSE); //if bool then STMT else
                s2 = stmt();
                match(Tag.END_ELSE);
                return new Statement();

            case Tag.FOR: //STMT → for ( stmt ; bool ; stmt )
                match(Tag.FOR);
                match('(');
                s1 = stmt();
                match(';');
                x = bool();
                match(';');
                s2 = stmt();

                match(')');
                return new Statement();
            case Tag.DO: //STMT → do STMT while ( BOOL ) ;
                match(Tag.DO);
                match(Tag.WHILE);
                x = bool();
                s1 = stmt(); // s1 can be break
                return new Statement();
            case Tag.LOOP: //STMT → break ;
                match(Tag.LOOP);
                return new Statement();
            case Tag.INPUT:
                match(Tag.INPUT);
                match('(');
                while(look.tag == Tag.ID) {
                    match(Tag.ID);
                }
                match(')');
                return new Statement();
            case Tag.OUTPUT:
                match(Tag.OUTPUT);
                match('(');
                while (look.tag != ')') {
                    x = bool();
                }
                match(')');
                return new Statement();
            case '{': //STMT → BLOCK
                return block();
             case '}':
                 match('}');
                 return null;
            case Tag.L_СOMMENT:
                match(Tag.L_СOMMENT);

                while (look.tag != Tag.R_COMMENT) {
                    System.out.println(lexer.getIndex() + " " + (lexer.getProgramLenght() - 1));
                    move();
                    if (lexer.getIndex() >= lexer.getProgramLenght() - 1) {
                        error("Незакрытый комментарий!");
                        return null;
                    }
                }
                match(Tag.R_COMMENT);
                return null;
            case Tag.R_COMMENT:
                error("Незакрытый комментарий!");
                return null;
            default:  //STMT → LOC = BOOL ;
                return assign();
        }
    }

    //what about a = 3 // let a = false
    private Statement assign() throws Throwable { //STMT → LOC = BOOL ; // LOC →  LOC [ BOOL] | id

        Statement statement = null;
        if (look.tag == Tag.LET) {
            match(Tag.LET);
        }
        if(look.tag == Tag.ID) {
            String variable = ((Keyword) look).lexeme;
            if (!lexer.findVariable(variable)) {
                error("variable " + variable + "undeclared");
            }
            match(Tag.ID);

            if (look.tag == '=') { //STMT -> id = expr
                match('=');
                Expression ex = bool();
                statement = new Statement();
            }
            match(';');
        }
        return statement;

    }

    private Expression bool() throws Throwable { //BOOL → BOOL || JOIN | JOIN
        Expression x = join();
        while (look.tag == Tag.OR) {
            Token tok = look;
            match(look.tag);
            x = join();
        }
        return x;
    }

    private Expression join() throws Throwable { // JOIN →  JOIN && EQUALITY | EQUALITY
        Expression x = equality();
        while (look.tag == Tag.AND) {
            Token tok = look;
            match(look.tag);
            x = equality();
        }
        return x;
    }

    private Expression equality() throws Throwable { //EQUALITY  →  EQUALITY == REL | EQUALITY != REL | REL
        Expression x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            match(look.tag);
            x = rel();
        }
        return x;
    }

    private Expression rel() throws Throwable { //REL → EXPR <  EXPR |  EXPR <= EXPR | EXPR >= EXPR | EXPR > EXPR |  EXPR
        Expression x = expr();
        switch (look.tag) {
            case Tag.LT:
            case Tag.LE:
            case Tag.GE:
            case Tag.GT:
                Token tok = look;
                match(look.tag);
                x = expr();
                return x;
            default:
                return x;
        }

    }

    private Expression expr() throws Throwable { //EXPR → EXPR + TERM | EXPR - TERM | TERM
        Expression x = term();
        while (look.tag == Tag.PLUS || look.tag == Tag.MINUS) {
            Token tok = look;
            move();
            x = term();

        }
        return x;
    }

    private Expression term() throws Throwable { //TERM →  TERM * UNARY | TERM / UNARY | UNARY
        Expression x = unary();
        while (look.tag == Tag.MULT || look.tag == Tag.DIV) {
            Token tok = look;
            move();
            x =  unary();
        }
        return x;

    }

    private Expression unary() throws Throwable { // UNARY →  !UNARY | -UNARY | FACTOR
        if (look.tag == '~') {
            match('~');
            return  unary();
        } else {
            return factor();
        }

    }

    private Expression factor() throws Throwable { //factor -> ( BOOL ) | ID[BOOL] |ID | num | real | true | false
        Expression x = null;

        switch (look.tag) {
            case '(':
                match('(');
                x = bool();
                match(')');
                return x;

            case Tag.NUM:
                x = new Expression(look); // factor.n= new Num(value)
                match(Tag.NUM);
                return x;

            case Tag.REAL:
            case Tag.FALSE:
            case Tag.TRUE: //factor.n= true, factor.type = bool
                x = new Expression(look); // factor.n= new Real(value)
                move();
                return x;
            case Tag.ID:
                System.out.println(!lexer.findVariable(((Keyword) look).lexeme) + " " + ((Keyword) look).lexeme);
                if (!lexer.findVariable(((Keyword) look).lexeme)) {

                    error("variable " + ((Keyword) look).lexeme + " undeclared");
                }
                match(Tag.ID);
                return new Expression(look, Tag.NUM); //factor.n = id, factor.type= id.type
        }
        return null;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
