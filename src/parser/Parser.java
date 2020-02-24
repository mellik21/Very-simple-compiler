/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import com.sun.deploy.security.ValidationState;
import inter.expr.Constant;
import inter.expr.Expr;
import inter.expr.Id;
import inter.expr.Rel;
import inter.expr.arith.Arith;
import inter.expr.arith.Unary;
import inter.expr.logic.Not;
import inter.stmt.*;

import java.io.IOException;
import java.util.ArrayList;

import lexer.Keyword;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import symbol.Env;
import symbol.Type;

public class Parser {
    ArrayList<String>variables = new ArrayList<>();

    private final Lexer lexer;
    private Token look;

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        move();
    }

    private void move() throws IOException {
        look = lexer.scan();
        System.out.println("# "+look.tag);

        if(look.tag == 65535){
            System.out.println("Анализ проведен успешно!");
        }
    }

    private void match(int t) throws IOException {
        if (look.tag == t) {
       //     System.out.println("$ "+look.tag+" "+t);
            move();
        } else {
            error("Syntax Error! Code "+t+" expected");
        }
    }

    private void error(String s) {
       throw new Error("near line " + lexer.line + " : " + s);

    }

    public void start() throws IOException {
        program();
    }

    private void program() throws IOException { // PROG → BLOCK
        if(look.tag == Tag.BEGIN) {
            match(Tag.BEGIN);
        }else{
            error("begin expected");
        }
        block();
        if(look.tag == Tag.END) {
            match(Tag.END);
        }else{
            error("end expected");
        }
    }

    private Env top = null; // top symbol table

    private Stmt block() throws IOException { // BLOCK → { DECLS STMTS }
        match('{');
        decls();
        Stmt s = stmts();
        match('}');
        return s;
    }

    //==========================================================================
    // what about var a,b,c,d : % ;
    private void decls() throws IOException {
        if(look.tag == Tag.VAR){
        match(Tag.VAR);
            while (look.tag == Tag.ID) {
                match(Tag.ID);
                if (look.tag == ',') {
                    match(',');
                }
            }
            match(':');
            switch (look.tag) {
                case Tag.NUM:
                    match(Tag.NUM);
                    break;
                case Tag.REAL:
                    match(Tag.REAL);
                    break;
                case Tag.BOOLEAN:
                    match(Tag.BOOLEAN);
                    break;
                default:
                    break;
            }

            match(';');

        }
    }

    //=============================================================================

    private Stmt stmts() throws IOException { //STMTS →  STMTS STMT | ε
        if (look.tag == '}') {
            return Stmt.Null;
        } else {
            return new StmtSeq(stmt(), stmts());
        }
    }

    private Stmt stmt() throws IOException {
        Expr x;
        Stmt s1;
        Stmt s2;
        Stmt savedstmt; // to save outer loop for break
        switch (look.tag) {
            case ';':
                move();
                return Stmt.Null;

            case Tag.IF: //STMT → if bool then STMT
                match(Tag.IF);

                x = bool();
                match(Tag.THEN);
                s1 = stmt();

                if (look.tag != Tag.ELSE) {
                    return new If(x, s1);
                }

                match(Tag.ELSE); //if bool then STMT else

                s2 = stmt();

                match(Tag.END_ELSE);
                return new IfElse(x, s1, s2);

            case Tag.FOR: //STMT → for ( stmt ; bool ; stmt )
                match(Tag.FOR);
                match('(');
                s1 = stmt();
                match(';');
                x = bool();
                match(';');
                s2 = stmt();
                match(')');
                return new For(x,s1,s2);
            case Tag.DO: //STMT → do STMT while ( BOOL ) ;
                match(Tag.DO);
                match(Tag.WHILE);
                While whilenode = new While();
                savedstmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode; // now there outer/Stmt.Enclosing is While

                x = bool();

                s1 = stmt(); // s1 can be break
                whilenode.init(x, s1);
                Stmt.Enclosing = savedstmt; // reset Stmt.Enclosing
                return whilenode;
            case Tag.LOOP: //STMT → break ;
                match(Tag.LOOP);

                return new Break();
            case Tag.INPUT:
                match(Tag.INPUT);
                match('(');
                match(Tag.ID);
                match(')');
                return new Input();
            case Tag.OUTPUT:
                match(Tag.OUTPUT);
                match('(');
                while (look.tag != ')') {
                    x = bool();
                }
                match(')');
                return new Input();
            case '{': //STMT → BLOCK
                return block();
            case '}':
                return null;
            default:  //STMT → LOC = BOOL ;
                return assign();

        }

    }

    //what about a = 3 // let a = false
    private Stmt assign() throws IOException { //STMT → LOC = BOOL ; // LOC →  LOC [ BOOL] | id

        Stmt stmt = null;
        if(look.tag == Tag.LET){
            match(Tag.LET);
        }
        Token t = look;
        match(Tag.ID);
        String variable  = ((Keyword)t).lexeme;
        if(!lexer.findVariable(variable)){
            error("variable " +variable +"undeclared");
        }


        if (look.tag == '=') { //STMT -> id = expr
            match('=');
            System.out.println(variable+" =");
            stmt = new Set(variable, bool());
        }
        return stmt;
    }

    private Expr bool() throws IOException { //BOOL → BOOL || JOIN | JOIN
        Expr x = join();
        while (look.tag == Tag.OR) {
            Token tok = look;
            move();
            x = new Rel(tok, x, join());
        }
        return x;
    }

    private Expr join() throws IOException { // JOIN →  JOIN && EQUALITY | EQUALITY
        Expr x = equality();
        while (look.tag == Tag.AND) {
            Token tok = look;
            move();
            x = new Rel(tok, x, equality());
        }
        return x;
    }

    private Expr equality() throws IOException { //EQUALITY  →  EQUALITY == REL | EQUALITY != REL | REL
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            move();
            x = new Rel(look, x, rel());
        }
        return x;
    }

    private Expr rel() throws IOException { //REL → EXPR <  EXPR |  EXPR <= EXPR | EXPR >= EXPR | EXPR > EXPR |  EXPR
        Expr x = expr();
        switch (look.tag) {
            case Tag.LT:
            case Tag.LE:
            case Tag.GE:
            case Tag.GT:
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }

    }

    private Expr expr() throws IOException { //EXPR → EXPR + TERM | EXPR - TERM | TERM
        Expr x = term();
        while (look.tag == Tag.PLUS || look.tag == Tag.MINUS) {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    private Expr term() throws IOException { //TERM →  TERM * UNARY | TERM / UNARY | UNARY
        Expr x = unary();
        while (look.tag == Tag.MULT || look.tag == Tag.DIV) {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());

        }
        return x;

    }

    private Expr unary() throws IOException { // UNARY →  !UNARY | -UNARY | FACTOR
        if (look.tag == '~') {
            move();
            return new Unary(Keyword.MINUS, unary());
        } else if (look.tag == '!') {
            Token tok = look;
            move();
            return new Not(tok, unary());

        } else {
            return factor();
        }

    }

    private Expr factor() throws IOException { //factor -> ( BOOL ) | ID[BOOL] |ID | num | real | true | false
        Expr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.INT); // factor.n= new Num(value)
                match(Tag.NUM);
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.FLOAT); // factor.n= new Real(value)
                move();
                return x;
            case Tag.TRUE: //factor.n= true, factor.type = bool
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            case Tag.ID:
                if (!lexer.findVariable(((Keyword)look).lexeme)) {
                    error("variable " + ((Keyword) look).lexeme + " undeclared");
                }
                move();
                return new Expr(look,Type.INT); //factor.n = id, factor.type= id.type
            default:
                error("Syntax error");

        }
        return null;
    }


}
