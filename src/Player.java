// Player Class -Marina Xanthopoulos Windows Card Game
import java.util.ArrayList;

public class Player {
    // Instance variables
    private String name;
    private ArrayList<Card> hand;
    private int points;

    // Constructors
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<Card>();
        points = 0;
    }

    // Getters & Setters
    public ArrayList<Card> getHand() { return hand; }
    public int getPoints() { return points; }
    public String getName() { return name; }


    // Take in a card and add it to hand
    public void addCard(Card newCard) {
        hand.add(newCard);
    }

    public int calculateScore() {
        int totalScore = 0;
        for (Card card : hand) {
            totalScore += card.getValue();
        }
        return totalScore;
    }

    // toString
    @Override
    public String toString() {
        return name + " has " + points + " points. Cards: " + hand;
    }
}
