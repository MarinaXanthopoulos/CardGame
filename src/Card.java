// Card Class -Marina Xanthopoulos Windows Card Game

public class Card {
    // Instance variables, attributes of a card
    private String rank;
    private String suit;
    private int value;

    // Constructor
    public Card(String rank, String suit, int value) {
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
