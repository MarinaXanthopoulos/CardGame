import java.util.ArrayList;

public class Player {
    // Instance variables
    private ArrayList<Card> hand;
    private int points;

    // Constructors
    public Player(String name) {
        points = 0;
    }

    public Player(String name, ArrayList<Card> hand){
        points = 0;
    }

    // Getters & Setters
    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getPoints() {
        return points;
    }

    // Take an int num points to add to players points
    public void addPoints(int numPoints){
        // Add new points to current points
        this.points += numPoints;
    }

    // Take in a card and add it to hand
    public void addCard(Card newCard){
        hand.add(newCard);
    }

    // toString
    @Override
    public String toString() {
        return "hi"; //name + " has " + getPoints() + " points " + names " cards: " + getHand;
    }
}
