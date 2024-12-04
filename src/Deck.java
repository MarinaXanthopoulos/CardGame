// Deck Class -Marina Xanthopoulos Windows Card Game
import java.util.ArrayList;

public class Deck {
    // Instance variables
    private ArrayList<Card> cards;
    private int cardsLeft;

    // Deck constructor
    public Deck (String[] ranks, String[] suits, int[] values) {
        cards = new ArrayList<>();

        // Create cards based on given arrays
        for(int i = 0; i < ranks.length; i++) {
            for (String suit : suits) {
                cards.add(new Card(ranks[i], suit, values[i]));
            }
        }

        // Initialize cardsleft and shuffle the deck
        cardsLeft = cards.size();
        shuffle();
    }

    // Getters & Setters
    public int getCardsLeft() {
        return cardsLeft;
    }

    public void setCardsLeft(int cardsLeft){
        this.cardsLeft = cardsLeft;
    }

    // Check if deck is now empty
    public boolean isEmpty(){
        return cardsLeft == 0;
    }

    // Shuffle the deck
    public void shuffle(){
        // For i = last index of deck down to 0
        for(int i = cards.size() - 1; i >= 0; i--){
            // Generate random int between - and i inclusive
            int random = (int)(Math.random()*i + 1);
            // Exchange cards at i and new random int
            Card copy = cards.get(i);
            cards.set(i, cards.get(random));
            cards.set(random, copy);
        }

        // Reset cardsLeft value to deck size
        cardsLeft = cards.size();
    }

    // Deal cards
    public Card deal(){
        // Return null if deck is empty
        if(isEmpty()){
            return null;
        }
        // Decrease cards left and return a card
        cardsLeft--;
        return cards.get(cardsLeft);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                ", cardsLeft=" + cardsLeft +
                '}';
    }
}
