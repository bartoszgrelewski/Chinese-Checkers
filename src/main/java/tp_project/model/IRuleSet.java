package tp_project.model;

public interface IRuleSet {
    boolean isValidMove(Field oldField, Field newField,Color movingColor);
    boolean isAnotherMoveAvailable(int newX, int newRow, Field jumpedField);
    void setAnotherMoveValue(boolean isAnotherMove);
    boolean getAnotherMoveValue();
    void setBoard(Board board);
}
