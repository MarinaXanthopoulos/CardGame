import java.util.Scanner;

public class Game {
    // Instance variables
    private static Deck deck;
    private Player person;
    private Player computer;
    // Make instance variabels for the deck attributes (final as they never change)
    private final String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private final String[] suits = {"hearts", "spades", "clubs", "diamonds"};
    // All cards have their numerical value except a = 0 and k = -1
    private final int[] values = {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -1};
    private Scanner input;

    public Game() {
        // printInstructions();

        // Initialize scanner
        input = new Scanner(System.in);

        // Create a deck
        deck = new Deck(ranks, suits, values);

        // Ask player for name
        System.out.println("What's your name?");
        Scanner input = new Scanner(System.in);
        String answer = input.nextLine();

        // Create player
        person = new Player(answer);
        // Make computer
        computer = new Player("computer");
    }

    public static void printInstructions(){
        System.out.println("Welcome to the game of Windows!");
        System.out.println("You will be delt 4 cards (no peeking!). Your goal is to get the lowest point value.");
        System.out.println("A is worth 0, 2-10 are worth their normal values, and j = 11, Q = 12, K = -1");
        System.out.println("Your cards are arranged in a window formation: ");
        System.out.println("                Z   Y");
        System.out.println("                X   W");
        System.out.println("To begin, you can only look at two of your cards, try your best to remember them!");
        System.out.println("On your turn, you will draw from the deck. You may either:");
        System.out.println("     Discard it without effect.");
        System.out.println("     Replace one of your cards with the new one.");
        System.out.println("     Use the card if it's a special card. The special cards are:");
        System.out.println("             7 - allows you to peek at one of your cards.");
        System.out.println("             8 - allows you to look at one of your opponents cards.");
        System.out.println("             Q - allows you to look at one of your opponents cards and swap if you wnat.");
        System.out.println("             J - allows you to blindly swap with someone elses cards.");
        System.out.println("To end the game, type windows! All cards will be revealed and the player with the lowest point value wins!");
    }

    public void playGame(){
        gameSetUp();
    }

    private void gameSetUp() {
        // Deal 4 cards to the player and computer
        for (int i = 0; i < 4; i ++){
            person.addCard(deck.deal());
            computer.addCard(deck.deal());
        }

        // Display players and computers cards face down
        System.out.println("Hi " + person.getName() + "!");
        System.out.println("Here are your 4 cards:          And my 4 cards:");
        System.out.println("      Z    Y                       V    U");
        System.out.println("      X    W                       T    S");

        // Let player choose two cards to look at
        System.out.println("Which of your two cards do you want to see?");
        System.out.print("Card: ");
        String index1 = input.nextLine();
        System.out.print("Card: ");
        String index2 = input.nextLine();

    }

    public static void main(String[] args){
        Game windows = new Game();
        windows.playGame();
    }
}
