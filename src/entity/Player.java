package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    int spriteNum = 1;

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {

        x = gp.tileSize * 3;
        y = gp.tileSize * 3;
        direction = "down";
    }

    public void getPlayerImage(){
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
            if (y - gp.tileSize >= 0) {
                direction = "up";
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
            if (y + gp.tileSize <= maxY) {
                direction = "down";
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
            if (x + gp.tileSize <= maxX) {
                direction = "right";
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
            if (x - gp.tileSize >= 0) {
                direction = "left";
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

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch(direction){
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }

    
}
