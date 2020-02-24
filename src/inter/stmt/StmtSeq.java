/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter.stmt;


public class StmtSeq extends Stmt{
    Stmt stmtl; Stmt stmt2;

    public StmtSeq(Stmt stmtl, Stmt stmt2) {
        this.stmtl = stmtl;
        this.stmt2 = stmt2;
    }
    
}
