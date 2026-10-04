package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Player;

public class GamePanel extends JPanel implements Runnable {
    
    //Screen Settings
    final int originalTileSize = 16; // 16x16 tile size
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile size
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screeHeight = tileSize * maxScreenRow; // 576 pixels

    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    //set default player position
    int playerX = tileSize * 3;
    int playerY = tileSize * 3;


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screeHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps; //0.01666 seconds per update
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while (gameThread != null) {
            
            update();   // update information

            repaint();  // redraw updated screen

            try {
                double remainintTime = nextDrawTime - System.nanoTime();
                remainintTime = remainintTime / 1000000;

                if (remainintTime < 0) {
                    remainintTime = 0;
                }

                Thread.sleep((long) remainintTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void update() {

        player.update();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();
    }
}
