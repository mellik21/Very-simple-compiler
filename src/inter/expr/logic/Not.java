/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter.expr.logic;

import inter.expr.Expr;
import lexer.Token;


public class Not extends Logical
{

    public Not(Token token, Expr x1) {
        super(token, x1, x1);
    }

   
    
}
