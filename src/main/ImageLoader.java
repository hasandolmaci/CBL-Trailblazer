package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public final class ImageLoader {
    /*
     Prevents cerating an object just to load a picture. Other classes call the
     shared load method directly, since tihs helper has no separate state to store.
     */
    private ImageLoader() {}

    /*
     Opens one image from the resource folders and returns it ready for darwing. The
     stream closes automatically.
     */
    public static BufferedImage load(String path) {
        try (InputStream stream = ImageLoader.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Missing image: " + path);
            }
            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                throw new IllegalStateException("Invalid image: " + path);
            }
            return image;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read image: " + path, e);
        }
    }
}
