package entity;

import main.GamePanel;
import main.ImageLoader;
import main.KeyHandler;

public class Player extends Entity {

    KeyHandler keyH;

    /*
     Connects the palyer to the game panel and keyboard handler. It sets a starting
     position and loads the walking pictures.
     */
    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);

        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }

    /*
     Gives the palyer an initial position and makes it face down. These values are
     used during construction, before the map cohoses the actual starting tile.
     */
    public void setDefaultValues() {

        x = gp.tileSize * 3;
        y = gp.tileSize * 3;
        direction = "down";
    }

    /*
     Loads two walking pictures for each of the four directions.
     */
    public void getPlayerImage() {
        up1 = ImageLoader.load("/player/player_up_1.png");
        up2 = ImageLoader.load("/player/player_up_2.png");
        up3 = ImageLoader.load("/player/player_up_3.png");
        up4 = ImageLoader.load("/player/player_up_4.png");

        down1 = ImageLoader.load("/player/player_down_1.png");
        down2 = ImageLoader.load("/player/player_down_2.png");
        down3 = ImageLoader.load("/player/player_down_3.png");
        down4 = ImageLoader.load("/player/player_down_4.png");

        left1 = ImageLoader.load("/player/player_left_1.png");
        left2 = ImageLoader.load("/player/player_left_2.png");
        left3 = ImageLoader.load("/player/player_left_3.png");
        left4 = ImageLoader.load("/player/player_left_4.png");

        right1 = ImageLoader.load("/player/player_right_1.png");
        right2 = ImageLoader.load("/player/player_right_2.png");
        right3 = ImageLoader.load("/player/player_right_3.png");
        right4 = ImageLoader.load("/player/player_right_4.png");
    }

    public void nextAnimationFrame() {

        spriteNum++;

        if (spriteNum > 4) {
            spriteNum = 1;
        }
    }

    @Override
    /*
     Reads one requested driection from the keyboard and clears the saved key
     presses. If movement is allowed, it asks the game panel to try taht move.
     */
    public void update() {
        if (!gp.canPlayerMove()) {
            keyH.clear();
            return;
        }
        String requested = null;
        if (keyH.upPressed) { requested = "up"; }
        else if (keyH.downPressed) { requested = "down"; }
        else if (keyH.rightPressed) { requested = "right"; }
        else if (keyH.leftPressed) { requested = "left"; }
        keyH.clear();
        if (requested != null) {
            gp.tryMovePlayer(requested);
        }
    }
}
