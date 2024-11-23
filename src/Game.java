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
        // Print game instructions for player to know how to play! printInstructions();

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

    public void playGame(){
        gameSetUp();

        // Start game by having player take their turn
        String choice = input.nextLine();
        checkChoice(choice);

        // playerTakeTurn();
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
        // Card 1 is z and v, card 2 is y and u, card 3 is x and t, card 4 is w and s
        // Note: the card's aren't displayed as card 1,2,3,4 to not confuse them with their values
        System.out.println("      Z    Y                       V    U");
        System.out.println("      X    W                       T    S");

        // Let player choose two cards to look at
        System.out.println("Which of your two cards do you want to see?");
        System.out.print("Card: ");
        String choice1 = input.nextLine();
        System.out.print("Card: ");
        String choice2 = input.nextLine();

        // Reveal the two cards at the chosen indexes
        int index1 = 0;
        if(choice1.equals("Z")){
            index1 = 0;
        }
        else if (choice1.equals("Y")){
            index1 = 1;
        }
        else if (choice1.equals("X")){
            index1 = 2;
        }
        else if (choice1.equals("W")){
            index1 = 3;
        }
        else{
            System.out.println("Invalid card choice. Please choose form your cards:  Z    Y");
            System.out.println("                                                     X    W");
        }

        int index2 = 0;
        if(choice2.equals("Z")){
            index2 = 0;
        }
        else if (choice2.equals("Y")){
            index2 = 1;
        }
        else if (choice2.equals("X")){
            index2 = 2;
        }
        else if (choice2.equals("W")){
            index2 = 3;
        }
        else{
            System.out.println("Invalid card choice. Please choose form your cards:  Z    Y");
            System.out.println("                                                     X    W");
        }
        System.out.println(choice1 + " is "+ person.getHand().get(index1));
        System.out.println(choice2 + " is "+ person.getHand().get(index2));

        // Pretend computer has looked at their own cards
        System.out.println("I looked at two of mine already! Your turn to draw, type 'draw' to take your turn.");
    }

    private void checkChoice(String choice) {
        if(choice.equals("draw")){
            //person.drawCard();
        }
        else if(choice.equals("windows!")){
            endGame();
        }
    }

    private void endGame() {
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

    public static void main(String[] args){
        Game windows = new Game();
        windows.playGame();
    }
}
