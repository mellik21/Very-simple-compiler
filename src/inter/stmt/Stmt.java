/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.stmt;

import inter.Node;


public class Stmt extends Node {

    public Stmt() {
    
    }
    public static Stmt Null = new Stmt();
    public static Stmt Enclosing = Stmt.Null; // used for break stmts
}
