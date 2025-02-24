// Card Class -Marina Xanthopoulos Windows Card Game
import javax.swing.*;
import java.awt.*;

public class Card {
    // Instance variables, attributes of a card
    private String rank;
    private String suit;
    private int value;
    private Image image;
    private boolean isFaceUp;

    // Constructor
    public Card(String rank, String suit, int value, ImageIcon imageIcon) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
        this.image = imageIcon.getImage();
        this.isFaceUp = true;
    }

    // Getters & Setters
    public String getRank() { return rank; }
    public String getSuit() { return suit; }
    public int getValue() { return value; }
    public boolean isFaceUp() { return isFaceUp; }
    public void setFaceUp(boolean faceUp) { this.isFaceUp = faceUp; }

    public void draw(Graphics g, int x, int y, CardGameViewer viewer) {
        int cardWidth = 80;
        int cardHeight = 120;

        if (isFaceUp) {
            g.drawImage(image, x, y, cardWidth, cardHeight, viewer);
        } else {
            ImageIcon cardBack = new ImageIcon("Resources/Cards/back.png");
            g.drawImage(cardBack.getImage(), x, y, cardWidth, cardHeight, viewer);
        }
    }

    // toString -print the rank NOT the suit
    @Override
    public String toString() {
        return rank;
    }
}
