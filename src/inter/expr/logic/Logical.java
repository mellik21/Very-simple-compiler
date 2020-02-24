/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.expr.logic;

import inter.expr.Expr;
import lexer.Token;
import symbol.Type;

public class Logical extends Expr {

    public Expr expr1, expr2;

    public Logical(Token token, Expr x1, Expr x2) {
        super(token, null);
        this.expr1 = x1;
        this.expr2 = x2;
        type = check(expr1.type, expr2.type);
        if (type == null) {
            error("type error : boolean expected");
        }
    }

    public Type check(Type p1, Type p2) {
        if (p1 == Type.BOOLEAN && p2 == Type.BOOLEAN) {
            return Type.BOOLEAN;
        } else {
            return null;
        }
    }

}
