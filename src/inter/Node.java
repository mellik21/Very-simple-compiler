/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter;

import lexer.Lexer;

public class Node {
    
    private int lexline;

    public Node() {
        lexline = Lexer.line;
    }

    public void error(String s) {

       new Error("near line " + lexline + ": " + s);
       // System.exit(0);
    }
}
