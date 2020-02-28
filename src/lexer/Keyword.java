
package lexer;

public class Keyword extends Token {

    public final String lexeme;

    public Keyword(String lexeme, int tag) {
        super(tag);
        this.lexeme = lexeme;
    }

    public static final Keyword
            AND = new Keyword("and", Tag.AND),
            OR = new Keyword("or", Tag.OR),
            EQUAL = new Keyword("eq", Tag.EQ),
            N_EQUAL = new Keyword("ne", Tag.NE),
            L_EQUAL = new Keyword("le", Tag.LE),
            G_EQUAL = new Keyword("ge", Tag.GE),
            TRUE = new Keyword("true", Tag.TRUE),
            FALSE = new Keyword("false", Tag.FALSE),
            IF = new Keyword("if", Tag.IF),
            ELSE = new Keyword("else", Tag.ELSE),
            BREAK = new Keyword("loop", Tag.LOOP),
            DO = new Keyword("do", Tag.DO),
            WHILE = new Keyword("while", Tag.WHILE),
            MINUS = new Keyword("min", Tag.MINUS),

    BEGIN = new Keyword("begin", Tag.BEGIN),
            END = new Keyword("end", Tag.END),
            VAR = new Keyword("var", Tag.VAR),
            END_ELSE = new Keyword("end_else", Tag.END_ELSE),
            PLUS = new Keyword("plus", Tag.PLUS),
            MULT = new Keyword("mult", Tag.MULT),
            DIV = new Keyword("div", Tag.DIV),
            INPUT = new Keyword("input", Tag.INPUT),
            OUTPUT = new Keyword("output", Tag.OUTPUT),
            LET = new Keyword("let", Tag.LET),
            FOR = new Keyword("for", Tag.FOR),


            L_СOMMENT = new Keyword("(*", Tag.L_СOMMENT),
            R_COMMENT = new Keyword("*)", Tag.R_COMMENT),
            LT = new Keyword("LT", Tag.LT),
            GT = new Keyword("GT", Tag.GT),

            THEN = new Keyword("then", Tag.THEN),

    NUM = new Keyword("!", Tag.NUM),
            REAL = new Keyword("%", Tag.REAL),
            BOOLEAN = new Keyword("$", Tag.BOOLEAN),



            //onesym


    L_P = new Keyword("{",Tag.L_P),
        R_P = new Keyword("{",Tag.R_P),
    COLON = new Keyword(":",Tag.COLON),
        COMMA = new Keyword(",",Tag.COMMA),
        EQSYM = new Keyword("=",Tag.EQSYM),
        SEMICOLON = new Keyword(";",Tag.SEMICOLON),
        UNARY = new Keyword("~",Tag.UNARY);

    @Override
    public int hashCode()
    {
        return (int)this.lexeme.charAt(0);
    }
}
