package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {
    /*
     Creates the game window, sattus label and control buttons on the Swing event
     thread. The buttons start the blue robot or restart the lveel.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("TrailBlazer");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel, BorderLayout.CENTER);

            JLabel status = new JLabel();
            status.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            window.add(status, BorderLayout.NORTH);
            JButton moveButton = new JButton("Move blue robot");
            JButton restartButton = new JButton("Restart");
            moveButton.setFocusable(false);
            restartButton.setFocusable(false);
            JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
            controls.add(moveButton);
            controls.add(restartButton);
            window.add(controls, BorderLayout.SOUTH);

            moveButton.addActionListener(event -> {
                gamePanel.startBlueRobot();
                gamePanel.requestFocusInWindow();
            });
            restartButton.addActionListener(event -> gamePanel.restartLevel());
            gamePanel.setStateListener(() -> {
                status.setText(gamePanel.getStatus());
                status.setVisible(!gamePanel.getStatus().isEmpty());
                window.pack();
                moveButton.setEnabled(gamePanel.getGameState() == GameState.PLANNING);
            });
            window.addWindowListener(new WindowAdapter() {
                /*
                 Stops the game timer when the window closes. This keeps updates from
                 continuing after the player has fniished using the window.
                 */
                @Override public void windowClosing(WindowEvent event) { gamePanel.stopGameLoop(); }
            });
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gamePanel.startGameLoop();
        });
    }
}
