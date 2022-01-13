package tp_project.model;

import java.util.ArrayList;
import java.util.List;
/**
 * The field creation logic defined in this class.
 */
public abstract class AbstractBoard implements Board{

    protected ArrayList<Field> fields;  //protected -> visible in child classes
    protected IRuleSet rules;

    public void generateCenterZone(BoardSize boardSize) {
        switch (boardSize) {
            case BIGGER_BOARD:
                int k = 0;

                for (int i = 5; i <= 13; i++) {                         // filling in the bottom center of the board
                    for (int j = 9; j <= 13 - k; j++)
                        fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

                    if (i >= 9)
                        k++;
                }

                k = 0;

                for (int i = 13; i >= 6; i--) {
                    // filling in the top center of the board
                    for (int j = 8; j >= 5 + k; j--)
                        fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

                    if (i <= 9)
                        k++;
                }
                break;
            case SMALLER_BOARD:
                int l = 0;

                for (int i = 4; i <= 10; i++) {                         // filling in the bottom center of the board
                    for (int j = 7; j <= 10 - l; j++)
                        fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

                    if (i >= 7)
                        l++;
                }

                l = 0;

                for (int i = 10; i >= 5; i--) {
                    // filling in the top center of the board
                    for (int j = 6; j >= 4 + l; j--)
                        fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

                    if (i <= 7)
                        l++;
                }
                break;
        }
    }

    public void generateCornerZone(Zone zone, Color color, BoardSize boardSize) {
        switch (boardSize) {
            case BIGGER_BOARD:
                int x = 0;
                int row = 0;
                int deltaX = 0;
                int deltaRow = 0;
                switch (zone) {                                        // filling in the corners
                    case RED_ZONE:
                        x = 1;
                        row = 13;
                        deltaX = 1;
                        deltaRow = -1;
                        break;
                    case BLACK_ZONE:
                        x = 8;
                        row = 5;
                        deltaX = -1;
                        deltaRow = 1;
                        break;
                    case BLUE_ZONE:
                        x = 10;
                        row = 4;
                        deltaX = 1;
                        deltaRow = -1;
                        break;
                    case YELLOW_ZONE:
                        x = 17;
                        row = 5;
                        deltaX = -1;
                        deltaRow = 1;
                        break;
                    case GREEN_ZONE:
                        x = 10;
                        row = 13;
                        deltaX = 1;
                        deltaRow = -1;
                        break;
                    case ORANGE_ZONE:
                        x = 8;
                        row = 14;
                        deltaX = -1;
                        deltaRow = 1;
                        break;

                }
                for (int i = x; i != x + 4 * deltaX; i += deltaX) {
                    for (int j = row; j != row + (Math.abs(i - x) + 1) * deltaRow; j += deltaRow) {
                        fields.add(new Field(i, j, color, zone));
                        System.out.println("i:" + i + " j:" + j + " color:" + color + " zone:" + zone);
                    }
                }
                break;
            case SMALLER_BOARD:
                int x1 = 0;
                int row1 = 0;
                int deltaX1 = 0;
                int deltaRow1 = 0;

                switch (zone) {                                        // filling in the corners
                    case RED_ZONE:
                        x1 = 1;
                        row1 = 10;
                        deltaX1 = 1;
                        deltaRow1 = -1;
                        break;
                    case BLACK_ZONE:
                        x1 = 6;
                        row1 = 4;
                        deltaX1 = -1;
                        deltaRow1 = 1;
                        break;
                    case BLUE_ZONE:
                        x1 = 8;
                        row1 = 3;
                        deltaX1 = 1;
                        deltaRow1 = -1;
                        break;
                    case YELLOW_ZONE:
                        x1 = 13;
                        row1 = 4;
                        deltaX1 = -1;
                        deltaRow1 = 1;
                        break;
                    case GREEN_ZONE:
                        x1 = 8;
                        row1 = 10;
                        deltaX1 = 1;
                        deltaRow1 = -1;
                        break;
                    case ORANGE_ZONE:
                        x1 = 6;
                        row1 = 11;
                        deltaX1 = -1;
                        deltaRow1 = 1;
                        break;
                }

                for (int i = x1; i != x1 + 3 * deltaX1; i += deltaX1) {
                    for (int j = row1; j != row1 + (Math.abs(i - x1) + 1) * deltaRow1; j += deltaRow1) {
                        fields.add(new Field(i, j, color, zone));
                    }
                }
                break;
        }


    }

    @Override
    public boolean checkWin(Color winningColor) {

        /**
         * filter() returns a stream consisting of the elements of this stream that match the given predicate
         */
        if(fields.stream().filter(f -> f.getZone().equals(winningColor.getEndingZone())).allMatch(f -> f.getColor().equals(winningColor))) {
            return true;
        }

        return false;
    }

    @Override
    public void move(int oldX, int oldRow, int newX, int newRow, Color movingColor) throws WrongMoveException {
        Field oldField = fields.stream().filter(f -> f.getX() == oldX && f.getRow() == oldRow).findFirst().orElse(null);
        Field newField = fields.stream().filter(f -> f.getX() == newX && f.getRow() == newRow).findFirst().orElse(null);

        if(!isValidMove(oldField, newField,movingColor)) {
            throw new WrongMoveException();
        }

        newField.setColor(oldField.getColor());
        oldField.setColor(Color.NONE);
    }

    @Override
    public boolean isValidMove(Field oldField, Field newField, Color movingColor) {
        return rules.isValidMove(oldField, newField, movingColor);
    }

    @Override
    public boolean isAnotherMoveAvailable(int x, int row, Field jumpedField) {
        return rules.isAnotherMoveAvailable(x, row, jumpedField);
    }

    @Override
    public Field getFieldAt(int x, int row) {
        return fields.stream().filter(f -> f.getX() == x && f.getRow() == row).findFirst().orElse(null);
    }

    @Override
    public void temporaryPrint() {
        fields.forEach(System.out::println);
        System.out.println(fields.size());
    }

    @Override
    public List<Field> getFields(){
        return fields;
    }

    @Override
    public void setAnotherMoveValue(boolean isAnotherMove) {
        rules.setAnotherMoveValue(isAnotherMove);
    }

    @Override
    public boolean getAnotherMoveValue() {
        return rules.getAnotherMoveValue();
    }

}