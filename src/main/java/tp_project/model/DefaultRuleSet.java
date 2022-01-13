package tp_project.model;
// TODO do zmiany

/**
 * Proper implementation of the rules defined in this class
 */
public class DefaultRuleSet implements IRuleSet {
    private boolean isAnotherMove;
    private Field lastMoveField;
    private Board board;

    public DefaultRuleSet() {
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public boolean isValidMove(Field oldField, Field newField, Color movingColor) {
        if (oldField == null || newField == null)                                                   // field not found
            return false;
        if (oldField.getColor().equals(Color.NONE))                                                 // we cannot move blank field
            return false;
        if (oldField.getColor().getEndingZone().equals(oldField.getZone()) && !newField.getZone().equals(oldField.getZone()))  // we cannot leave ending zone
            return false;
        if (!newField.getColor().equals(Color.NONE))                                                // we cannot move busy field
            return false;
        if (movingColor != oldField.getColor())
            return false;
        if (isAnotherMove && oldField != lastMoveField)
            return false;

        int dX = newField.getX() - oldField.getX();
        int dR = newField.getRow() - oldField.getRow();

        Field jumpedField = null;

        if (dX + dR != 0 && dX + dR != dX && dX + dR != dR)
            return false;

        if (Math.abs(dX) > 2 || Math.abs(dR) > 2)
            return false;

        if (Math.abs(dX) == 2 || Math.abs(dR) == 2) {
            jumpedField = board.getFieldAt(oldField.getX() + (dX / 2), oldField.getRow() + (dR / 2));
            if (jumpedField != null && jumpedField.getColor() == Color.NONE)
                return false;
        }

        if (Math.abs(dX) == 1 || Math.abs(dR) == 1)
            if (isAnotherMove)
                return false;
            else {
                return true;
            }

        isAnotherMove = isAnotherMoveAvailable(newField.getX(), newField.getRow(), jumpedField);
        lastMoveField = newField;
        return true;
    }

    @Override
    public boolean isAnotherMoveAvailable(int newX, int newRow, Field jumpedField) {
        int dir[][] = {{1, 0},                                               // we treat it as a matrix
                {1, -1},                                                    // for example element 00 is 1, 30 is -1
                {0, -1},
                {-1, 0},
                {-1, 1},
                {0, 1}};
        boolean neighField = false; // neighbour field
        boolean nextField = false;

        for (int i = 0; i < 6; i++) {
            for (Field field : board.getFields()) {
                if ((field != jumpedField) && (field.getX() == newX + dir[i][0] && field.getRow() == newRow + dir[i][1] && field.getColor() != Color.NONE)) { // jump right
                    neighField = true;
                }
                if (field.getX() == newX + 2 * dir[i][0] && field.getRow() == newRow + 2 * dir[i][1] && field.getColor() == Color.NONE)
                    nextField = true;
            }
            if (neighField && nextField)
                return true;

            neighField = false;
            nextField = false;
        }
        return false;
    }


    @Override
    public void setAnotherMoveValue(boolean isAnotherMove) {
        this.isAnotherMove = isAnotherMove;
    }

    @Override
    public boolean getAnotherMoveValue() {
        return isAnotherMove;
    }
}
