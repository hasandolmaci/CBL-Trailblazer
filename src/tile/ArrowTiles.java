package tile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/** Each floor texture has four matching arrow PNGs. */
public class ArrowTiles {
    private final BufferedImage[][] images = new BufferedImage[4][4];

    public ArrowTiles() {
        String[] directions = {"right", "down", "left", "up"};
        for (int floor = 0; floor < 4; floor++) {
            for (int direction = 0; direction < directions.length; direction++) {
                String path = "/tiles/arrows/Floor" + (floor + 1)
                    + "_" + directions[direction] + ".png";
                try (InputStream stream = getClass().getResourceAsStream(path)) {
                    if (stream == null) {
                        throw new IllegalStateException("Missing arrow image: " + path);
                    }
                    images[floor][direction] = ImageIO.read(stream);
                    if (images[floor][direction] == null) {
                        throw new IllegalStateException("Invalid arrow image: " + path);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Cannot load arrow image: " + path, e);
                }
            }
        }
    }

    public BufferedImage getImage(int floor, String direction) {
        switch (direction) {
            case "right": return images[floor][0];
            case "down": return images[floor][1];
            case "left": return images[floor][2];
            case "up": return images[floor][3];
            default: throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }
}
