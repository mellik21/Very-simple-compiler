/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter.expr;

import inter.Node;
import lexer.Token;
import symbol.Type;

public class Expr extends Node{
    
    public Token token;
    public Type type;

    public Expr(Token token, Type type) {
        this.token = token;
        this.type = type;
    }
    
}
