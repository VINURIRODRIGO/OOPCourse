import javax.swing.*;
import java.awt.*;

public class CarGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame(); // create car frame
        frame.setTitle("Formula 1 Championship"); //set title of frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit out of the application
        frame.setResizable(false); // prevent from being resized
        frame.setSize(420,420); // set x and y dimension of the frame
        frame.setVisible(true); // make the visible

        ImageIcon imageIcon = new ImageIcon("src/images.png");// create an image icon
        frame.setIconImage(imageIcon.getImage());// change the frame icon
        frame.getContentPane().setBackground(new Color(73, 80, 87)); // change the background colour of the frame
    }
}
