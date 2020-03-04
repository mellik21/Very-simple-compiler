/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;


public class Num extends Token {

    public final String value;

    public Num(String value) {
        super(Tag.NUM);
        this.value = value;
    }

}
