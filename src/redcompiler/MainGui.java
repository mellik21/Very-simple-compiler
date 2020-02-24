package redcompiler;

import javax.swing.*;
import java.awt.*;

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
    private String[][]keywords;
    private String keywordsColumns ;

    public MainGui(){
        setSize(new Dimension(800,600));
       setContentPane(panel1);
       panel1.setBackground(new Color(200,200,200));
       setVisible(true);
       tableWords = new JTable();



   }
}
