package entity;

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

    public Entity(GamePanel gp) {
        this.gp = gp;
    }
}
