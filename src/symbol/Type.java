/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package symbol;

import lexer.Keyword;
import lexer.Tag;



public class Type extends Keyword {

    public final int width;

    public Type(String lexeme, int tag, int width) {
        super(lexeme, tag);
        this.width = width;
    }

    public static Type INT = new Type("%", Tag.NUM, 4);
    public static Type FLOAT = new Type("!", Tag.REAL, 8);
    public static Type  BOOLEAN = new Type("$", Tag.BOOLEAN, 4);

    
    public static boolean isNumeric(Type p) {
      return   p == Type.INT || p == Type.FLOAT;
    }

    public static Type maxNumericType(Type t1, Type t2) {
        if (!isNumeric(t1) || !isNumeric(t2)) {
            return null;
        }
        if (t1 == Type.FLOAT || t2 == Type.FLOAT) {
            return Type.FLOAT;
        }

        return Type.INT;

    }

}
