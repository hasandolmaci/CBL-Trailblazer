package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class IntroductionPanel extends JPanel {

    public JButton proceedButton = new JButton("Proceed to Menu");

    public IntroductionPanel() {
        setLayout(new BorderLayout(10, 20));

        setBackground(MenuStyle.background);

        setBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );

        JLabel title = new JLabel("TRAILBLAZER", JLabel.CENTER);
        title.setFont(MenuStyle.pixelFont(24));
        title.setForeground(MenuStyle.gold);
        add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(MenuStyle.pixelFont(11));
        textArea.setBackground(MenuStyle.panel);
        textArea.setForeground(MenuStyle.text);
        textArea.setCaretColor(MenuStyle.text);
        textArea.setMargin(new java.awt.Insets(25, 25, 25, 25));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    getClass().getResourceAsStream("/introduction.txt")
                )
            );

            String line;

            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(false);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(MenuStyle.gold, 3)
        );

        scrollPane.getViewport().setBackground(MenuStyle.panel);

        add(scrollPane, BorderLayout.CENTER);

        MenuStyle.styleButton(proceedButton);

        proceedButton.setPreferredSize(
            new java.awt.Dimension(0, 65)
        );

        add(proceedButton, BorderLayout.SOUTH);
    }
}