import javax.swing.*;
import java.awt.*;

public class CardGameViewer extends JFrame {
    // Instance variables
    private Game game;
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 900;
    private final int TITLE_BAR_HEIGHT = 23;

    // Constructor
    public CardGameViewer(Game game){
        this.game = game;

        // Setup the window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Windows!");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
    }

    public void paint(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        if(state == 0){
            paintInstructions(g);
        } else if (state == 1){
            paintGame(g);
        } else if (state == 2) {
            //paintEnd(g);
        }
    }

    private void paintInstructions(Graphics g) {
        g.drawString("hi", 100, 100);
        repaint();
    }

    private void paintGame(Graphics g){
    }
}
