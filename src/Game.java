import java.util.Scanner;

public class Game {
    // Instance variables
    private Deck decks;
    private Player players;

    public Game(Deck deck, Player person, Player computer) {
        // Declare deck attributes and make a deck
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        // All cards have their numerical value except a = 0 and k = -1
        int[] values = {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -1};
        String[] suits = {"hearts", "spades", "clubs", "diamonds"};
        deck = new Deck(ranks, suits, values);

        // Ask player for name
        System.out.println("What's your name?");
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();

        // Create player
        person = new Player(answer);
        // Make computer
        computer = new Player("computer");

        // Deal 4 cards to the player and computer
        person.addCard();
    }

    public static void printInstructions(){
        System.out.println("Welcome to the game of Windows!");
        System.out.println("You will be delt 4 cards (no peeking!) and...");
    }

    public static void playGame(){
        gameSetUp();
    }

    private static void gameSetUp() {
        // Print game instructions
        printInstructions();
    }

    public static void main(String[] args){
        playGame();
    }
}
