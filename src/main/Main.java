package main;

import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("TrailBlazer");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        JButton moveButton = new JButton("Move Robot");
        moveButton.setFocusable(false);
        window.add(moveButton, "North");

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            
            int robNum = scanner.nextInt();
            System.out.println("The robot you are moving is: robot " + robNum);

            moveButton.addActionListener(e -> {
                gamePanel.npc[robNum].startMoving();
            });
        }

    }

}