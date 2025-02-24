import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private CardGameViewer window;
    private static Deck deck;
    private Player person;
    private Player computer;

    // Keep track of which cards are face-up
    private ArrayList<Integer> revealedPlayerCards;
    private ArrayList<Integer> revealedComputerCards;

    // Discard pile (top = last index)
    private ArrayList<Card> discardPile;
    // The card currently drawn but not decided upon
    private Card currentDrawCard;
    private boolean currentlyDrawing;

    // The state for the viewer: 0=instructions, 1=playing, 2=end
    private int state;

    // For convenience, we store the deck attributes again:
    private final String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    private final String[] suits = {"spades","hearts","diamonds","clubs"};
    private final int[] values = {0,2,3,4,5,6,7,8,9,10,11,12,-1};

    private Scanner input;

    public Game() {
        // Create deck, players, etc.
        deck = new Deck(ranks, suits, values);
        person = new Player("Player");
        computer = new Player("Computer");
        input = new Scanner(System.in);

        // Manage revealed cards
        revealedPlayerCards = new ArrayList<>();
        revealedComputerCards = new ArrayList<>();

        // Piles
        discardPile = new ArrayList<>();
        currentDrawCard = null;
        currentlyDrawing = false;

        // Create the GUI
        window = new CardGameViewer(this);
        // Start with instructions
        state = 0;
        window.setState(state);

        // Deal initial cards
        dealInitialCards();
    }

    // For the viewer to read
    public ArrayList<Integer> getRevealedPlayerCards() { return revealedPlayerCards; }
    public ArrayList<Integer> getRevealedComputerCards() { return revealedComputerCards; }

    public Player getPerson() { return person; }
    public Player getComputer() { return computer; }


    // The deck is effectively our “draw pile”
    public Card peekTopOfDeck() {
        if (deck.isEmpty()) return null;
        return deck.peekTop();
    }

    // The top of the discard pile is discardPile.get(0), if not empty
    public Card peekTopOfDiscard() {
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.get(0);
    }

    public boolean isCurrentlyDrawing() {
        return currentlyDrawing;
    }
    public Card getCurrentDrawCard() {
        return currentDrawCard;
    }

    // Deal 4 cards to each
    public void dealInitialCards() {
        for (int i = 0; i < 4; i++) {
            person.addCard(deck.deal());
            computer.addCard(deck.deal());
        }
    }

    // Main game flow
    public void playGame() {
        // Show the instructions in the GUI (state=0), also do console instructions if desired
        System.out.println("Welcome to Windows Card Game. Press Enter to proceed...");
        input.nextLine();

        // Move to state=1 (playing)
        state = 1;
        window.setState(state);

        // Let the user pick 2 cards to see
        chooseTwoCardsToPeek();

        boolean gameRunning = true;
        while (gameRunning) {
            System.out.println("Your turn! Type 'draw' or 'windows!':");
            String turnChoice = input.nextLine();
            gameRunning = playerAction(turnChoice);
            // If still running, do computer turn, etc.
            if (gameRunning) {
                computerTurn();
            }
        }
    }

    // Let the user pick 2 cards (Z, Y, X, W => indexes 0..3)
    private void chooseTwoCardsToPeek() {
        System.out.println("Which two of your cards do you want to see? (Z, Y, X, W)");
        String choice1, choice2;
        choice1 = getValidCardChoice();
        choice2 = getValidCardChoiceDifferent(choice1);

        // Convert card letters to indexes
        int idx1 = getIndex(choice1);
        int idx2 = getIndex(choice2);

        // Reveal them
        revealedPlayerCards.add(idx1);
        revealedPlayerCards.add(idx2);
        window.repaint();

        // Show them in console
        System.out.println(choice1 + " is " + person.getHand().get(idx1));
        System.out.println(choice2 + " is " + person.getHand().get(idx2));

        // Flip them back down after user presses Enter
        System.out.println("Press Enter when done viewing those two...");
        input.nextLine();
        revealedPlayerCards.remove((Integer)idx1);
        revealedPlayerCards.remove((Integer)idx2);
        window.repaint();
    }

    // Returns a valid card letter from Z,Y,X,W
    private String getValidCardChoice() {
        while (true) {
            String c = input.nextLine().toUpperCase();
            if (getIndex(c) != -1) {
                return c;
            }
            System.out.println("Invalid choice. Please pick Z,Y,X,W.");
        }
    }

    private String getValidCardChoiceDifferent(String alreadyChosen) {
        while(true) {
            String c = input.nextLine().toUpperCase();
            if (!c.equals(alreadyChosen) && getIndex(c) != -1) {
                return c;
            }
            System.out.println("Invalid choice or already chosen. Try again:");
        }
    }

    // Player's turn
    private boolean playerAction(String choice) {
        switch (choice.toLowerCase()) {
            case "draw":
                drawCardLogic();
                return true;
            case "windows!":
                endGame();
                return false;
            default:
                System.out.println("Invalid choice.");
                return true;
        }
    }

    // Draw logic: show the top card face-up, let user keep or discard
    private void drawCardLogic() {
        if (deck.isEmpty()) {
            System.out.println("No more cards to draw! Ending game...");
            endGame();
            return;
        }

        currentlyDrawing = true;
        currentDrawCard = deck.deal(); // show face-up
        window.repaint();

        System.out.println("You drew a " + currentDrawCard);

        if (isSpecial(currentDrawCard)) {
            System.out.println("This is a special card ("+ currentDrawCard +").");
            System.out.println("Type 'use' to use it, 'replace' to add it to your hand, or 'discard' to discard.");

            String actionChoice = input.nextLine().toLowerCase();
            if (actionChoice.equals("use")) {
                // Use the special effect
                specialCard(currentDrawCard);

                // After using, ask user if they want to keep it or discard
                System.out.println("You've used " + currentDrawCard + ".");
                System.out.println("Do you want to 'keep' (replace a card in your hand) or 'discard' it?");
                String postUse = input.nextLine().toLowerCase();
                if (postUse.equals("keep")) {
                    replaceCard(currentDrawCard);
                } else {
                    discardCard(currentDrawCard);
                }
            }
            else if (actionChoice.equals("replace")) {
                replaceCard(currentDrawCard);
            }
            else {
                // Discard
                discardCard(currentDrawCard);
            }
        }
        else {
            // Normal card
            System.out.println("Do you want to replace one of your cards with this one? (yes = replace / no = discard)");
            String ans = input.nextLine().toLowerCase();
            if (ans.equals("yes")) {
                replaceCard(currentDrawCard);
            } else {
                discardCard(currentDrawCard);
            }
        }

        // Done deciding
        currentDrawCard = null;
        currentlyDrawing = false;
        window.repaint();
    }

    // Discard puts new top at index 0
    private void discardCard(Card c) {
        if (c != null) {
            // Insert at front so it's the top
            discardPile.add(0, c);
        }
        System.out.println("Discarded " + c);
    }

    // Replace a chosen card with 'c' and discard the old one
    private void replaceCard(Card c) {
        System.out.println("Which card (Z,Y,X,W) do you want to replace?");
        String choice = input.nextLine().toUpperCase();
        int idx = getIndex(choice);
        if (idx == -1) {
            System.out.println("Invalid choice, discarding new card instead.");
            discardCard(c);
            return;
        }
        Card old = person.getHand().get(idx);
        person.getHand().set(idx, c);
        discardCard(old);
        System.out.println("You replaced " + choice + " (which was " + old + ") with " + c);
    }

    // 7, 8, J=11, Q=12
    private boolean isSpecial(Card c) {
        int v = c.getValue();
        return (v == 7 || v == 8 || v == 11 || v == 12);
    }

    // Evaluate the special effect
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


    // Computer’s turn
    private void computerTurn() {
        System.out.println("Computer's turn...");
        if (deck.isEmpty()) {
            endGame();
            return;
        }
        Card c = deck.deal();
        System.out.println("Computer drew a hidden card.");

        // Simple logic: always discard. You could do something more advanced
        if (isSpecial(c)) {
            // Possibly do special effect or not
            System.out.println("Computer discards the special card: " + c);
            discardCard(c);
        } else {
            // Discard or keep if it's better
            // We'll just discard to keep it simple
            System.out.println("Computer discards: " + c);
            discardCard(c);
        }
    }

    // End the game
    private void endGame() {
        System.out.println("Game Over!");
        revealedPlayerCards.clear();
        revealedComputerCards.clear();
        // reveal all 4
        revealedPlayerCards.add(0);revealedPlayerCards.add(1);
        revealedPlayerCards.add(2);revealedPlayerCards.add(3);

        revealedComputerCards.add(0);revealedComputerCards.add(1);
        revealedComputerCards.add(2);revealedComputerCards.add(3);

        state = 2;
        window.setState(state);

        int pScore = person.calculateScore();
        int cScore = computer.calculateScore();
        System.out.println("Your final hand: " + person.getHand());
        System.out.println("Computer final hand: " + computer.getHand());
        System.out.println("Your score: " + pScore);
        System.out.println("Computer score: " + cScore);
        if (pScore < cScore) {
            System.out.println("You win!");
        } else if (pScore > cScore) {
            System.out.println("Computer wins!");
        } else {
            System.out.println("Tie!");
        }
    }

    // Convert from letter (Z,Y,X,W or V,U,T,S) to index
    private int getIndex(String choice) {
        switch(choice) {
            case "Z": case "V": return 0;
            case "Y": case "U": return 1;
            case "X": case "T": return 2;
            case "W": case "S": return 3;
            default: return -1;
        }
    }

    public static void main(String[] args){
        Game g = new Game();
        g.playGame();
    }
}