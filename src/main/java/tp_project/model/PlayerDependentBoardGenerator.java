package tp_project.model;

/**
 * it is used for generating board on condition of number of players
 */
public class PlayerDependentBoardGenerator extends BoardGenerator {

    private final int numOfPlayers;
    private final BoardSize boardSize;
    Board board;

    public PlayerDependentBoardGenerator(int numOfPlayers, BoardSize boardSize) {
        this.numOfPlayers = numOfPlayers;
        this.boardSize = boardSize;
    }

    @Override
    public Board instantiateBoard() {

        if (numOfPlayers != 5 && numOfPlayers > 1 && numOfPlayers < 7) {
            board = new PlayersBoard(numOfPlayers, boardSize);
        }
        return board;
    }
}