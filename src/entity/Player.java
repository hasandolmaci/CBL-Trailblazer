package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
    }

    public void setDefaultValues() {

        x = gp.tileSize * 3;
        y = gp.tileSize * 3;
    }

    public void update() {

        // Keep the entire player square inside the game panel.
        int maxX = gp.getWidth() - gp.tileSize;
        int maxY = gp.getHeight() - gp.tileSize;

        if (keyH.upPressed) {
            if (y - gp.tileSize >= 0) {
                y -= gp.tileSize;
            }
            keyH.upPressed = false;
        }
        if (keyH.downPressed) {
            if (y + gp.tileSize <= maxY) {
                y += gp.tileSize;
            }
            keyH.downPressed = false;
        }
        if (keyH.rightPressed) {
            if (x + gp.tileSize <= maxX) {
                x += gp.tileSize;
            }
            keyH.rightPressed = false;
        }
        if (keyH.leftPressed) {
            if (x - gp.tileSize >= 0) {
                x -= gp.tileSize;
            }
            keyH.leftPressed = false;
        }
    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.white);

        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
    }
}
