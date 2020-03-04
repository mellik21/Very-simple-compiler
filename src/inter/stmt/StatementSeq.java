/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter.stmt;


public class StatementSeq extends Statement {
    Statement stmtl; Statement statement2;

    public StatementSeq(Statement stmtl, Statement statement2) {
        this.stmtl = stmtl;
        this.statement2 = statement2;
    }
    
}
