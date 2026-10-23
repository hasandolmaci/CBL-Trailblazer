package main;

import entity.Exit;
import entity.NPCRobot;
import entity.PressurePlate;
import java.util.Arrays;

public class AssetSetter {
    private final GamePanel gp;

    /*
    Stores the game panel used to set up each level.
    */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /*
    Reads the map and places the robots, plates, exit and player.
    */
    public void setAssets() {
        Arrays.fill(gp.npc, null);
        gp.plates.clear();
        gp.exit = null;
        String[] colors = {"blue", "red", "green", "purple"};
        String[] directions = {"up", "down", "left", "right"};

        int robotIndex = 0;

        // Create the entities using their map numbers.
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int num = gp.tileM.getTileNum(col, row);
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;
                if (num >= 20 && num <= 35) {
                    String color = colors[(num - 20) / 4];
                    
                    NPCRobot robot = new NPCRobot(gp, color, directions[(num - 20) % 4]);
                    robot.x = x;
                    robot.y = y;
                    gp.npc[robotIndex++] = robot;
                } else if (num >= 36 && num <= 39) {
                    String color = colors[num - 36];

                    gp.plates.add(new PressurePlate(x, y, color));
                } else if (num == 40) {
                    gp.exit = new Exit(x, y);
                } else if (num == 41) {
                    gp.player.x = x;
                    gp.player.y = y;
                    gp.player.direction = "down";
                    gp.player.spriteNum = 1;
                }

                // Replace entity markers with the correct floor tile.
                if (num >= 20 && num != 42) {
                    int[] floorPattern = {0, 2, 1, 3};
                    int floorIndex = Math.floorMod(col - 1 + Math.floorMod(row - 1, 2), 4);
                    gp.tileM.setTileNum(col, row, floorPattern[floorIndex]);
                }
            }
        }
    }
}
