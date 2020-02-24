package inter.stmt;

import inter.expr.Expr;
import symbol.Type;

public class For extends Stmt {
    Expr expr;
    Stmt stmt;
    Stmt stmt1;

    public For(Expr expr, Stmt stmt, Stmt stmt1){
        this.expr = expr;
        this.stmt = stmt;
        this.stmt1 = stmt1;
    }

}
