/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import inter.expr.Constant;
import inter.expr.Expr;
import inter.expr.Rel;
import inter.expr.arith.Arith;
import inter.expr.arith.Unary;
import inter.expr.logic.Not;
import inter.stmt.*;
import lexer.Keyword;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import symbol.Env;
import symbol.Type;

public class Parser {

    private final Lexer lexer;
    private Token look;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        move();
    }

    private void move() {
        if(lexer.getIndex()!=lexer.getProgramLenght()) {
            look = lexer.scan();
        //    System.out.println("# " + look.tag);
        }else {
            System.out.println("Анализ проведен успешно!");
        }
    }

    private void match(int t) {
        if (look.tag == t) {
        //  System.out.println("$ "+look.tag+" "+t);
            move();
        } else {
            error("Syntax Error! Code "+t+" expected");
        }
    }

    private void error(String s) {
        System.out.println(("near line " + lexer.line + " : " + s));
        System.exit(0);
    }

    public void start()  {
        program();
    }

    private void program() { // PROG → BLOCK
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

    private Stmt block()  { // BLOCK → { DECLS STMTS }
        match('{');
        decls();
        Stmt s = stmts();
        match('}');
        return s;
    }

    //==========================================================================
    // what about var a,b,c,d : % ;
    private void decls()  {
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

    private Stmt stmts()  { //STMTS →  STMTS STMT | ε
        if (look.tag == '}') {
            return Stmt.Null;
        } else {
            return new StmtSeq(stmt(), stmts());
        }
    }

    private Stmt stmt()  {
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
            case Tag.L_СOMMENT:
                match(Tag.L_СOMMENT);
                while(look.tag!=Tag.R_COMMENT){
                    lexer.scan();
                    if(lexer.getIndex()>=lexer.getProgramLenght()-1){
                        error("Незакрытый комментарий!");
                        break;
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
    private Stmt assign()  { //STMT → LOC = BOOL ; // LOC →  LOC [ BOOL] | id

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
         //   System.out.println(variable+" =");
            stmt = new Set(variable, bool());
        }
        return stmt;
    }

    private Expr bool()  { //BOOL → BOOL || JOIN | JOIN
        Expr x = join();
        while (look.tag == Tag.OR) {
            Token tok = look;
            match(look.tag);
            x = new Rel(tok, x, join());
        }
        return x;
    }

    private Expr join() { // JOIN →  JOIN && EQUALITY | EQUALITY
        Expr x = equality();
        while (look.tag == Tag.AND) {
            Token tok = look;
            match(look.tag);
            x = new Rel(tok, x, equality());
        }
        return x;
    }

    private Expr equality()  { //EQUALITY  →  EQUALITY == REL | EQUALITY != REL | REL
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {
            match(look.tag);
            x = new Rel(look, x, rel());
        }
        return x;
    }

    private Expr rel()  { //REL → EXPR <  EXPR |  EXPR <= EXPR | EXPR >= EXPR | EXPR > EXPR |  EXPR
        Expr x = expr();
        switch (look.tag) {
            case Tag.LT:
            case Tag.LE:
            case Tag.GE:
            case Tag.GT:
                Token tok = look;
                match(look.tag);
                return new Rel(tok, x, expr());
            default:
                return x;
        }

    }

    private Expr expr() { //EXPR → EXPR + TERM | EXPR - TERM | TERM
        Expr x = term();
        while (look.tag == Tag.PLUS || look.tag == Tag.MINUS) {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }

    private Expr term()  { //TERM →  TERM * UNARY | TERM / UNARY | UNARY
        Expr x = unary();
        while (look.tag == Tag.MULT || look.tag == Tag.DIV) {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());

        }
        return x;

    }

    private Expr unary()  { // UNARY →  !UNARY | -UNARY | FACTOR
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

    private Expr factor()  { //factor -> ( BOOL ) | ID[BOOL] |ID | num | real | true | false
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
