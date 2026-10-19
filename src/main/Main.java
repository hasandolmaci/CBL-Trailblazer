package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
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

            /*
            * Creates separate menu and gameplay screens within the same window.
            */
            GamePanel gamePanel = new GamePanel();

            CardLayout cardLayout = new CardLayout();
            JPanel screens = new JPanel(cardLayout);

            JPanel menuPanel = new JPanel(new BorderLayout());
            JPanel gameScreen = new JPanel(new BorderLayout());

            gameScreen.add(gamePanel, BorderLayout.CENTER);

            JButton moveButton = new JButton("Move blue robot");
            JButton restartButton = new JButton("Restart");
            JButton menuButton = new JButton("Menu");
            JButton nextButton = new JButton("Next level");
            nextButton.setFocusable(false);
            menuButton.setFocusable(false);
            moveButton.setFocusable(false);
            restartButton.setFocusable(false);
            JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
            controls.add(moveButton);
            controls.add(restartButton);
            controls.add(menuButton);
            controls.add(nextButton);
            gameScreen.add(controls, BorderLayout.SOUTH);

            /*
            * Disables level buttons that have not been unlocked yet.
            */

            moveButton.addActionListener(event -> {
                gamePanel.startBlueRobot();
                gamePanel.requestFocusInWindow();
            });
            restartButton.addActionListener(event -> gamePanel.restartLevel());
            gamePanel.setStateListener(() -> {
                nextButton.setVisible(
                    gamePanel.getGameState() == GameState.WON
                    && gamePanel.currentLevel < gamePanel.totalLevels
                );
                window.pack();
                moveButton.setEnabled(gamePanel.getGameState() == GameState.PLANNING);
            });
            window.addWindowListener(new WindowAdapter() {
                /*
                 Stops the game timer when the window closes. This keeps updates from
                 continuing after the player has fniished using the window.
                 */
                @Override public void windowClosing(WindowEvent event) { 
                    gamePanel.stopGameLoop(); 
                }
            });

            /*
            * Creates the level-selection buttons and loads the selected level.
            */
            JPanel levelButtons = new JPanel(new GridLayout(4, 1, 10, 10));
            JButton[] levels = new JButton[4];

            for (int i = 0; i < 4; i++) {
                int levelNumber = i + 1;

                JButton button = new JButton("Level " + levelNumber);
                levels[i] = button;
                levelButtons.add(button);

                button.addActionListener(event -> {
                    if (levelNumber <= gamePanel.highestUnlockedLevel) {
                        gamePanel.inMenu = false;
                        gamePanel.loadLevel(levelNumber);
                        cardLayout.show(screens, "GAME");
                        gamePanel.requestFocusInWindow();
                    }
                });
            }

            /*
            * Refreshes unlocked levels and returns to the menu.
            */
            menuButton.addActionListener(event -> {
                gamePanel.inMenu = true;

                for (int i = 0; i < 4; i++) {
                    int levelNumber = i + 1;

                    if (levelNumber <= gamePanel.highestUnlockedLevel) {
                        levels[i].setEnabled(true);
                        levels[i].setText("Level " + levelNumber);
                    } else {
                        levels[i].setEnabled(false);
                        levels[i].setText("Level " + levelNumber + " - Locked");
                    }
                }

                cardLayout.show(screens, "MENU");
            });

            /*
            * Loads the next level after successfully completing the current one.
            */
            nextButton.addActionListener(event -> {
                if (gamePanel.getGameState() == GameState.WON
                        && gamePanel.currentLevel < gamePanel.totalLevels) {
                    gamePanel.loadLevel(gamePanel.currentLevel + 1);
                }
            });

            menuPanel.add(levelButtons, BorderLayout.CENTER);

            /*
            * Registers both screens and displays the menu when the game starts.
            */
            screens.add(menuPanel, "MENU");
            screens.add(gameScreen, "GAME");

            window.add(screens);
            cardLayout.show(screens, "MENU");

            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gamePanel.startGameLoop();

            for (int i = 0; i < 4; i++) {
                int levelNumber = i + 1;

                if (levelNumber <= gamePanel.highestUnlockedLevel) {
                    levels[i].setEnabled(true);
                    levels[i].setText("Level " + levelNumber);
                } else {
                    levels[i].setEnabled(false);
                    levels[i].setText("Level " + levelNumber + " - Locked");
                }
            }
        });
    }
}
