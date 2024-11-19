public class Game {
    // Instance variables
    private Deck decks;
    private Player players;

    public Game(Deck decks, Player players) {
        this.decks = decks;
        this.players = players;
    }

    public void printInstructions(){
        System.out.println("Welcome to the game of Windows!");
        System.out.println("You will be delt 4 cards (no peeking!) and...");
    }

    public void playGame(){

    }

    public void main(){
        playGame();
    }
}
