/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter;

import lexer.Token;

public class Expression {
    
    public Token token;
    public int type;

    public Expression(Token token, Integer type) {
        this.token = token;
        this.type = type;
    }

    public Expression(Token token) {
        this.token = token;
    }
}
