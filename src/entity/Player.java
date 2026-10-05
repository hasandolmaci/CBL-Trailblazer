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

        if (keyH.upPressed) {
            y -= gp.tileSize;
            keyH.upPressed = false;
        }
        if (keyH.downPressed) {
            y += gp.tileSize;
            keyH.downPressed = false;
        }
        if (keyH.rightPressed) {
            x += gp.tileSize;
            keyH.rightPressed = false;
        }
        if (keyH.leftPressed) {
            x -= gp.tileSize;
            keyH.leftPressed = false;
        }
    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.white);

        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
    }
}
