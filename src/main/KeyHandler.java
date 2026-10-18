package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;

    /*
     Resets all four saved movement requests. The game uses this after cnosuming a
     move and when changing phases, so an old key press does not unexpectedly move
     the player later.
     */
    public void clear() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    @Override
    /*
     Does not hnadle typed characters because movement uses key press events instead.
     This method is still present bceause the keyboard listener interface requires
     it.
     */
    public void keyTyped(KeyEvent e) {
    }

    @Override
    /*
     Checks whether the pressed key is an arrow key or its mtaching WASD key. It
     marks that direction as requested.
     */
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = true;
        }

    }

    @Override
    /*
     Clears the flag for a released movement key. Both WASD and the arorw keys use
     the same four flags, so either keyboard layout can control the player.
     */
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }

}
