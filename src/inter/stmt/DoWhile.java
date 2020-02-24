/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.stmt;

import inter.expr.Expr;
import symbol.Type;


public class DoWhile extends Stmt {

    Expr expr;
    Stmt stmt;

    public DoWhile() {
        expr = null;
        stmt = null;
    }

    public void init(Expr e, Stmt s) {
        this.expr = e;
        this.stmt = s;
        if (expr.type != Type.BOOLEAN) {
            expr.error("boolean required in do");
        }
    }
}
