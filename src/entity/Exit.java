package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.ImageLoader;

public class Exit {
    public final int x;
    public final int y;
    public boolean unlocked;
    private final BufferedImage closed = ImageLoader.load("/tiles/exit_locked.png");
    private final BufferedImage open = ImageLoader.load("/tiles/exit_open.png");

    /*
    Stores the exit position and starts with the exit locked.
    */
    public Exit(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /*
    Draws the locked or unlocked exit sprite.
    */
    public void draw(Graphics2D graphics, int tileSize) {
        BufferedImage image = closed;
        if (unlocked) {
            image = open;
        }
        graphics.drawImage(image, x, y, tileSize, tileSize, null);
    }
}
