package main;

import entity.Entity;
import entity.Exit;
import entity.NPCRobot;
import entity.Player;
import entity.PressurePlate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import tile.TileManager;

public class GamePanel extends JPanel {
    public final int tileSize = 72;
    public final int maxScreenCol = 10;
    public final int maxScreenRow = 8;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screeHeight = tileSize * maxScreenRow;
    
    public int currentLevel = 1;
    public final int totalLevels = 4;
    public int highestUnlockedLevel = 1;
    public boolean inMenu = true;

    public final TileManager tileM = new TileManager(this);
    final KeyHandler keyH = new KeyHandler();
    final Player player = new Player(this, keyH);
    public final Entity[] npc = new Entity[10];
    public final List<PressurePlate> plates = new ArrayList<>();
    public Exit exit;
    private final AssetSetter assetSetter = new AssetSetter(this);
    private GameState state = GameState.PLANNING;
    private String status = "";
    private Runnable stateListener = () -> {};
    private final Timer timer = new Timer(16, event -> update(System.nanoTime()));

    /*
     Sets up the panel size, keyboard ipnut and starting map objects. It begins in
     the planning phase so the player can draw arrows.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screeHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);
        addFocusListener(new FocusAdapter() {
            /*
             Clears pending mvoement when the panel loses keyboard focus. This
             prevents a saved key press from being carried over after siwtching
             windows.
             */
            @Override public void focusLost(FocusEvent event) { 
                keyH.clear(); 
            }
        });
        assetSetter.setAssets();
        setState(GameState.PLANNING, "");
    }

    /*
     Starts the Swing timer and asks the panel to receive keyboard input. Each timer
     event updates mvoement and requests a redraw.
     */
    public void startGameLoop() {
        timer.start();
        requestFocusInWindow();
    }

    /*
     Stops the timer that drives game updates. Closing the window calls this method
     so the gmae no longer needs to keep updating or requesting new frames.
     */
    public void stopGameLoop() {
        timer.stop();
    }

    /*
     Returns the current game phase. Other calsses use it to decide whether the
     player can plan paths, robots can move, or the level has ended.
     */
    public GameState getGameState() { 
        return state; 
    }
    /*
     Returns the msesage for the current phase. The window displays it when there is
     something to show.
     */

    public String getStatus() { 
        return status; 
    }
    /*
     Allows player movement druing planning and after all robots have parked.
     */

    public boolean canPlayerMove() { 
        return state == GameState.PLANNING || state == GameState.EXIT_READY; 
    }
    /*
     Stores the action that refreshes the window controls when the gmae phase
     changes. It runs that action immediately as well, so the buttons and status
     start with the correct apeparance.
     */

    public void setStateListener(Runnable listener) { 
        stateListener = listener; 
        listener.run();
    }

    /*
     Changes the game phase and its message together. It also clears pending keyboard
     input, refreshes the cnotrols and requests a redraw.
     */
    private void setState(GameState nextState, String message) {
        state = nextState;
        status = message;
        keyH.clear();
        stateListener.run();
        repaint();
    }

    /*
    * Updates player and robot movement when gameplay is active.
    * Pauses updates while the menu is displayed.
    */
    public void update(long now) {
        if (inMenu) {
            return;
        }
        if (canPlayerMove()) {
            player.update();
        } else {
            keyH.clear();
        }
        if (state == GameState.RUNNING) {
            for (Entity entity : npc) {
                if (entity instanceof NPCRobot && state == GameState.RUNNING) {
                    ((NPCRobot) entity).updateMovement(now);
                }
            }
        }
        repaint();
    }

    /*
     Checks the requested step against walls, robots and the locked exit. A valid
     palnning move leaves an arrow behind and alternates the walking picture.
     */
    public boolean tryMovePlayer(String direction) {
        if (!canPlayerMove()) { 
            return false; 
        }
        int nextX = player.x;
        int nextY = player.y;
        switch (direction) {
            case "up": nextY -= tileSize; 
            break;
            case "down": nextY += tileSize; 
            break;
            case "left": nextX -= tileSize; 
            break;
            case "right": nextX += tileSize; 
            break;
            default: return false;
        }
        if (!tileM.canMoveTo(nextX, nextY) || robotAt(nextX, nextY, null) != null
                || (isExitAt(nextX, nextY) && !exit.unlocked)) {
            return false;
        }
        if (state == GameState.PLANNING) {
            tileM.placeArrow(player.x, player.y, direction);
        }
        player.x = nextX;
        player.y = nextY;
        player.direction = direction;
        player.nextAnimationFrame();
        
        /*
        * Unlocks the following level when the current level is completed.
        */
        if (state == GameState.EXIT_READY && isExitAt(nextX, nextY)) {
            if (currentLevel < totalLevels) {
                if (currentLevel + 1 > highestUnlockedLevel) {
                    highestUnlockedLevel = currentLevel + 1;
                }
            }
            setState(GameState.WON, "Level " + currentLevel + " complete!");
        }
        return true;
    }

    /*
     Uses the current time to start the blue rboot when the button is clicked. The
     other version accepts a time directly, allowing movement checks to use
     perdictable timing.
     */
    public void startBlueRobot() { 
        startBlueRobot(System.nanoTime()); 
    }

    /*
     Starts the blue robot chain from the planning phase. It locks player movement
     before activating bule.
     */
    public void startBlueRobot(long now) {
        if (state != GameState.PLANNING) { 
            return; 
        }
        setState(GameState.RUNNING, "Robots running. Player movement is locked.");
        activateRobot("blue", now);
    }

    /*
     Finds the robot with the requested color and starts it. If that robot is already
     parked, the chain cnanot continue and the attempt ends.
     */
    private void activateRobot(String color, long now) {
        for (Entity entity : npc) {
            if (entity instanceof NPCRobot) {
                NPCRobot robot = (NPCRobot) entity;
                if (robot.color.equals(color)) {
                    if (robot.locked) {
                        gameOver("The chain ended before every robot reached a plate.");
                    } else {
                        robot.activate(now);
                    }
                    return;
                }
            }
        }
        gameOver("No robot matches the " + color + " pressure plate.");
    }

    /*
     Checks the remaining robots and plates after a robot parks. When all are
     fniished, it opens the exit and unlocks the player.
     */
    public void robotReachedPlate(PressurePlate plate, long now) {
        boolean allPlates = true;
        for (PressurePlate item : plates) {
            if (!item.activated) { 
                allPlates = false; 
            }
        }
        exit.unlocked = allPlates;
        boolean allRobots = true;
        for (Entity entity : npc) {
            if (entity instanceof NPCRobot && !((NPCRobot) entity).locked) {
                allRobots = false;
            }
        }
        if (allPlates && allRobots) {
            setState(GameState.EXIT_READY, "All robots parked! Walk to the green exit.");
        } else {
            activateRobot(plate.color, now);
        }
    }

    /*
     Looks for a robot at the supplied pixel position. The optional ecxluded robot is
     skipped so a moving robot does not collide with itself.
     */
    public NPCRobot robotAt(int x, int y, NPCRobot except) {
        for (Entity entity : npc) {
            if (entity instanceof NPCRobot && entity != except && entity.x == x && entity.y == y) {
                return (NPCRobot) entity;
            }
        }
        return null;
    }

    /*
     Looks through the pressure paltes for one at the supplied pixel position. The
     arriving robot uses the result to decide whether to park.
     */
    public PressurePlate getPlateAt(int x, int y) {
        for (PressurePlate plate : plates) {
            if (plate.x == x && plate.y == y) { 
                return plate; 
            }
        }
        return null;
    }

    /*
     Checks wehther the supplied pixel position matches the exit.
     */
    public boolean isExitAt(int x, int y) { 
        return exit != null && exit.x == x && exit.y == y; 
    }
    /*
     Checks whether the player occupies the supplied pixel psoition. A robot uses
     this before starting its next step to detect a collision with the locked player.
     */

    public boolean isPlayerAt(int x, int y) { 
        return player.x == x && player.y == y; 
    }

    /*
     Ends an active rboot run and saves the reason for the game over screen.
     */
    public void gameOver(String reason) {
        if (state == GameState.RUNNING) {
            setState(GameState.GAME_OVER, reason);
        }
    }

    /*
     * Loads the selected level and resets its map, entities and game state.
     */
    public void loadLevel(int level) {
        currentLevel = level;
        tileM.loadMap(currentLevel);
        assetSetter.setAssets();
        setState(GameState.PLANNING, "");
        requestFocusInWindow();
    }

    /*
    * Restarts the level and places the saved solution arrows.
    * The robots only move when the player presses Move blue robot.
    */
    public void showSolution() {
        restartLevel();
        tileM.loadSolution(currentLevel);
        requestFocusInWindow();
        repaint();
    }

    /*
    * Restarts the currently selected level.
    */
    public void restartLevel() {
        loadLevel(currentLevel);
    }

    @Override
    /*
     Draws the map first, then the exit, plates, rboots and player. Pixel scaling
     keeps the sprites sharp.
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        tileM.draw(g2);
        exit.draw(g2, tileSize);
        for (PressurePlate plate : plates) { 
            plate.draw(g2, tileSize); 
        }

        for (Entity entity : npc) { 
            if (entity != null) { 
                entity.draw(g2); 
            } 
        }

        player.draw(g2);
        if (state == GameState.GAME_OVER || state == GameState.WON) {
            g2.setColor(new Color(12, 20, 28, 220));
            g2.fillRect(0, 0, screenWidth, screeHeight);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
            if (state == GameState.GAME_OVER) {
                drawCentered(g2, "Game over", 235);
            }
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            drawCentered(g2, status, 280);
            /*
            * Displays the available options after winning or losing.
            */
            if (state == GameState.WON) {
                drawCentered(g2, "Choose Next level or Menu below.", 320);
            } else {
                drawCentered(g2, "Choose Restart or Menu below.", 320);
            }
        }
        g2.dispose();
    }

    /*
     Measures the width of a message and draws it hroizontally centered at the
     supplied height. The end screen uses this for its title, reason and restart
     instruction.
     */
    private void drawCentered(Graphics2D graphics, String text, int y) {
        graphics.setFont(MenuStyle.pixelFont(12));
        int width = graphics.getFontMetrics().stringWidth(text);
        graphics.drawString(text, (screenWidth - width) / 2, y);
    }
}
