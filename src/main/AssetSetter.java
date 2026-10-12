package main;

import entity.NPCRobot;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setNPC() {

        int npcIndex = 0;

        for (int row = 1; row < gp.maxScreenRow - 1; row++) {
            for (int col = 1; col < gp.maxScreenCol - 1; col++) {

                int num = gp.tileM.getTileNum(col, row);

                if (num >= 20 && num <= 35) {

                    String color = "blue";

                    if (num >= 24 && num <= 27) {
                        color = "red";
                    } else if (num >= 28 && num <= 31) {
                        color = "green";
                    } else if (num >= 32 && num <= 35) {
                        color = "purple";
                    }

                    String direction = "down";

                    int directionNum = (num - 20) % 4;

                    if (directionNum == 0) {
                        direction = "up";
                    } else if (directionNum == 1) {
                        direction = "down";
                    } else if (directionNum == 2) {
                        direction = "left";
                    } else if (directionNum == 3) {
                        direction = "right";
                    }

                    NPCRobot robot = new NPCRobot(gp, color, direction);

                    robot.x = col * gp.tileSize;
                    robot.y = row * gp.tileSize;

                    if (npcIndex < gp.npc.length) {
                        gp.npc[npcIndex] = robot;
                        npcIndex++;
                    }

                    // Restore the floor underneath the robot
                    int[] floorPattern = {0, 2, 1, 3};
                    int floorIndex = (col - 1 + (row - 1) % 2) % 4;

                    gp.tileM.setTileNum(col, row, floorPattern[floorIndex]);
                }
            }
        }
    }
}