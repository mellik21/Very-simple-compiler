/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

public class Real extends Token {
    
    public final float value;

    public Real(float value) {
        super(Tag.REAL);
        this.value = value;
    }

}
