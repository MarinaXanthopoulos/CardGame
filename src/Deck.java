// Deck Class -Marina Xanthopoulos Windows Card Game
import javax.swing.*;
import java.util.ArrayList;

public class Deck {
    // Instance variables
    private ArrayList<Card> cards;
    private int cardsLeft;

    // Deck constructor
    public Deck(String[] ranks, String[] suits, int[] values) {
        cards = new ArrayList<>();

        // Make deck
        for (int i = 0; i < ranks.length; i++) {
            for (int s = 0; s < suits.length; s++) {
                // Calculate the file index from i*4 + s
                int fileIndex = i * suits.length + s;
                String filename = "Resources/Cards/" + fileIndex + ".png";

                // Build the card
                Card c = new Card(ranks[i], suits[s], values[i], new ImageIcon(filename));
                cards.add(c);
            }
        }

        // Initialize cardsleft and shuffle the deck
        cardsLeft = cards.size();
        shuffle();
    }

    // Getters & Setters
    public int getCardsLeft() { return cardsLeft; }
    public void setCardsLeft(int cardsLeft){ this.cardsLeft = cardsLeft; }

    // Check if deck is now empty
    public boolean isEmpty(){ return cardsLeft == 0; }

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

    // Peek the top card of the deck without dealing it
    public Card peekTop() {
        if (isEmpty()) {
            return null; // or throw an exception if you prefer
        }
        return cards.get(cardsLeft - 1);
    }


    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                ", cardsLeft=" + cardsLeft +
                '}';
    }
}
