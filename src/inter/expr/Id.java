/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inter.expr;

import lexer.Keyword;
import symbol.Type;

public class Id extends Expr{

    public Id(Keyword token, Type type) {
        super(token, type);
    }
    
}
