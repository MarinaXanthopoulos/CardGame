// Game Class -Marina Xanthopoulos Windows Card Game
import java.util.Scanner;

public class Game {
    // Instance variables
    private CardGameViewer window;
    private static Deck deck;
    private Player person;
    private Player computer;
    private int state;

    // Make instance variabels for the deck attributes (final as they never change)
    private final String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private final String[] suits = {"spades", "hearts", "diamonds", "clubs"};
    // All cards have their numerical value except a = 0 and k = -1
    private final int[] values = {0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, -1};
    private Scanner input;

    // Creates the aspects for the game and prints instructions
    public Game() {
        state = 0;
        window = new CardGameViewer(this);
        window.repaint();
        // Print game instructions for player to know how to play!
        printInstructions();

        // Initialize scanner
        input = new Scanner(System.in);

        // Create a deck
        deck = new Deck(ranks, suits, values);

        // Ask player for name
        System.out.println("What's your name?");
        String answer = input.nextLine();

        // Create player
        person = new Player(answer);
        // Make computer
        computer = new Player("computer");
    }

    // Sets up game and runs the game (uses boolean value to keep game going until someone ends it)
    public void playGame(){
        gameSetUp();

        // Start game by having player take their turn
        boolean gameRunning = true;

        // Create a loop until game ends
        while (gameRunning) {
            // Have player take a turn
            System.out.print("Type your choice ('draw' or 'windows!'): ");
            String turnChoice = input.nextLine();

            // After a players turn let computer take their turn
            gameRunning = playerAction(turnChoice);
            if (gameRunning) {
                computerTurn();
            }
        }
    }

    // Deals cards and shows players their starting cards
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
        String choice1;
        String choice2;

        // Make sure it's a valid card to look at
        while(true) {
            System.out.print("Card: ");
            choice1 = input.nextLine().toUpperCase();
            if (getIndex(choice1) != -1) break;
            System.out.println("Invalid choice. Please choose a valid card (Z, Y, X, W).");
        }

        while(true) {
            System.out.print("Card: ");
            choice2 = input.nextLine().toUpperCase();
            if (!choice1.equals(choice2) && getIndex(choice2) != -1) break;
            if (choice1.equals(choice2)){
                System.out.println("You already chose that card. Choose a different one!");
            } else {
                System.out.println("Invalid choice. Please choose a valid card (Z, Y, X, W).");
            }
        }

