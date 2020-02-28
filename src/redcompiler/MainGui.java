package redcompiler;

import lexer.Keyword;
import lexer.Lexer;
import parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Hashtable;

public class MainGui extends JFrame {
    private JLabel label1;
    private JPanel panel1;
    private JTextArea textAreaProgram;
    private JRadioButton RadioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton RadioButton4;
    private JTextArea textAreaAn1;
    private JTable tableWords;
    private JTable tableBoarders;
    private JTextArea textAreaErrors;
    private JTable tableNums;
    private JTable tableVariables;
    private JButton button1;

    private static String[][]keywords;
    private static String[][]boarders;
    private static String[][]numbers;
    private static String[][]variables;
    private static String[]keywordsColumns = new String[]{"№", "Word"};
    private static  Lexer lexer = new Lexer();

    public MainGui(){
        setSize(new Dimension(800,600));
       setContentPane(panel1);
       panel1.setBackground(new Color(200,200,200));
       setVisible(true);


        RadioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n  var a : ! ; \n if 1 LT 2  \n then \n let a = 3 \n else \n a = 2 \n end_else \n } end");
            }
        });
        radioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n var a,b,c,d : ! ; \n let a = 011B ; \n b = 011D ; \n c = 011O ; \n d = F2C22A3H ; \n } end");
            }
        });
        radioButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n var a : ! ; \n let a = 1 \n do while ( a LT 10 ) \n a = a plus 1 loop \n } end\n");
            }
        });
        RadioButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n var a : 1 ; \n input ( a ) \n output ( a plus 1 ) } end");
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(textAreaProgram.getText());
                try {
                    Parser parser = new Parser(lexer);


                    System.setIn(new ByteArrayInputStream(textAreaProgram.getText().getBytes()));

                    parser.start();

                } catch (IOException ex) {
                    System.out.println("EXCEPTION");
                    textAreaErrors.setText("");
                    textAreaErrors.append("Ошибка загрузки вводного текста!");
                }
            }
        });
    }

   private static void fillKeywords(){
       keywords = new String[lexer.getWords().size()][2];
       int j=0; int i = 0;
       for(Object word : lexer.getWords().keySet()){
           keywords[i][j]=String.valueOf(i+1);
           keywords[i][j+1]=word.toString();
           i++;
       }
   }

    private static void fillBoarders(){
        boarders = new String[lexer.getBorders().size()][2];
        int j=0; int i = 0;
        for(Object word : lexer.getBorders().keySet()){
            boarders[i][j]=String.valueOf(i+1);
           boarders[i][j+1]=word.toString();
            i++;
        }
    }

   private static void fillNums(){
        numbers = new String[lexer.getNumbers().size()][2];
        System.out.println(lexer.getNumbers().size());
        int j=0; int i = 0;
        for(String num : lexer.getNumbers()){
            numbers[i][j]=String.valueOf(i+1);
            numbers[i][j+1]=num;
            System.out.println(num);
            i++;
        }
    }


    private static void fillVariables(){
        variables = new String[lexer.getVariables().size()][2];
        int j=0; int i = 0;
        for(String num : lexer.getVariables()){
            variables[i][j]=String.valueOf(i+1);
            variables[i][j+1]=num;
            i++;
        }
    }

    private void createUIComponents() {

        textAreaProgram = new JTextArea();
        textAreaProgram.setEditable(true);

        textAreaErrors = new JTextArea();
        button1 = new JButton();


        fillKeywords();
        tableWords = new JTable(keywords,keywordsColumns);

        fillBoarders();
        tableBoarders = new JTable(boarders,keywordsColumns);

        fillNums();
        tableNums = new JTable(numbers,keywordsColumns);

        fillVariables();
        tableVariables = new JTable(variables,keywordsColumns);

    }
}
