package tile;

import java.awt.image.BufferedImage;
import main.ImageLoader;

public class ArrowTiles {
    private final BufferedImage[][] images = new BufferedImage[4][4];

    /*
     Loads all sixteen arrow pictures once. The outer loop selects a floor texture
     and the inenr loop selects a direction.
     */
    public ArrowTiles() {
        String[] directions = {"right", "down", "left", "up"};
        for (int floor = 0; floor < 4; floor++) {
            for (int direction = 0; direction < directions.length; direction++) {
                String path = "/tiles/arrows/Floor" + (floor + 1)
                    + "_" + directions[direction] + ".png";
                images[floor][direction] = ImageLoader.load(path);
            }
        }
    }

    /*
     Returns the arrow picture matching the floor number and direction. The floor
     sleects a row in the image array and the direction selects a column.
     */
    public BufferedImage getImage(int floor, String direction) {
        switch (direction) {
            case "right": return images[floor][0];
            case "down": return images[floor][1];
            case "left": return images[floor][2];
            case "up": return images[floor][3];
            default: return null;
        }
    }
}
