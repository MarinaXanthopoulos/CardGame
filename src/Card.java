// Card Class -Marina Xanthopoulos Windows Card Game

import javax.swing.*;
import java.awt.*;

public class Card {
    // Instance variables, attributes of a card
    private String rank;
    private String suit;
    private int value;
    private Image image;

    // Constructor
    public Card(String rank, String suit, int value, ImageIcon imageIcon) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    // Getters & Setters
    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // toString -print the rank NOT the suit
    @Override
    public String toString() {
        return rank;
    }
}
