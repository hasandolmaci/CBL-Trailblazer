package entity;

import javax.imageio.ImageIO;
import main.GamePanel;

public class NPCRobot extends Entity{

    public NPCRobot(GamePanel gp) {
        super(gp);

        direction = "down";

        getImage();
    }

    public void getImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_down_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_down_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_left_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_left_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_right_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_right_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_up_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/npc/robot_up_2.png"));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void setNPC() {

        gp.npc[0] = new NPCRobot(gp);
    }
}