package main;

import entity.Exit;
import entity.NPCRobot;
import entity.PressurePlate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AssetSetter {
    private final GamePanel gp;

    /*
     Keeps a reference to the game panel so the map setup can fill its robot and
     plate collections. The smae setup object is reused when restarting the level.
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    /*
     Reads each map number and creates the matching robot, palte, exit or player
     position. Entity markers become ordinary floor in memory.
     */
    public void setAssets() {
        Arrays.fill(gp.npc, null);
        gp.plates.clear();
        gp.exit = null;
        String[] colors = {"blue", "red", "green", "purple"};
        String[] directions = {"up", "down", "left", "right"};
        Set<String> robotColors = new HashSet<>();
        Set<String> plateColors = new HashSet<>();
        int robotIndex = 0;
        int playerCount = 0;

        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int num = gp.tileM.getTileNum(col, row);
                int x = col * gp.tileSize;
                int y = row * gp.tileSize;
                if (num >= 20 && num <= 35) {
                    String color = colors[(num - 20) / 4];
                    if (!robotColors.add(color)) {
                        throw new IllegalStateException("Duplicate robot color: " + color);
                    }
                    NPCRobot robot = new NPCRobot(gp, color, directions[(num - 20) % 4]);
                    robot.x = x;
                    robot.y = y;
                    gp.npc[robotIndex++] = robot;
                } else if (num >= 36 && num <= 39) {
                    String color = colors[num - 36];

                    if (!plateColors.add(color)) {
                        throw new IllegalStateException("Duplicate plate color: " + color);
                    }

                    gp.plates.add(new PressurePlate(x, y, color));
                } else if (num == 40) {
                    if (gp.exit != null) {
                        throw new IllegalStateException("The map must have exactly one exit.");
                    }
                    gp.exit = new Exit(x, y);
                } else if (num == 41) {
                    playerCount++;
                    gp.player.x = x;
                    gp.player.y = y;
                    gp.player.direction = "down";
                    gp.player.spriteNum = 1;
                } else if (num < 0 || (num >= 20 && num != 42)) {
                    throw new IllegalStateException("Unknown map number: " + num);
                }

                if (num >= 20 && num != 42) {
                    int[] floorPattern = {0, 2, 1, 3};
                    int floorIndex = Math.floorMod(col - 1 + Math.floorMod(row - 1, 2), 4);
                    gp.tileM.setTileNum(col, row, floorPattern[floorIndex]);
                }
            }
        }
        if (!robotColors.contains("blue") || !robotColors.equals(plateColors)) {
            throw new IllegalStateException("Use one matching plate per robot color, including blue.");
        }
        if (gp.exit == null || playerCount != 1) {
            throw new IllegalStateException("The map needs one exit (40) and one player (41).");
        }
    }
}
