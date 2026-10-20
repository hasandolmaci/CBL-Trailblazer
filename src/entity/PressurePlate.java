package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.ImageLoader;

public class PressurePlate {
    public final String color;
    public final int x;
    public final int y;
    public boolean activated;
    private final BufferedImage image;

    /*
     Stores the plate color and pixel position taken from the map. It loads the
     matching plate pciture.
     */
    public PressurePlate(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
        
        if (color.equals("blue")) {
            image = ImageLoader.load("/plates/plate_gold.png");
        } else {
            image = ImageLoader.load("/plates/plate_" + color + ".png");
        }
    }

    /*
     Draws the plate at its map position. When a robot has activated it, a light
     border is added so its state is vsiible.
     */
    public void draw(Graphics2D graphics, int tileSize) {
        graphics.drawImage(image, x, y, tileSize, tileSize, null);
        if (activated) {
            graphics.setColor(new Color(255, 235, 150));
            graphics.drawRect(x + 2, y + 2, tileSize - 5, tileSize - 5);
        }
    }
}
