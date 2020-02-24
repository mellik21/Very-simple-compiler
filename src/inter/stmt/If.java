/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.stmt;

import inter.expr.Expr;
import symbol.Type;


public class If extends Stmt {

    public Expr expr;
    public Stmt stmt;

    public If(Expr e, Stmt s) {
        this.expr = e;
        this.stmt = s;
        if (expr.type != Type.BOOLEAN) {
            expr.error("boolean required in if");
        }
    }

}
