/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.expr;

import inter.expr.logic.Logical;
import lexer.Token;

import symbol.Type;

public class Rel extends Logical {

    public Rel(Token token, Expr x1, Expr x2) {
        super(token, x1, x2);
    }

    @Override
    public Type check(Type p1, Type p2) {
        if (p1 == p2) {
            return Type.BOOLEAN;
        } else {
            return null;
        }
    }

}
