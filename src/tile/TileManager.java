package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

public class TileManager {
    
    GamePanel gp;
    Tile[] tile;
    int mapTileNum[][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[20];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {

        try {
            
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Floor1.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Floor2.png"));

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Floor3.png"));

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Floor4.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Wall1.png"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Wall2.png"));

            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Wall3.png"));

            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Corner.png"));


            // RIGHT WALLS
            tile[8] = new Tile();
            tile[8].image = rotateImage(tile[4].image, 90);

            tile[9] = new Tile();
            tile[9].image = rotateImage(tile[5].image, 90);


            // BOTTOM WALLS
            tile[10] = new Tile();
            tile[10].image = rotateImage(tile[4].image, 180);

            tile[11] = new Tile();
            tile[11].image = rotateImage(tile[5].image, 180);


            // LEFT WALLS
            tile[12] = new Tile();
            tile[12].image = rotateImage(tile[4].image, 270);

            tile[13] = new Tile();
            tile[13].image = rotateImage(tile[5].image, 270);


            // TOP-RIGHT CORNERS
            tile[14] = new Tile();
            tile[14].image = rotateImage(tile[6].image, 90);

            tile[15] = new Tile();
            tile[15].image = rotateImage(tile[7].image, 90);


            // BOTTOM-RIGHT CORNERS
            tile[16] = new Tile();
            tile[16].image = rotateImage(tile[6].image, 180);

            tile[17] = new Tile();
            tile[17].image = rotateImage(tile[7].image, 180);


            // BOTTOM-LEFT CORNERS
            tile[18] = new Tile();
            tile[18].image = rotateImage(tile[6].image, 270);

            tile[19] = new Tile();
            tile[19].image = rotateImage(tile[7].image, 270);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void loadMap() {

        try {

            InputStream is = getClass().getResourceAsStream("/maps/map01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
                
                String line = br.readLine();

                while (col < gp.maxScreenCol) {
                    
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }

                if (col == gp.maxScreenCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void draw(Graphics2D g2) {
        
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {

            int tileNum = mapTileNum[col][row];
            
            g2.drawImage(
                tile[tileNum].image,
                x,
                y,
                gp.tileSize,
                gp.tileSize,
                null
            );

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