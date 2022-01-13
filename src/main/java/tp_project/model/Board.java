package tp_project.model;

import java.util.List;

public interface Board{

    void generateBoard(IRuleSet rules);

    List<Field> getFields();
    void temporaryPrint();
    Field getFieldAt(int i, int j);
    boolean checkWin(Color winningColor);
    void move(int oldX, int oldRow, int newX, int newRow, Color movingColor) throws WrongMoveException;
    void setAnotherMoveValue(boolean isAnotherMove);
    boolean getAnotherMoveValue();
    boolean isValidMove(Field oldField, Field newField, Color movingColor);
    boolean isAnotherMoveAvailable(int x, int row, Field jumpedField);

}