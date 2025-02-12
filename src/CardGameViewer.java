import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class CardGameViewer {
    // Instance variables
    private Game game;
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 900;
    private final int TITLE_BAR_HEIGHT = 23;

    // Constructor
    public CardGameViewer(Game game){
        this.game = game;

        // Load/initiate x and o images


        // Setup the windo and the buffer strategy
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("TicTacToe!");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        createBufferStrategy(2);
    }

    public void paint(Graphics g){
        super.paint(g);
    }
}
