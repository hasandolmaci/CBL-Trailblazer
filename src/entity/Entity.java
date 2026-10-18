package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity {

    GamePanel gp;
    public int x;
    public int y;

    public BufferedImage up1;
    public BufferedImage up2;
    public BufferedImage down1;
    public BufferedImage down2;
    public BufferedImage left1;
    public BufferedImage left2;
    public BufferedImage right1;
    public BufferedImage right2;
    public String direction;
    public int spriteNum = 1;
    public Rectangle solidArea;

    /*
     Stores the game panel so the player and robots can use the smae tile size and
     game rules. It also makes a square collision area matching one tile.
     */
    public Entity(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
    }

    /*
     Chooses the pciture for the direction this entity is facing. The sprite number
     selects one of the two walking frames.
     */
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }

        if (image != null) {
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
        }
    }

    /*
     Leaves a palce for child classes to define their own update behaviour. The
     player replaces this method with keyboard mvoement.
     */
    public void update() {
    }

    /*
     This empty method is left from the earlier movement code. It currently does
     nothing.
     */
    public void startMoving() {
    }
}
