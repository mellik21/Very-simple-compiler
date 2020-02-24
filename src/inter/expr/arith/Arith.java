/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.expr.arith;

import inter.expr.Expr;
import lexer.Token;
import symbol.Type;

public class Arith extends Expr {

    public Expr expr1, expr2;

    public Arith(Token op, Expr x1, Expr x2) {
        super(op, null);
        this.expr1 = x1;
        this.expr2 = x2;
        super.type = Type.maxNumericType(expr1.type, expr2.type);
        if (super.type == null) {
            error("number type expected ,type error ");
        }

    }

}
