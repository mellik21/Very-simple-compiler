package Compiler;

import lexer.Lexer;
import parser.Parser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

    private static String[][] keywords;
    private static String[][] boarders;
    private static String[] keywordsColumns = new String[]{"№", "Word"};
    private static Lexer lexer = new Lexer();

    public MainGui() {
        setSize(new Dimension(800, 600));
        setContentPane(panel1);
        panel1.setBackground(new Color(200, 200, 200));
        setVisible(true);


        RadioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n  var a : ! ; \n if 1 LT 2  \n then \n let a = 3 ; \n else \n a = 2 ; \n end_else \n } end");
            }
        });
        radioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n var a , b , c , d : ! ; \n let a = .123E+13 ; \n b = 011D ; \n c = 011XYZ ; \n d = F2C22A3H ; \n } end");
            }
        });
        radioButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n var a : ! ; \n let a = 1 \n do a LT 10 \n a = a plus 1 loop \n } end\n");
            }
        });
        RadioButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textAreaProgram.setText("");
                textAreaProgram.append("begin { \n input ( a ) \n output ( a plus 1 ) } end");
            }
        });

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               clearAll();
                try {
                    lexer = new Lexer();
                    lexer.setProgram(textAreaProgram.getText().getBytes());
                    Parser parser = new Parser(lexer);
                    parser.start();

                    if(parser.getError()!=null){
                        System.out.println("GET ERROR");
                        System.out.println(parser.getError());
                    }
                }catch (Throwable e1){

                }
                textAreaErrors.setText("");
                if(lexer.getError()!=null){

                    textAreaErrors.append(lexer.getError());
                }else{
                    textAreaErrors.append("Aнализ проведен успешно!");
                }
                fillNums();
                fillVariables();

                textAreaAn1.setText("");
                textAreaAn1.setLineWrap(true);
                textAreaAn1.setWrapStyleWord(true);
                textAreaAn1.append(lexer.getResult().toString());
            }
        });
    }

    private static void fillKeywords() {
        keywords = new String[lexer.getWords().size()][2];
        int j = 0;
        int i = 0;
        for (Object word : lexer.getWords().keySet()) {
            keywords[i][j] = String.valueOf(i + 1);
            keywords[i][j + 1] = word.toString();
            i++;
        }
    }

    private static void fillBoarders() {
        boarders = new String[lexer.getBorders().size()][2];
        int j = 0;
        int i = 0;
        for (Object word : lexer.getBorders().keySet()) {
            boarders[i][j] = String.valueOf(i + 1);
            boarders[i][j + 1] = word.toString();
            i++;
        }
    }

    private void fillNums() {
        int i = 0;
        for (String num : lexer.getNumbers()) {
            numsModel.addRow(new String[]{String.valueOf(i + 1),num});
            i++;
        }
    }


    private static void fillVariables() {
        int i = 0;
        for (String var : lexer.getVariables()) {
            varsModel.addRow(new String[]{String.valueOf(i + 1),var});
            i++;
        }
    }

    private static void clearAll(){
        for(int i = numsModel.getRowCount() - 1; i >= 0; i--) {
            numsModel.removeRow(i);
        }

        for(int i =varsModel.getRowCount() - 1; i >= 0; i--) {
            varsModel.removeRow(i);
        }
    }

    private static DefaultTableModel numsModel = new DefaultTableModel(keywordsColumns,0);
    private static DefaultTableModel varsModel = new DefaultTableModel(keywordsColumns,0);
    private void createUIComponents() {

        textAreaProgram = new JTextArea();
        textAreaProgram.setEditable(true);

        textAreaErrors = new JTextArea();
        button1 = new JButton();


        fillKeywords();
        tableWords = new JTable(keywords, keywordsColumns);

        fillBoarders();
        tableBoarders = new JTable(boarders, keywordsColumns);

        tableNums = new JTable(numsModel);

        tableVariables = new JTable(varsModel);

    }
}
