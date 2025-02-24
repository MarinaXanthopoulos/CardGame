// CardGameViewer Class -Marina Xanthopoulos Windows Card Game
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardGameViewer extends JFrame {
    // Instance variables
    private Game game;
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 700;
    private final int TITLE_BAR_HEIGHT = 23;
    // Variable to control what window is displayed ==> 0 = instructions, 1 = playing, 2 = final screen
    private int state;

    // Store background images in an arraylist
    private Image[] backgrounds;

    // Constructor
    public CardGameViewer(Game game){
        this.game = game;
        // Setup the window
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Windows!");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setVisible(true);
        this.state = 0;

        // Load backgrounds
        backgrounds = new Image[3];
        backgrounds[0] = new ImageIcon("Resources/Backgrounds/0.png").getImage();
        backgrounds[1] = new ImageIcon("Resources/Backgrounds/1.png").getImage();
        backgrounds[2] = new ImageIcon("Resources/Backgrounds/2.png").getImage();
    }

    public void setState(int newState) {
        this.state = newState;
        // Ensure the screen updates when state changes
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);

        if (state >= 0 && state < backgrounds.length) {
            // Scale it to fit the window, or just draw at top-left
            g.drawImage(backgrounds[state], 0, 23, WINDOW_WIDTH, WINDOW_HEIGHT, this);
        }

        // Call certain windows to draw based on what part of the game we're in
        if (state == 0) {
            paintInstructions(g);
        }
        else if (state == 1) {
            paintGame(g);
        }
        else if (state == 2) {
            paintEnd(g);
        }
    }

    // Display Instructions
    private void paintInstructions(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 16));
        g.drawString("Welcome to the Windows Card Game!", WINDOW_HEIGHT / 2, 75);

        // Display Instructions
        g.setFont(new Font("Serif", Font.PLAIN, 14));
        g.drawString("Instructions:", 250, 150);

        g.drawString("You will be delt 4 cards (no peeking!). Your goal is to get the lowest point value.", 250, 190);
        g.drawString("A is worth 0, 2-10 are worth their normal values, and j = 11, Q = 12, K = -1", 250, 210);
        g.drawString("Your cards are arranged in a window formation: ", 250, 230);
        g.drawString("                Z   Y", 250, 250);
        g.drawString("                X   W", 250, 270);
        g.drawString("To begin, you can only look at two of your cards, try your best to remember them!", 250, 290);
        g.drawString("On your turn, you will draw from the deck. You may either:", 250, 310);
        g.drawString("     Discard it without effect.", 250, 330);
        g.drawString("     Replace one of your cards with the new one.", 250, 350);
        g.drawString("     Use the card if it's a special card. The special cards are:", 250, 370);
        g.drawString("             7 - allows you to peek at one of your cards.", 250, 390);
        g.drawString("             8 - allows you to look at one of your opponents cards.", 250, 410);
        g.drawString("             Q - allows you to look at one of your opponents cards and swap if you want.", 250, 430);
        g.drawString("             J - allows you to blindly swap with someone elses cards.", 250, 450);
        g.drawString("To end the game, type windows!", 250, 470);
        g.drawString("All cards will be revealed and the player with the lowest point value wins!", 250, 490);
    }

    // Show cards
    private void paintGame(Graphics g) {
        // Player's 4 cards on left
        ArrayList<Card> pHand = game.getPerson().getHand();
        ArrayList<Integer> rP = game.getRevealedPlayerCards();

        // Computer's 4 cards on right
        ArrayList<Card> cHand = game.getComputer().getHand();
        ArrayList<Integer> rC = game.getRevealedComputerCards();

        // Draw player's 2x2
        int startXLeft = 150, startYLeft = 200;
        int spacingX = 130, spacingY = 160;

        // Z(0), Y(1) top row
        drawCardWithLabel(g, pHand.get(0), "Z", startXLeft, startYLeft, rP.contains(0));
        drawCardWithLabel(g, pHand.get(1), "Y", startXLeft + spacingX, startYLeft, rP.contains(1));
        // X(2), W(3) bottom row
        drawCardWithLabel(g, pHand.get(2), "X", startXLeft, startYLeft + spacingY, rP.contains(2));
        drawCardWithLabel(g, pHand.get(3), "W", startXLeft + spacingX, startYLeft + spacingY, rP.contains(3));

        // Draw computer's 2x2
        int startXRight = 600, startYRight = 200;
        drawCardWithLabel(g, cHand.get(0), "V", startXRight, startYRight, rC.contains(0));
        drawCardWithLabel(g, cHand.get(1), "U", startXRight + spacingX, startYRight, rC.contains(1));
        drawCardWithLabel(g, cHand.get(2), "T", startXRight, startYRight + spacingY, rC.contains(2));
        drawCardWithLabel(g, cHand.get(3), "S", startXRight + spacingX, startYRight + spacingY, rC.contains(3));

        // Draw discard and draw piles
        paintPiles(g);
    }

    // Display discarded and drawn cards
    private void paintPiles(Graphics g) {
        // Centering Variables
        int cardWidth = 80;
        int cardHeight = 120;
        int centerX = (WINDOW_WIDTH - cardWidth) / 2;
        int discardY = 150;
        int drawY    = discardY + 180;

        // Labels
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 16));
        g.drawString("Discard Pile", centerX, discardY - 10);
        g.drawString("Draw Pile",    centerX, drawY    - 10);

        // Discard pile: face-up if not empty
        Card topDiscard = game.peekTopOfDiscard();
        if (topDiscard != null) {
            boolean orig = topDiscard.isFaceUp();
            topDiscard.setFaceUp(true);
            topDiscard.draw(g, centerX, discardY, this);
            topDiscard.setFaceUp(orig);
        } else {
            // Empty discard
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(centerX, discardY, cardWidth, cardHeight);
            g.setColor(Color.BLACK);
            g.drawRect(centerX, discardY, cardWidth, cardHeight);
        }

        // Draw pile
        boolean isDrawing = game.isCurrentlyDrawing();
        Card topDeckCard = game.peekTopOfDeck();
        if (isDrawing) {
            // Show currentDrawCard face-up
            Card drawn = game.getCurrentDrawCard();
            if (drawn != null) {
                boolean orig = drawn.isFaceUp();
                drawn.setFaceUp(true);
                drawn.draw(g, centerX, drawY, this);
                drawn.setFaceUp(orig);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(centerX, drawY, cardWidth, cardHeight);
                g.setColor(Color.BLACK);
                g.drawRect(centerX, drawY, cardWidth, cardHeight);
            }
        } else {
            // Not currently drawing => show top card face-down if any
            if (topDeckCard != null) {
                ImageIcon back = new ImageIcon("Resources/Cards/back.png");
                g.drawImage(back.getImage(), centerX, drawY, cardWidth, cardHeight, this);
            } else {
                // Deck empty
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(centerX, drawY, cardWidth, cardHeight);
                g.setColor(Color.BLACK);
                g.drawRect(centerX, drawY, cardWidth, cardHeight);
            }
        }
    }

    // Show all cards and scores at the end!
    private void paintEnd(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 26));
        g.drawString("Game Over!", 100, 100);

        // Flip everyone's cards
        ArrayList<Card> playerHand = game.getPerson().getHand();
        ArrayList<Card> computerHand = game.getComputer().getHand();

        // Player’s Z(0), Y(1), X(2), W(3) - all face-up
        int startXLeft = 150, startYLeft = 200;
        int spacingX = 130, spacingY = 160;
        drawCardWithLabel(g, playerHand.get(0), "Z", startXLeft, startYLeft, true);
        drawCardWithLabel(g, playerHand.get(1), "Y", startXLeft + spacingX, startYLeft, true);
        drawCardWithLabel(g, playerHand.get(2), "X", startXLeft, startYLeft + spacingY, true);
        drawCardWithLabel(g, playerHand.get(3), "W", startXLeft + spacingX, startYLeft + spacingY, true);

        // Computer’s V(0), U(1), T(2), S(3) - all face-up
        int startXRight = 600, startYRight = 200;
        drawCardWithLabel(g, computerHand.get(0), "V", startXRight, startYRight,true);
        drawCardWithLabel(g, computerHand.get(1), "U",startXRight + spacingX, startYRight,true);
        drawCardWithLabel(g, computerHand.get(2), "T", startXRight, startYRight + spacingY,true);
        drawCardWithLabel(g, computerHand.get(3), "S",startXRight + spacingX, startYRight + spacingY,true);

        // Calculate and get scores
        int personScore = game.getPerson().calculateScore();
        int compScore = game.getComputer().calculateScore();
        g.drawString(game.getPerson().getName() + "r final score: " + personScore, 200, 530);
        g.drawString("Computer's final score: " + compScore, 600, 530);

        // Show winner
        String winner;
        if (personScore < compScore) winner = "You win!";
        else if (personScore > compScore) winner = "Computer wins!";
        else winner = "Tie!";
        g.drawString(winner, 100, 120);
    }

    // Label the cards on the screen
    private void drawCardWithLabel(Graphics g, Card card, String label, int x, int y, boolean faceUp) {
        boolean originalFace = card.isFaceUp();
        card.setFaceUp(faceUp);
        card.draw(g, x, y, this);
        card.setFaceUp(originalFace);

        // Label
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 16));
        g.drawString(label, x+30, y-5);
    }
}