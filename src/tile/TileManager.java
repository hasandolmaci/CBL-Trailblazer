package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import main.ImageLoader;
import main.GamePanel;

public class TileManager {

    GamePanel gp;
    Tile[] tile;
    int[][] mapTileNum;
    private String[][] arrows;
    private final ArrowTiles arrowTiles = new ArrowTiles();

    /*
     Creates storage for the map tiles and loads the folor and wall pictures. It then
     reads the starting map.
     */
    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[20];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    /*
     Returns the number stored at a map column and row. These are gird positions
     rather than pixels.
     */
    public int getTileNum(int col, int row) {
        return mapTileNum[col][row];
    }

    /*
     Changes one map cell in memory. The setup code uses this after raeding an entity
     marker, replacing it with the floor underneath.
     */
    public void setTileNum(int col, int row, int num) {
        mapTileNum[col][row] = num;
    }

    /*
     Loads the floor textures and the original wall and croner pictures. It rotates
     the wall pictures to create the other sides of the room.
     */
    public void getTileImage() {


        tile[0] = new Tile();
        tile[0].image = ImageLoader.load("/tiles/Floor1.png");

        tile[1] = new Tile();
        tile[1].image = ImageLoader.load("/tiles/Floor2.png");

        tile[2] = new Tile();
        tile[2].image = ImageLoader.load("/tiles/Floor3.png");

        tile[3] = new Tile();
        tile[3].image = ImageLoader.load("/tiles/Floor4.png");

        tile[4] = new Tile();
        tile[4].image = ImageLoader.load("/tiles/Wall1.png");

        tile[5] = new Tile();
        tile[5].image = ImageLoader.load("/tiles/Wall2.png");

        tile[6] = new Tile();
        tile[6].image = ImageLoader.load("/tiles/Corner1.png");

        tile[7] = new Tile();
        tile[7].image = ImageLoader.load("/tiles/Corner2.png");


        tile[8] = new Tile();
        tile[8].image = rotateImage(tile[4].image, 90);

        tile[9] = new Tile();
        tile[9].image = rotateImage(tile[5].image, 90);


        tile[10] = new Tile();
        tile[10].image = rotateImage(tile[4].image, 180);

        tile[11] = new Tile();
        tile[11].image = rotateImage(tile[5].image, 180);


        tile[12] = new Tile();
        tile[12].image = rotateImage(tile[4].image, 270);

        tile[13] = new Tile();
        tile[13].image = rotateImage(tile[5].image, 270);


        tile[14] = new Tile();
        tile[14].image = rotateImage(tile[6].image, 90);

        tile[15] = new Tile();
        tile[15].image = rotateImage(tile[7].image, 90);


        tile[16] = new Tile();
        tile[16].image = rotateImage(tile[6].image, 180);

        tile[17] = new Tile();
        tile[17].image = rotateImage(tile[7].image, 180);


        tile[18] = new Tile();
        tile[18].image = rotateImage(tile[6].image, 270);

        tile[19] = new Tile();
        tile[19].image = rotateImage(tile[7].image, 270);
    }

    /*
     Makes a new image and darws the original into it at the requested angle.
     Rotating around the center keeps the wall inside the tile.
     */
    public BufferedImage rotateImage(BufferedImage image, int angle) {

        BufferedImage rotatedImage =
            new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        Graphics2D g2 = rotatedImage.createGraphics();

        g2.rotate(
            Math.toRadians(angle),
            image.getWidth() / 2,
            image.getHeight() / 2
        );

        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return rotatedImage;
    }

    /*
     Reads the map rwos into a fresh array and clears the arrows after loading
     succeeds. Spaces separate the tile nmubers.
     */
    public void loadMap() {
        int[][] freshMap = new int[gp.maxScreenCol][gp.maxScreenRow];
        InputStream stream = getClass().getResourceAsStream("/maps/map01.txt");
        if (stream == null) {
            throw new IllegalStateException("Missing map01.txt");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            for (int row = 0; row < gp.maxScreenRow; row++) {
                String line = reader.readLine();
                String[] numbers = line == null ? new String[0] : line.trim().split("\\s+");
                if (numbers.length != gp.maxScreenCol) {
                    throw new IllegalStateException("Each map row needs " + gp.maxScreenCol + " numbers. Check row " + (row + 1));
                }
                for (int col = 0; col < gp.maxScreenCol; col++) {
                    freshMap[col][row] = Integer.parseInt(numbers[col]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new IllegalStateException("Cannot read map01.txt", e);
        }
        mapTileNum = freshMap;
        arrows = new String[gp.maxScreenCol][gp.maxScreenRow];
    }

    /*
     Checks whether a pixel position is inside the screen and belongs to a floor
     tile. Dividing by the tile size cnoverts pixels into map coordinates.
     */
    public boolean canMoveTo(int x, int y) {
        if (x < 0 || y < 0 || x >= gp.screenWidth || y >= gp.screeHeight) {
            return false;
        }
        int tileNum = mapTileNum[x / gp.tileSize][y / gp.tileSize];
        return tileNum >= 0 && tileNum < 4;
    }

    /*
     Converts a pixel position into a map column and row, then saves the driection
     there if it is floor. The arrow stays in memory.
     */
    public void placeArrow(int x, int y, String direction) {
        if (canMoveTo(x, y)) {
            arrows[x / gp.tileSize][y / gp.tileSize] = direction;
        }
    }

    /*
     Returns the direction saved at a grid position. A tile wtihout an arrow returns
     null.
     */
    public String getArrowDirection(int col, int row) {
        return arrows[col][row];
    }

    /*
     Walks across each row of the map and draws its floor or wall picture. If a clel
     has an arrow, it uses the matching arrow picture instead.
     */
    public void draw(Graphics2D g2) {

        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];

            BufferedImage image = tile[tileNum].image;
            if (arrows[col][row] != null) {
                BufferedImage arrowImage = arrowTiles.getImage(tileNum, arrows[col][row]);
                if (arrowImage != null) { image = arrowImage; }
            }
            g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}
