package main;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class MenuStyle {

    /*
    Stores the colors and styling used by the menus.
    */
    public static Color background = new Color(25, 29, 37);
    public static Color panel = new Color(48, 56, 70);
    public static Color buttonColor = new Color(76, 88, 104);
    public static Color gold = new Color(229, 184, 92);
    public static Color text = new Color(242, 235, 217);

    /*
    Loads the pixel font at the requested size.
    */
    public static Font pixelFont(float size) {
        try {
            Font font = Font.createFont(
                Font.TRUETYPE_FONT,
                MenuStyle.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf")
            );

            return font.deriveFont(size);

        } catch (Exception e) {
            return new Font("Arial", Font.PLAIN, (int) size);
        }
    }

    /*
    Applies the same styling to each button.
    */
    public static void styleButton(JButton button) {
        button.setFont(pixelFont(12));
        button.setBackground(buttonColor);
        button.setForeground(text);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(gold, 3));
    }
}