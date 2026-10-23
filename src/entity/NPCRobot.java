package entity;

import java.awt.Graphics2D;
import main.GamePanel;
import main.GameState;
import main.ImageLoader;

public class NPCRobot extends Entity {
    public static final long STEP_DURATION_NANOS = 350_000_000L;
    public final String color;
    public boolean active;
    public boolean locked;
    private boolean firstMove = true;
    private boolean stepping;
    private int targetX;
    private int targetY;
    private long stepStarted;
    private double progress;
    private long lastAnimationTime = 0;
    private int animationStep = 0;

    /*
    Creates a robot with its color and starting direction.
    */
    public NPCRobot(GamePanel gp, String color, String direction) {
        super(gp);
        this.color = color;
        this.direction = direction;
        getImage(color);
    }

    /*
    Loads the three animation frames for each direction.
    */
    public void getImage(String color) {
        String folder = "/npc/" + color + "/robot_";
        up1 = ImageLoader.load(folder + "up_1.png");
        up2 = ImageLoader.load(folder + "up_2.png");
        up3 = ImageLoader.load(folder + "up_3.png");

        down1 = ImageLoader.load(folder + "down_1.png");
        down2 = ImageLoader.load(folder + "down_2.png");
        down3 = ImageLoader.load(folder + "down_3.png");

        left1 = ImageLoader.load(folder + "left_1.png");
        left2 = ImageLoader.load(folder + "left_2.png");
        left3 = ImageLoader.load(folder + "left_3.png");

        right1 = ImageLoader.load(folder + "right_1.png");
        right2 = ImageLoader.load(folder + "right_2.png");
        right3 = ImageLoader.load(folder + "right_3.png");
    }

    /*
    Starts the robot if it is not already moving or locked.
    */
    public void activate(long now) {
        if (active || locked || gp.getGameState() != GameState.RUNNING) {
            return;
        }
        active = true;
        beginStep(now);
    }

    /*
    Finds the next tile and checks for collisions.
    */
    private void beginStep(long now) {
        if (!firstMove) {
            String arrow = gp.tileM.getArrowDirection(x / gp.tileSize, y / gp.tileSize);
            if (arrow == null) {
                gp.gameOver(color.substring(0, 1).toUpperCase() 
                    + color.substring(1) + " robot reached the end of its arrow path.");
                return;
            }
            direction = arrow;
        }

        targetX = x;
        targetY = y;
        switch (direction) {
            case "up": targetY -= gp.tileSize; 
            break;
            case "down": targetY += gp.tileSize; 
            break;
            case "left": targetX -= gp.tileSize; 
            break;
            case "right": targetX += gp.tileSize; 
            break;
        }

        if (gp.isPlayerAt(targetX, targetY)) {
            gp.gameOver(color + " robot collided with the player.");
            return;
        }
        
        if (gp.robotAt(targetX, targetY, this) != null) {
            gp.gameOver(color + " robot collided with another robot.");
            return;
        }
        stepStarted = now;
        progress = 0;
        stepping = true;
    }

    /*
    Updates the robot's movement, animation and pressure plate activation.
    */
    public void updateMovement(long now) {
        if (!active || locked || !stepping || gp.getGameState() != GameState.RUNNING) {
            return;
        }
        progress = Math.min(1.0, Math.max(0.0,
            (double) (now - stepStarted) / STEP_DURATION_NANOS));

        if (now - lastAnimationTime >= 200_000_000L) {
            lastAnimationTime = now;
            animationStep++;

            if (animationStep > 3) {
                animationStep = 0;
            }

            if (animationStep == 0 || animationStep == 2) {
                spriteNum = 1;
            } else if (animationStep == 1) {
                spriteNum = 2;
            } else if (animationStep == 3) {
                spriteNum = 3;
            }
        
        }
        if (progress < 1.0) {
            return;
        }
        x = targetX;
        y = targetY;
        stepping = false;
        firstMove = false;

        PressurePlate plate = gp.getPlateAt(x, y);
        if (plate != null) {
            locked = true;
            active = false;
            spriteNum = 1;
            plate.activated = true;
            gp.robotReachedPlate(plate, now);
        } else {
            beginStep(now);
        }
    }

    /*
    Draws the robot between tiles while it is moving.
    */
    @Override
    public void draw(Graphics2D graphics) {
        Graphics2D copy = (Graphics2D) graphics.create();
        if (stepping) {
            copy.translate(Math.round((targetX - x) * progress),
                           Math.round((targetY - y) * progress));
        }
        super.draw(copy);
        copy.dispose();
    }
}