        revealCards(choice1, choice2);
        // Pretend computer has looked at their own cards (they will use the values of these cards
        // later when they get special cards
        System.out.println("I looked at my V and U cards! Your turn to draw, type 'draw' to take your turn.");
    }

    // Shows cards given the String names
    private void revealCards(String choice1, String choice2){
        // Reveal the two cards at the chosen indexes
        int index1 = getIndex(choice1);
        int index2 = getIndex(choice2);

        // Make sure they're valid card index's
        if(index1 != -1 && index2 != -1){
            System.out.println(choice1 + " is "+ person.getHand().get(index1));
            System.out.println(choice2 + " is "+ person.getHand().get(index2));
        } else {
            System.out.println("Invalid card choices. No cards revealed.");
        }
    }

    // Player's turn controls
    private boolean playerAction(String choice) {
        switch (choice.toLowerCase()) {
            case "draw":
                // Draw a card
                Card drawn = deck.deal();
                System.out.println("You drew a " + drawn);

                // Check if it's a special card
                boolean isSpecial = isSpecial(drawn);
                if(isSpecial) {
                    // Ask player if they want to use the special card or replace it
                    System.out.print("Do you want to use this special card or replace one of your own with it? ");
                    String actionChoice = input.nextLine().toLowerCase();
                    if (actionChoice.equals("replace")) {
                        String replace = input.nextLine();
                        if (replace.equals("yes")) {
                            replaceCard(drawn);
                        } else {
                            System.out.println("Card discarded.");
                        }
                    } else if (actionChoice.equals("use")) {
                        specialCard(drawn);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                } else {
                    System.out.println("Do you want to replace one of your cards with this one?");
                    String replace = input.nextLine();
                    if (replace.equals("yes")) {
                        replaceCard(drawn);
                    } else {
                        System.out.println("Card discarded.");
                    }
                }
                return true;

            // End the game if the player types windows
            case "windows!":
                endGame();
                return false;

           // Say invalid choice if the player doesn't choose the above options
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    // Check if the card is a special card with diffent player turn options
    private boolean isSpecial(Card drawn) {
        if (drawn.getValue() == 8 || drawn.getValue() == 7 || drawn.getValue() == 11 || drawn.getValue() == 12){
            return true;
        } else {
            return false;
        }
    }

    // Replaces card of choice with the drawn card
    private void replaceCard(Card drawn) {
        System.out.println("Which card do you want to replace? (Z, Y, X, W) ");
        String replaceChoice = input.nextLine().toUpperCase();
        int replaceIndex = getIndex(replaceChoice);
        if (replaceIndex != -1) {
            System.out.println("Replacing " + replaceChoice + " with " + drawn);
            System.out.println("Discarded " + replaceChoice + ". It was a " + person.getHand().get(replaceIndex));
            person.getHand().set(replaceIndex, drawn);
        } else {
            System.out.println("Invalid card choice.");
        }
    }

    // Special card options if it was a special card
    private void specialCard(Card drawn) {
        switch (drawn.getValue()) {
            // 7 means look at your own card
            case 7:
                System.out.println("7 is a special card! You may look at one of your cards!");
                System.out.print("Which card do you want to look at? " );
                String lookChoice = input.nextLine();
                int lookIndex = getIndex(lookChoice);
                if (lookIndex != -1) {
                    System.out.println("Card " + lookChoice + " is " + person.getHand().get(lookIndex));
                } else {
                    System.out.println("Invalid card choice.");
                }
                break;

            // 8 means look at someone elses card
            case 8:
                System.out.println("8 is a special card! You may look at one of my cards!");
                System.out.print("Which card do you want to look at? ");
                String computerLookChoice = input.nextLine();
                int computerLookIndex = getIndex(computerLookChoice);
                if (computerLookIndex != -1) {
                    System.out.println("My card " + computerLookChoice + " is " +
                            computer.getHand().get(computerLookIndex));
                } else {
                    System.out.println("Invalid card choice.");
                }
                break;

            // J means blind swap
            case 11:
                System.out.println("J is a special card! You may blindly swap one of your cards with my cards.");
                System.out.print("Do you want to blindly swap with one of my cards? ");
                String blindSwap = input.nextLine();
                if (blindSwap.equals("yes")) {
                    System.out.println("Which of your cards do you want to blindly swap with mine?");
                    String bsPersonChoice = input.nextLine();
                    int bsPersonIndex = getIndex(bsPersonChoice);
                    System.out.println("Which of my cards do you want to blindly swap with?");
                    String bsChoice = input.nextLine();
                    int bsComputerIndex = getIndex(bsChoice);
                    if (bsComputerIndex != -1) {
                        Card personCard = person.getHand().get(bsPersonIndex);
                        Card computerCard = computer.getHand().get(bsComputerIndex);
                        person.getHand().set(bsPersonIndex, computerCard);
                        computer.getHand().set(bsComputerIndex, personCard);
                        System.out.println("Successfully swapped!");
                    }
                }

            // Q means look swap
            case 12:
                System.out.println("Q is a special card! You may look at one of your " +
                        "cards and swap it with mine if you want.");
                System.out.print("Which of your cards do you want to look at? (Z, Y, X, W) ");
                // Get the choice card they want to look at
                String personChoice = input.nextLine();
                // Set the int index to the String card name
                int personIndex = getIndex(personChoice);
                // Make sure it's a valid index and print out what the card there is
                if (personIndex != -1) {
                    System.out.println("Your card " + personChoice + " is " + person.getHand().get(personIndex));
                    System.out.println("Do you want to swap this card with an opponent's? (yes/no)");
                    String swapResponse = input.nextLine();
                    if (swapResponse.equalsIgnoreCase("yes")) {
                        System.out.println("Which opponent's card do you want to swap with? (V, U, T, S)");
                        String computerChoice = input.nextLine();
                        int computerIndex = getIndex(computerChoice);
                        if (computerIndex != -1) {
                            // Swap the cards
                            Card personCard = person.getHand().get(personIndex);
                            Card computerCard = computer.getHand().get(computerIndex);
                            person.getHand().set(personIndex, computerCard);
                            computer.getHand().set(computerIndex, personCard);
                            System.out.println("Succesfully swapped!");
                        } else {
                            System.out.println("Invalid opponent card choice. No swap.");
                        }
                    } else {
                        System.out.println("No swap performed.");
                    }
                } else {
                    System.out.println("Invalid card choice. No action taken.");
                }
                break;
        }
    }

    // See what the player does
    private boolean checkChoice(String choice) {
        if(choice.equals("draw")){
            Card drawn = deck.deal();
            return true;
        }
        else if(choice.equals("windows!")){
            endGame();
            return false;
        }
        else {
            System.out.println("Invalid choice.");
            return true;
        }
    }

    // Computer's simulated turn
    private void computerTurn() {
        System.out.println("My turn!");
        // Draw card
        Card drawn = deck.deal();

        if(isSpecial(drawn)) {
            switch (drawn.getValue()) {
                // 7 means look at your own card
                case 7:
                    System.out.println("I got a 7 and looked at one of my cards!");
                    break;

                // 8 means look at someone elses card
                case 8:
                    System.out.println("I got an 8 and looked at one of your cards!");
                    break;

                // J means blind swap
                case 11:
                    System.out.println("I got an 11 so am blind-swapping with one of your cards...");
                    int myIndex = (int) (Math.random() * 4);
                    int playerIndex = (int) (Math.random() * 4);
                    Card myCard = computer.getHand().get(myIndex);
                    Card playerCard = person.getHand().get(playerIndex);
                    computer.getHand().set(myIndex, playerCard);
                    person.getHand().set(playerIndex, myCard);
                    String myLetter;
                    if (myIndex == 0) {
                        myLetter = "V";
                    } else if (myIndex == 1) {
                        myLetter = "U";
                    } else if (myIndex == 2) {
                        myLetter = "T";
                    } else {
                        myLetter = "S";
                    }
                    String playerLetter;
                    if (playerIndex == 0) {
                        playerLetter = "Z";
                    } else if (playerIndex == 1) {
                        playerLetter = "Y";
                    } else if (playerIndex == 2) {
                        playerLetter = "X";
                    } else {
                        playerLetter = "W";
                    }
                    System.out.println("I blindly swapped my " + myLetter + " card with your " + playerLetter +
                            " card.");
                    break;

                // Q means look swap
                case 12:
                    int randomIndex = (int) (Math.random() * 4);
                    playerCard = person.getHand().get(randomIndex);

                    playerLetter = "";
                    if (randomIndex == 0) {
                        playerLetter = "Z";
                    } else if (randomIndex == 1) {
                        playerLetter = "Y";
                    } else if (randomIndex == 2) {
                        playerLetter = "X";
                    } else if (randomIndex == 3) {
                        playerLetter = "W";
                    }
                    int computerV = computer.getHand().get(0).getValue();
                    int computerU = computer.getHand().get(1).getValue();

                    myLetter = "";
                    Card cardToSwap = null;
                    int swapIndex = -1;
                    if (playerCard.getValue() < computerV) {
                        cardToSwap = computer.getHand().get(0);
                        swapIndex = 0;
                        myLetter = "V";
                    } else if (playerCard.getValue() < computerU) {
                        cardToSwap = computer.getHand().get(1);
                        swapIndex = 1;
                        myLetter = "U";
                    }
                    if (cardToSwap != null) {
                        System.out.println("I swapped your " + playerLetter + " card with my " + myLetter + " card.");
                        computer.getHand().set(swapIndex, playerCard);
                        person.getHand().set(randomIndex, cardToSwap);
                    } else {
                        System.out.println("I drew a Q but decided not to swap with your card.");
                    }
                    break;
            }
        } else {
            int computerV = computer.getHand().get(0).getValue();
            int computerU = computer.getHand().get(1).getValue();
            if (drawn.getValue() < computerV){
                computer.getHand().set(0, drawn);
                System.out.println("I replaced my V card with the drawn card.");
                System.out.println("Discarded V card, it was a " + computer.getHand().get(0));
            } else if (drawn.getValue() < computerU) {
                computer.getHand().set(1, drawn);
                System.out.println("I replaced my U card with the drawn card.");
                System.out.println("Discarded U card, it was a " + computer.getHand().get(1));
            } else {
                System.out.println("Discarding drawn card, it was a " + drawn);
            }
        }
    }

    // End game
    private void endGame() {
        System.out.println("Game Over!");
        System.out.println(person.getName() + "'s cards: " + person.getHand());
        System.out.println("My cards: " + computer.getHand());
        int personScore = person.calculateScore();
        int computerScore = computer.calculateScore();
        System.out.println(person.getName() + "'s score is " + personScore);
        System.out.println("My score is " + computerScore);

        if (personScore < computerScore) {
            System.out.println(person.getName() + " wins!");
        } else if (personScore > computerScore) {
            System.out.println("I win!");
        } else {
            System.out.println("We tied!");
        }
    }

    // Get the index given the String card name
    private int getIndex(String choice) {
        if(choice.equals("Z") || choice.equals("V")){
            return 0;
        }
        else if (choice.equals("Y") || choice.equals("U")){
            return 1;
        }
        else if (choice.equals("X") || choice.equals("T")){
            return 2;
        }
        else if (choice.equals("W") || choice.equals("S")){
            return 3;
        }
        else{
            System.out.println("Invalid card choice.");
            return -1;
        }
    }

    // Instructions to play
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
        System.out.println("To end the game, type windows! All cards will be revealed and " +
                " the player with the lowest point value wins!");
    }

    public static void main(String[] args){
        Game windows = new Game();
        windows.playGame();
    }
}
