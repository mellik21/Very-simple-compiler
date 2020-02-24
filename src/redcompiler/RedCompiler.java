
package redcompiler;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.util.Scanner;

import com.company.Main;
import lexer.Lexer;
import parser.Parser;

public class RedCompiler {
    public static  String TEST = "{  \n var a  : ! ; \n }";
    public static String TEST1 = "begin { \n  var a : ! ; \n if  1 > 2  \n then \n let a = 3 \n else \n a = 2 \n end_else \n } end";
    public static String TEST2 = "begin { var a : ! ; let a = 1 do while ( a LT 10 ) a = a plus 1 loop } end";
    public static String TEST3 = "begin {\n var a : ! ; \n for ( a = 1 ; a LT 2 ; let a = a plus 1 ) \n a = 2 ; } end";
    public static String TEST4 = "begin { \n var a : 1 ; \n input ( a ) \n output ( a plus 1 ) } end";
    public static String TEST5 = "begin { \n var a,b,c,d : ! ; \n let a = 011B ; b = 011D ; c = 011O ; d = F2C22A3H ; \n } end";

    public static void main(String[] args) throws IOException {
       MainGui lexerAnalyser = new MainGui();
     //  lexerAnalyser.setVisible(true);


        System.setIn(new ByteArrayInputStream(TEST5.getBytes()));

        Parser parser = new Parser(new Lexer());
        parser.start();

    }
}
