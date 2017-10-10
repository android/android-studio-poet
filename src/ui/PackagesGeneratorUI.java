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

    private JPanel contentPane;
    private  Controller controller;
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
            controller.generate(textArea.getText());
        });
        contentPane.add(btnGenerate, BorderLayout.SOUTH);

        textArea = new JTextArea();

        textArea.setBackground(new Color(46, 48, 50));
        textArea.setForeground(Color.CYAN);
        textArea.setFont(new Font("Menlo", Font.PLAIN, 18));

        textArea.setText(Controller.SAMPLE_CONFIG);
        textArea.setCaretPosition(textArea.getText().length());
        textArea.setCaretColor(Color.YELLOW);

        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        textArea.setRows(30);
        textArea.setColumns(50);

        contentPane.add(scrollPane, BorderLayout.CENTER);
        pack();

        controller = new Controller(this);
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