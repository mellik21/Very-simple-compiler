/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.stmt;

public class Break extends Stmt {

    Stmt stmt;

    public Break() {
        if (!(Stmt.Enclosing instanceof While) || !(Stmt.Enclosing instanceof DoWhile)) {
            error("unenclosed break  ");
        }
        stmt = Stmt.Enclosing;
    }

}
