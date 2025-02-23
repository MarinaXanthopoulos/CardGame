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
        this.isFaceUp = false;
    }

    public void flip() {
        this.isFaceUp = !this.isFaceUp;
    }

    // Getters & Setters
    public String getRank() { return rank; }
    public String getSuit() { return suit; }
    public int getValue() { return value; }
    public boolean isFaceUp() { return isFaceUp; }

    public void draw(Graphics g, int x, int y, CardGameViewer viewer) {
        if (isFaceUp) {
            g.setColor(Color.WHITE);
            g.fillRect(x, y, 80, 120);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 80, 120);
            g.drawString(rank + " of " + suit, x + 10, y + 60);
        } else {
            ImageIcon cardBack = new ImageIcon("Resources/Cards/back.png");
            g.drawImage(cardBack.getImage(), x, y, 80, 120, viewer);
        }
    }

    // toString -print the rank NOT the suit
    @Override
    public String toString() {
        return rank;
    }
}
