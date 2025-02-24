import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardGameViewer extends JFrame {
    // Instance variables
    private Game game;
    private final int WINDOW_WIDTH = 1200;
    private final int WINDOW_HEIGHT = 900;
    private final int TITLE_BAR_HEIGHT = 23;
    // Variable to control what window is displayed
    // 0 = instructions, 1 = playing, 2 = final screen
    private int state;

    // Constructor
    public CardGameViewer(Game game){
        this.game = game;
        // Setup the window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Windows!");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        this.state = 0;
    }

    public void setState(int newState) {
        this.state = newState;
        // Ensure the screen updates when state changes
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (state == 0) {
            // Show instructions
            paintInstructions(g);
        }
        else if (state == 1) {
            // Main game view
            paintGame(g);
        }
        else if (state == 2) {
            // End-of-game screen
            paintEnd(g);
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
    private void paintGame(Graphics g) {
        // Retrieve each side’s hand
        ArrayList<Card> playerHand = game.getPerson().getHand();
        ArrayList<Card> computerHand = game.getComputer().getHand();

        // Indices of face-up cards
        ArrayList<Integer> revealedPlayer = game.getRevealedPlayerCards();
        ArrayList<Integer> revealedComputer = game.getRevealedComputerCards();

        // Coordinates for the player's 2x2 grid on the left
        //   Z(0)    Y(1)
        //   X(2)    W(3)
        int startXLeft = 150, startYLeft = 200;
        int spacingX = 130, spacingY = 160;

        // Draw the player's cards
        drawCardWithLabel(g, playerHand.get(0), "Z",
                startXLeft, startYLeft,
                revealedPlayer.contains(0));
        drawCardWithLabel(g, playerHand.get(1), "Y",
                startXLeft + spacingX, startYLeft,
                revealedPlayer.contains(1));
        drawCardWithLabel(g, playerHand.get(2), "X",
                startXLeft, startYLeft + spacingY,
                revealedPlayer.contains(2));
        drawCardWithLabel(g, playerHand.get(3), "W",
                startXLeft + spacingX, startYLeft + spacingY,
                revealedPlayer.contains(3));

        // Coordinates for the computer's 2x2 grid on the right
        //   V(0)    U(1)
        //   T(2)    S(3)
        int startXRight = 600, startYRight = 200;

        drawCardWithLabel(g, computerHand.get(0), "V",
                startXRight, startYRight,
                revealedComputer.contains(0));
        drawCardWithLabel(g, computerHand.get(1), "U",
                startXRight + spacingX, startYRight,
                revealedComputer.contains(1));
        drawCardWithLabel(g, computerHand.get(2), "T",
                startXRight, startYRight + spacingY,
                revealedComputer.contains(2));
        drawCardWithLabel(g, computerHand.get(3), "S",
                startXRight + spacingX, startYRight + spacingY,
                revealedComputer.contains(3));

        // Draw the discard and draw piles
        paintPiles(g);
    }

    private void paintEnd(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 26));
        g.drawString("Game Over!", 100, 100);

        // Show the final scores
        int personScore = game.getPerson().calculateScore();
        int compScore = game.getComputer().calculateScore();

        g.drawString(game.getPerson().getName() + "'s final score: " + personScore, 100, 150);
        g.drawString("Computer's final score: " + compScore, 100, 190);

        String winner;
        if (personScore < compScore) {
            winner = game.getPerson().getName() + " wins!";
        } else if (personScore > compScore) {
            winner = "Computer wins!";
        } else {
            winner = "It's a tie!";
        }
        g.drawString(winner, 100, 230);
    }

    /**
     * Draw the discard and draw piles.
     * We'll put the discard pile on top, the draw pile below it.
     */
    private void paintPiles(Graphics g) {
        // We'll put the discard pile up top, the draw pile below
        int discardX = 400, discardY = 220;
        int drawX = 400, drawY = discardY + 180; // 180 px below

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Discard Pile", discardX, discardY - 10);
        g.drawString("Draw Pile", drawX, drawY - 10);

        // 1) Discard Pile: top is discardPile[0]
        Card topDiscard = game.peekTopOfDiscard();
        if (topDiscard != null) {
            // Always face-up
            boolean orig = topDiscard.isFaceUp();
            topDiscard.setFaceUp(true);
            topDiscard.draw(g, discardX, discardY, this);
            topDiscard.setFaceUp(orig);
        } else {
            // placeholder if empty
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(discardX, discardY, 80, 120);
            g.setColor(Color.BLACK);
            g.drawRect(discardX, discardY, 80, 120);
        }

        // 2) Draw Pile
        boolean isDrawing = game.isCurrentlyDrawing();
        Card topDeckCard = game.peekTopOfDeck();
        if (isDrawing) {
            // show currentDrawCard face-up
            Card drawn = game.getCurrentDrawCard();
            if (drawn != null) {
                boolean orig = drawn.isFaceUp();
                drawn.setFaceUp(true);
                drawn.draw(g, drawX, drawY, this);
                drawn.setFaceUp(orig);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(drawX, drawY, 80, 120);
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, 80, 120);
            }
        } else {
            // not currently drawing => show top face-down if not empty
            if (topDeckCard != null) {
                ImageIcon backIcon = new ImageIcon("Resources/Cards/back.png");
                g.drawImage(backIcon.getImage(), drawX, drawY, 80, 120, this);
            } else {
                // deck is empty
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(drawX, drawY, 80, 120);
                g.setColor(Color.BLACK);
                g.drawRect(drawX, drawY, 80, 120);
            }
        }
    }


    /**
     * Helper method to draw a card and label above it.
     */
    private void drawCardWithLabel(Graphics g, Card card, String label, int x, int y, boolean faceUp) {
        boolean originalState = card.isFaceUp();
        card.setFaceUp(faceUp);

        card.draw(g, x, y, this);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString(label, x + 30, y - 5);

        // restore
        card.setFaceUp(originalState);
    }
}
