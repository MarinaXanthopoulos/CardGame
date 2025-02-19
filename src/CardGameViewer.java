import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardGameViewer extends JFrame {
    // Instance variables
    private Game game;
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 900;
    private final int TITLE_BAR_HEIGHT = 23;
    private int state;

    // Constructor
    public CardGameViewer(Game game){
        this.game = game;

        // Setup the window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Windows!");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        state = 1;
    }

    public void paint(Graphics g){

        if(state == 0){
            paintInstructions(g);
        } else if (state == 1){
            paintGame(g);
        } else if (state == 2) {
            //paintEnd(g);
        }
    }

    private void paintInstructions(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 16));
        g.drawString("Welcome to the Windows Card Game!", 450, 75);

        // Display Instructions
        g.setFont(new Font("Serif", Font.PLAIN, 14));
        g.drawString("Instructions:", 200, 150);

        g.drawString("You will be delt 4 cards (no peeking!). Your goal is to get the lowest point value.", 200, 190);
        g.drawString("A is worth 0, 2-10 are worth their normal values, and j = 11, Q = 12, K = -1", 200, 210);
        g.drawString("Your cards are arranged in a window formation: ", 200, 230);
        g.drawString("                Z   Y", 200, 250);
        g.drawString("                X   W", 200, 270);
        g.drawString("To begin, you can only look at two of your cards, try your best to remember them!", 200, 290);
        g.drawString("On your turn, you will draw from the deck. You may either:", 200, 310);
        g.drawString("     Discard it without effect.", 200, 330);
        g.drawString("     Replace one of your cards with the new one.", 200, 350);
        g.drawString("     Use the card if it's a special card. The special cards are:", 200, 370);
        g.drawString("             7 - allows you to peek at one of your cards.", 200, 390);
        g.drawString("             8 - allows you to look at one of your opponents cards.", 200, 410);
        g.drawString("             Q - allows you to look at one of your opponents cards and swap if you want.", 200, 430);
        g.drawString("             J - allows you to blindly swap with someone elses cards.", 200, 450);
        g.drawString("To end the game, type windows! All cards will be revealed and the player with the lowest point value wins!", 200, 470);
        repaint();
    }

    private void paintGame(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        // Draw Player's name and cards
        //g.drawString("Player: " + game.getPlayer().getName(), 20, 30);
        int xOffset = 200;
        int yOffset = 250;
        ImageIcon cardBack = new ImageIcon("Resources/Cards/back.png");
        Image backImage = cardBack.getImage();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int x = xOffset + j * (100 + 20); // Horizontal spacing between cards
                int y = yOffset + i * (150 + 20); // Vertical spacing between cards
                g.drawImage(backImage, x, y, 100, 150, null); // Draw each card
            }
        }
    }
}
