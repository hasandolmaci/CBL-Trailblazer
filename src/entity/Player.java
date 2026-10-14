package entity;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = gp.tileSize * 3;
        y = gp.tileSize * 3;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/player_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/player_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/player_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/player_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/player_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/player_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/player_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/player_right_2.png"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        // to keep player inside the grid
        int maxX = gp.getWidth() - gp.tileSize; // 720px
        int maxY = gp.getHeight() - gp.tileSize; // 528px

        if (keyH.upPressed) {
            if (y - gp.tileSize >= 0 && gp.tileM.canMoveTo(x, y - gp.tileSize)) {
                direction = "up";
                gp.tileM.placeArrow(x, y, direction);
                y -= gp.tileSize;
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }
            keyH.upPressed = false;
        }
        if (keyH.downPressed) {
            if (y + gp.tileSize <= maxY && gp.tileM.canMoveTo(x, y + gp.tileSize)) {
                direction = "down";
                gp.tileM.placeArrow(x, y, direction);
                y += gp.tileSize;
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }
            keyH.downPressed = false;
        }
        if (keyH.rightPressed) {
            if (x + gp.tileSize <= maxX && gp.tileM.canMoveTo(x + gp.tileSize, y)) {
                direction = "right";
                gp.tileM.placeArrow(x, y, direction);
                x += gp.tileSize;
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }
            keyH.rightPressed = false;
        }
        if (keyH.leftPressed) {
            if (x - gp.tileSize >= 0 && gp.tileM.canMoveTo(x - gp.tileSize, y)) {
                direction = "left";
                gp.tileM.placeArrow(x, y, direction);
                x -= gp.tileSize;
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            }
            keyH.leftPressed = false;
        }
    }
}
