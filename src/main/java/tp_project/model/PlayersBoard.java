package tp_project.model;

import java.util.ArrayList;

/**
 * defined creating board for number of players
 */
public class PlayersBoard extends AbstractBoard {

    private final int numOfPlayers;
    private final BoardSize boardSize;


    /**
     * Constructor created to determine the number of players in PlayerDependentBoardGenerator class
     */

    public PlayersBoard(int numOfPlayers, BoardSize boardSize) {
        this.numOfPlayers = numOfPlayers;
        this.boardSize = boardSize;
    }


    @Override
    public void generateBoard(IRuleSet rules) {
        fields = new ArrayList<>(); // rules and fields inherited from the parent class, that's why they are protected
        this.rules = rules;
        this.rules.setBoard(this);
        /**
         * This loop is responsible for iterating through Zone constants(thanks to values() function)
         */
        for (Zone z : Zone.values()) {
            generateZone(z, numOfPlayers, boardSize);
        }
    }

    /**
     * Method responsible for generating zones depending on the number of players
     *
     * @param zone         zone
     * @param numOfPlayers number of players
     * @param boardSize    size of the board
     */
    public void generateZone(Zone zone, int numOfPlayers, BoardSize boardSize) {

        switch (numOfPlayers) {
            case 2:                                                   // two players
                switch (zone) {
                    case CENTER:
                        generateCenterZone(boardSize);
                        break;
                    case RED_ZONE:
                        generateCornerZone(zone, Color.RED, boardSize);
                        break;
                    case YELLOW_ZONE:
                        generateCornerZone(zone, Color.YELLOW, boardSize);
                        break;
                    default:
                        generateCornerZone(zone, Color.NONE, boardSize);
                        break;
                }
                break;
            case 3:                                                  // three players
                switch (zone) {
                    case CENTER:
                        generateCenterZone(boardSize);
                        break;
                    case RED_ZONE:
                        generateCornerZone(zone, Color.RED, boardSize);
                        break;
                    case GREEN_ZONE:
                        generateCornerZone(zone, Color.GREEN, boardSize);
                        break;
                    case BLUE_ZONE:
                        generateCornerZone(zone, Color.BLUE, boardSize);
                        break;
                    default:
                        generateCornerZone(zone, Color.NONE, boardSize);
                        break;
                }
                break;
            case 4:                                                  // four players
                switch (zone) {
                    case CENTER:
                        generateCenterZone(boardSize);
                        break;
                    case RED_ZONE:
                        generateCornerZone(zone, Color.RED, boardSize);
                        break;
                    case YELLOW_ZONE:
                        generateCornerZone(zone, Color.YELLOW, boardSize);
                        break;
                    case BLUE_ZONE:
                        generateCornerZone(zone, Color.BLUE, boardSize);
                        break;
                    case ORANGE_ZONE:
                        generateCornerZone(zone, Color.ORANGE, boardSize);
                        break;
                    default:
                        generateCornerZone(zone, Color.NONE, boardSize);
                        break;
                }
                break;
            case 6:                                                  // six players
                switch (zone) {
                    case CENTER:
                        generateCenterZone(boardSize);
                        break;
                    case RED_ZONE:
                        generateCornerZone(zone, Color.RED, boardSize);
                        break;
                    case YELLOW_ZONE:
                        generateCornerZone(zone, Color.YELLOW, boardSize);
                        break;
                    case BLUE_ZONE:
                        generateCornerZone(zone, Color.BLUE, boardSize);
                        break;
                    case ORANGE_ZONE:
                        generateCornerZone(zone, Color.ORANGE, boardSize);
                        break;
                    case GREEN_ZONE:
                        generateCornerZone(zone, Color.GREEN, boardSize);
                        break;
                    case BLACK_ZONE:
                        generateCornerZone(zone, Color.BLACK, boardSize);
                        break;
                    default:
                        generateCornerZone(zone, Color.NONE, boardSize);
                        break;
                }
                break;
        }
    }
}
