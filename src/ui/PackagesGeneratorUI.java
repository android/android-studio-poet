package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class PackagesGeneratorUI extends JFrame {


    public static final String SAMPLE_CONFIG = "{\n" +
            "  \"root\": \"/Users/bfarber/Desktop/module1/\",\n" +
            "  \"allMethods\": \"4000\",\n" +
            "  \"javaPackageCount\": \"20\",\n" +
            "  \"javaClassCount\": \"8\",\n" +
            "  \"javaMethodCount\": \"2000\",\n" +
            "  \"kotlinPackageCount\": \"20\",\n" +
            "  \"kotlinClassCount\": \"8\"\n" +
            "}";


    private JPanel contentPane;
    // TODO may be to do a decorator with PakagesGenerator
    private ModuleWriter moduleGenerator;
    private JTextArea textArea;

    public PackagesGeneratorUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JLabel lblTextLineExample = new JLabel("Packages Writer");
        lblTextLineExample.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTextLineExample, BorderLayout.NORTH);

        JButton btnGenerate = new JButton("Generate");
        btnGenerate.addActionListener(e -> {
            System.out.println(textArea.getText());
            moduleGenerator.generate(textArea.getText());
        });
        contentPane.add(btnGenerate, BorderLayout.SOUTH);

        textArea = new JTextArea();

        textArea.setBackground(new Color(46, 48, 50));
        textArea.setForeground(Color.CYAN);
        textArea.setFont(new Font("Menlo", Font.PLAIN, 18));

        textArea.setText(SAMPLE_CONFIG);
        textArea.setCaretPosition(textArea.getText().length());
        textArea.setCaretColor(Color.YELLOW);

        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textArea.setRows(30);
        textArea.setColumns(50);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        pack();

        moduleGenerator = new ModuleWriter(this);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                PackagesGeneratorUI frame = new PackagesGeneratorUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}