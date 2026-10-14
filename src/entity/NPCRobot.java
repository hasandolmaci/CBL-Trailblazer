package entity;

import javax.imageio.ImageIO;
import main.GamePanel;

public class NPCRobot extends Entity {

    int moveCounter = 0;
    boolean moving = false;
    boolean firstMove = true;

    public NPCRobot(GamePanel gp, String color, String direction) {
        super(gp);

        this.direction = direction;
        getImage(color);
    }

    public void getImage(String color) {
        try {
            
            up1 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_up_1.png"));
            up2 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_up_2.png"));

            down1 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_down_1.png"));
            down2 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_down_2.png"));

            left1 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_left_1.png"));
            left2 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_left_2.png"));

            right1 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_right_1.png"));
            right2 = 
            ImageIO.read(getClass().getResourceAsStream("/npc/" + color + "/robot_right_2.png"));

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startMoving() {
        moving = true;
    }

    @Override
    public void update() {

        if (moving) {

            moveCounter++;

            if (moveCounter >= 13) {
                moveCounter = 0;

                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }

                if (firstMove) {

                    firstMove = false;

                    if (direction.equals("up")) {
                        y = y - gp.tileSize;
                    } else if (direction.equals("down")) {
                        y = y + gp.tileSize;
                    } else if (direction.equals("left")) {
                        x = x - gp.tileSize;
                    } else if (direction.equals("right")) {
                        x = x + gp.tileSize;
                    }
                    return;
                }

                int col = x / gp.tileSize;
                int row = y / gp.tileSize;

                String arrow = gp.tileM.getArrowDirection(col, row);

                if (arrow == null) {
                    moving = false;
                    return;
                }

                direction = arrow;

                if (arrow.equals("up")) {
                    y = y - gp.tileSize;
                } else if (arrow.equals("down")) {
                    y = y + gp.tileSize;
                } else if (arrow.equals("left")) {
                    x = x - gp.tileSize;
                } else if (arrow.equals("right")) {
                    x = x + gp.tileSize;
                }
            }
        }
    }
}