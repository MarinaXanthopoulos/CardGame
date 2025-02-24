// Game Class -Marina Xanthopoulos Windows Card Game
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    // Instance Variables
    private CardGameViewer window;
    private static Deck deck;
    private Player person;
    private Player computer;
    private ArrayList<Integer> revealedPlayerCards;
    private ArrayList<Integer> revealedComputerCards;
    private ArrayList<Card> discardPile;
    private Card currentDrawCard;
    private boolean currentlyDrawing;
    private Scanner input;
    // 0=instructions,1=playing,2=end
    private int state;

    // Deck stuff
    private String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    private String[] suits = {"spades","hearts","diamonds","clubs"};
    private int[] values = {0,2,3,4,5,6,7,8,9,10,11,12,-1};

    // Constructor
    public Game() {
        window = new CardGameViewer(this);
        state = 0;
        window.setState(state);

        deck = new Deck(ranks, suits, values);
        person = new Player("You");
        computer = new Player("Computer");
        input = new Scanner(System.in);

        revealedPlayerCards = new ArrayList<>();
        revealedComputerCards = new ArrayList<>();

        discardPile = new ArrayList<>();
        currentDrawCard = null;
        currentlyDrawing = false;

        dealInitialCards();
    }

    // Getters n' setters
    public ArrayList<Integer> getRevealedPlayerCards() { return revealedPlayerCards; }
    public ArrayList<Integer> getRevealedComputerCards() { return revealedComputerCards; }
    public Player getPerson() { return person; }
    public Player getComputer() { return computer; }
    public Card getCurrentDrawCard() { return currentDrawCard; }

    // Looking functions
    public Card peekTopOfDeck() {
        if (deck.isEmpty()) return null;
        return deck.peekTop();
    }
    public Card peekTopOfDiscard() {
        if(discardPile.isEmpty()) return null;
        return discardPile.get(0);
    }
    public boolean isCurrentlyDrawing() {
        return currentlyDrawing;
    }

    // Deals cards to players and fake computer dude
    public void dealInitialCards() {
        for(int i=0; i<4; i++){
            person.addCard(deck.deal());
            computer.addCard(deck.deal());
        }
    }

    // Game logic!
    public void playGame() {
        System.out.println("Welcome to Windows Card Game! Press Enter to proceed...");
        input.nextLine();

        // Move to playing screen
        state = 1;
        window.setState(state);

        // Let user see two cards and flip em on screen
        chooseTwoCardsToPeek();

        // Game loop commence
        boolean playing = true;
        while(playing) {
            System.out.println("\nYour turn! Type 'draw' or 'windows!':");
            String choice = input.nextLine().toLowerCase();
            if(choice.equals("draw")){
                drawCardLogic();
            }
            else if(choice.equals("windows!")){
                endGame();
                playing=false;
            }
            else {
                System.out.println("Invalid choice. Try again.");
            }
            if(playing){
                computerTurn();
            }
        }
    }

    // Lookie lookie at two carsd! (display on screen)
    private void chooseTwoCardsToPeek() {
        System.out.println("Which two of your cards do you want to see? (Z,Y,X,W)");
        String choice1 = getValidCardChoice();
        String choice2 = getValidCardChoiceDifferent(choice1);

        // Call show the cards functions and repaint screen to show em
        int idx1 = getIndex(choice1);
        int idx2 = getIndex(choice2);
        revealedPlayerCards.add(idx1);
        revealedPlayerCards.add(idx2);
        window.repaint();

        // Continue as usual to write what the card is in the console
        System.out.println(choice1 + " is " + person.getHand().get(idx1));
        System.out.println(choice2 + " is " + person.getHand().get(idx2));

        System.out.println("Press Enter when done...");
        input.nextLine();

        revealedPlayerCards.remove((Integer)idx1);
        revealedPlayerCards.remove((Integer)idx2);
        window.repaint();
    }

    // Make sure user's choices are for reals
    private String getValidCardChoice(){
        while(true){
            String c = input.nextLine().toUpperCase();
            if(getIndex(c)!=-1) return c;
            System.out.println("Invalid. Must be (Z,Y,X,W).");
        }
    }
    private String getValidCardChoiceDifferent(String alreadyChosen){
        while(true){
            String c = input.nextLine().toUpperCase();
            if(!c.equals(alreadyChosen) && getIndex(c)!=-1){
                return c;
            }
            System.out.println("Invalid or already chosen. Try again:");
        }
    }

    // Drawing the card
    private void drawCardLogic(){
        if(deck.isEmpty()){
            System.out.println("No more cards in deck! Ending game.");
            endGame();
            return;
        }
        currentlyDrawing=true;
        currentDrawCard=deck.deal();
        window.repaint();

        System.out.println("You drew a " + currentDrawCard);
        if(isSpecial(currentDrawCard)){
            System.out.println("This is a special card! ('use' / 'replace' / 'discard')?");
            String action = input.nextLine().toLowerCase();
            if(action.equals("use")){
                specialCard(currentDrawCard);
                System.out.println("Do you want to 'keep' (replace your card) or 'discard' this " + currentDrawCard + "?");
                String postUse = input.nextLine().toLowerCase();
                if(postUse.equals("keep")){
                    replaceCard(currentDrawCard);
                } else {
                    discardCard(currentDrawCard);
                }
            }
            else if(action.equals("replace")){
                replaceCard(currentDrawCard);
            }
            else {
                // discard
                discardCard(currentDrawCard);
            }
        }
        else {
            // normal card
            System.out.println("Replace a card with this one? (yes/no)");
            String ans = input.nextLine().toLowerCase();
            if(ans.equals("yes")){
                replaceCard(currentDrawCard);
            } else {
                discardCard(currentDrawCard);
            }
        }

        currentDrawCard=null;
        currentlyDrawing=false;
        window.repaint();
    }

    // Discard function so we can see the discarded card on screen
    private void discardCard(Card c){
        if(c!=null){
            // Set index to 0 since it's the latest discarded card
            discardPile.add(0,c);
            System.out.println("Discarded " + c);
        }
    }

    // Replace function
    private void replaceCard(Card c){
        System.out.println("Which card do you want to replace? (Z,Y,X,W)");
        String choice = input.nextLine().toUpperCase();
        int idx = getIndex(choice);
        if(idx==-1){
            System.out.println("Invalid choice! Discarding " + c);
            discardCard(c);
        } else {
            Card old = person.getHand().get(idx);
            person.getHand().set(idx,c);
            System.out.println("Replacing " + choice + " which was " + old + " with " + c);
            discardCard(old);
        }
    }

    // Check if it's specialllll
    private boolean isSpecial(Card c){
        int v = c.getValue();
        return (v==7 || v==8 || v==11 || v==12);
    }

    // Special card options if it was a special card
    private void specialCard(Card c){
        switch(c.getValue()){
            case 7:
                System.out.println("7 => Peek at one of your cards! (Z,Y,X,W)?");
                String which = input.nextLine().toUpperCase();
                int idx = getIndex(which);
                if(idx!=-1){
                    revealedPlayerCards.add(idx);
                    window.repaint();
                    System.out.println(which + " is " + person.getHand().get(idx));
                    System.out.println("Press Enter to continue...");
                    input.nextLine();
                    revealedPlayerCards.remove((Integer)idx);
                    window.repaint();
                }
                break;
            case 8:
                System.out.println("8 => Peek at one of Computer's cards! (V,U,T,S)?");
                String cWhich = input.nextLine().toUpperCase();
                int cIdx = getIndex(cWhich);
                if(cIdx!=-1){
                    revealedComputerCards.add(cIdx);
                    window.repaint();
                    System.out.println(cWhich + " is " + computer.getHand().get(cIdx));
                    System.out.println("Press Enter to continue...");
                    input.nextLine();
                    revealedComputerCards.remove((Integer)cIdx);
                    window.repaint();
                }
                break;
            case 11:
                System.out.println("J is special! Blindly swap one of your cards (Z,Y,X,W) " +
                        "with one of the computer's (V,U,T,S)? (yes/no)");
                String blindSwap = input.nextLine().toLowerCase();
                if (blindSwap.equals("yes")) {
                    System.out.println("Which of your cards do you want to swap? (Z,Y,X,W)");
                    String bsPersonChoice = input.nextLine().toUpperCase();
                    int bsPersonIndex = getIndex(bsPersonChoice);

                    System.out.println("Which of computer's cards do you want to swap with? (V,U,T,S)");
                    String bsChoice = input.nextLine().toUpperCase();
                    int bsComputerIndex = getIndex(bsChoice);

                    if (bsPersonIndex != -1 && bsComputerIndex != -1) {
                        Card personCard = person.getHand().get(bsPersonIndex);
                        Card computerCard = computer.getHand().get(bsComputerIndex);
                        person.getHand().set(bsPersonIndex, computerCard);
                        computer.getHand().set(bsComputerIndex, personCard);
                        System.out.println("Successfully swapped your " + bsPersonChoice +
                                " with computer's " + bsChoice + "!");
                    } else {
                        System.out.println("Invalid choice. No swap performed.");
                    }
                } else {
                    System.out.println("No blind swap performed.");
                }
                break;

            // 12 => Q => look at one of your cards, optionally swap it with computer's
            case 12:
                System.out.println("Q is special! You may look at one of your cards (Z,Y,X,W), " +
                        "then optionally swap with the computer's (V,U,T,S).");
                String personChoice = input.nextLine().toUpperCase();
                int personIndex = getIndex(personChoice);
                if (personIndex != -1) {
                    // Reveal that card
                    revealedPlayerCards.add(personIndex);
                    window.repaint();
                    Card cardYouHave = person.getHand().get(personIndex);
                    System.out.println("Your card " + personChoice + " is " + cardYouHave);
                    System.out.println("Press Enter to continue...");
                    input.nextLine();
                    // Flip it back if you want:
                    revealedPlayerCards.remove((Integer)personIndex);
                    window.repaint();

                    System.out.println("Do you want to swap this card with the computer's? (yes/no)");
                    String swapResponse = input.nextLine().toLowerCase();
                    if (swapResponse.equals("yes")) {
                        System.out.println("Which of the computer's cards? (V,U,T,S)");
                        String compChoice = input.nextLine().toUpperCase();
                        int compIndex = getIndex(compChoice);
                        if (compIndex != -1) {
                            Card compCard = computer.getHand().get(compIndex);
                            person.getHand().set(personIndex, compCard);
                            computer.getHand().set(compIndex, cardYouHave);
                            System.out.println("Successfully swapped your " + personChoice +
                                    " with computer's " + compChoice + "!");
                        } else {
                            System.out.println("Invalid computer card choice. No swap.");
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

    // Simulate playing agianst someone
    private void computerTurn(){
        System.out.println("\n--- Computer's turn ---");
        if(deck.isEmpty()){
            endGame();
            return;
        }
        Card c = deck.deal();
        System.out.println("Computer draws a card (secret: "+ c + ")");

        // super-simple AI, discards everything
        discardCard(c);
    }

    // Endddddd!
    private void endGame(){
        System.out.println("Game Over!");
        revealedPlayerCards.clear();
        revealedComputerCards.clear();
        // Reveal all cards by flipping
        for(int i=0;i<4;i++){
            revealedPlayerCards.add(i);
            revealedComputerCards.add(i);
        }

        // Display final screen!
        state=2;
        window.setState(state);

        int pScore= person.calculateScore();
        int cScore= computer.calculateScore();
        System.out.println("Your hand: " + person.getHand());
        System.out.println("Computer hand: " + computer.getHand());
        System.out.println("Your score = " + pScore);
        System.out.println("Computer score = " + cScore);
        if(pScore<cScore){
            System.out.println("You win!");
        } else if(pScore>cScore){
            System.out.println("Computer wins!");
        } else {
            System.out.println("Tie!");
        }
    }

    private int getIndex(String choice){
        // Z=0, Y=1, X=2, W=3
        // V=0, U=1, T=2, S=3
        // This maps the letter to the same index but for the computer's side
        switch(choice){
            case "Z": case "V": return 0;
            case "Y": case "U": return 1;
            case "X": case "T": return 2;
            case "W": case "S": return 3;
        }
        return -1;
    }

    public static void main(String[] args){
        Game g= new Game();
        g.playGame();
    }
}
