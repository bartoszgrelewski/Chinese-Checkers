package tp_project.model;

/**
 * abstract class boardGenerator
 */
public abstract class BoardGenerator {

    public Board generateBoard(IRuleSet rules) {
        Board generatedBoard = instantiateBoard();
        generatedBoard.generateBoard(rules);
        return generatedBoard;
    }

    public abstract Board instantiateBoard();
}