package main;

import entity.Entity;
import entity.Exit;
import entity.NPCRobot;
import entity.Player;
import entity.PressurePlate;
import java.awt.Color;
import java.awt.Dimension;
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
    Sets up the game panel and keyboard controls.
    */
    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screeHeight));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);
        addFocusListener(new FocusAdapter() {

            /*
            Clears pending movement when the panel loses keyboard focus.
            Prevents a saved key press from being carried over after switching
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
    Starts the game timer and keyboard input.
    */
    public void startGameLoop() {
        timer.start();
        requestFocusInWindow();
    }

    /*
    Stops the game timer.
    */
    public void stopGameLoop() {
        timer.stop();
    }

    /*
    Returns the current game state.
    */
    public GameState getGameState() { 
        return state; 
    }

    /*
    Returns the current status message.
    */
    public String getStatus() { 
        return status; 
    }

    /*
    Checks whether the player is allowed to move.
    */
    public boolean canPlayerMove() { 
        return state == GameState.PLANNING || state == GameState.EXIT_READY; 
    }

    /*
    Sets the method used to update the GUI.
    */
    public void setStateListener(Runnable listener) { 
        stateListener = listener; 
        listener.run();
    }

    /*
    Changes the game state and updates the GUI.
    */
    private void setState(GameState nextState, String message) {
        state = nextState;
        status = message;
        keyH.clear();
        stateListener.run();
        repaint();
    }

    /*
    Updates player and robot movement.
    Pauses updates while the menu is displayed.
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
    Checks and performs the requested player movement.
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
        Unlocks the following level when the current level is completed.
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
    Starts the blue robot using the current time.
    */
    public void startBlueRobot() { 
        startBlueRobot(System.nanoTime()); 
    }

    /*
    Starts the robot chain during the planning phase.
    */
    public void startBlueRobot(long now) {
        if (state != GameState.PLANNING) { 
            return; 
        }
        setState(GameState.RUNNING, "Robots running. Player movement is locked.");
        activateRobot("blue", now);
    }

    /*
    Activates the robot matching the given color.
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
    Checks the plates and activates the next robot.
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
            setState(GameState.EXIT_READY, "All robots parked! Head to the exit.");
        } else {
            activateRobot(plate.color, now);
        }
    }

    /*
    Finds a robot at the given position.
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
    Finds a pressure plate at the given position.
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
    Checks whether a position contains the exit.
    */
    public boolean isExitAt(int x, int y) { 
        return exit != null && exit.x == x && exit.y == y; 
    }

    /*
    Checks whether a position contains the player.
    */
    public boolean isPlayerAt(int x, int y) { 
        return player.x == x && player.y == y; 
    }

    /*
    Ends the robot run and displays the reason.
    */
    public void gameOver(String reason) {
        if (state == GameState.RUNNING) {
            setState(GameState.GAME_OVER, reason);
        }
    }

    /*
    Loads the selected level and resets the game.
    */
    public void loadLevel(int level) {
        currentLevel = level;
        tileM.loadMap(currentLevel);
        assetSetter.setAssets();
        setState(GameState.PLANNING, "");
        requestFocusInWindow();
    }

    /*
    Restarts the level and displays its solution.
    */
    public void showSolution() {
        restartLevel();
        tileM.loadSolution(currentLevel);
        requestFocusInWindow();
        repaint();
    }

    /*
    Restarts the currently selected level.
    */
    public void restartLevel() {
        loadLevel(currentLevel);
    }

    /*
    Draws the map, entities and game messages. Pixel scaling
    keeps the sprites sharp.
    */
    @Override
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
            if (state == GameState.GAME_OVER) {
                drawCentered(g2, "Game over", 235);
            }

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
    Draws text in the center of the screen.
    */
    private void drawCentered(Graphics2D graphics, String text, int y) {
        graphics.setFont(MenuStyle.pixelFont(12));
        int width = graphics.getFontMetrics().stringWidth(text);
        graphics.drawString(text, (screenWidth - width) / 2, y);
    }
}
