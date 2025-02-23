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
        this.state = 1;
    }

    public void setState(int newState) {
        this.state = newState;
        repaint();  // Ensure the screen updates when state changes
    }


    public void paint(Graphics g) {
        super.paint(g);
        if (state == 1) {
            paintGame(g);
        }
    }

    private void paintGame(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        ArrayList<Card> playerHand = game.getPerson().getHand();
        ArrayList<Card> computerHand = game.getComputer().getHand();

        int xLeft = 200, yLeft = 200, spacingX = 100, spacingY = 150;
        int row = 0, col = 0;

        for (Card card : playerHand) {
            card.draw(g, xLeft + (col * spacingX), yLeft + (row * spacingY), this);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        int xRight = 500, yRight = 200;
        row = 0;
        col = 0;
        for (Card card : computerHand) {
            card.draw(g, xRight + (col * spacingX), yRight + (row * spacingY), this);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
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


    public void draw(Graphics g, int x, int y, CardGameViewer viewer) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, 80, 120); // Placeholder card shape
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 80, 120);
    }
}
