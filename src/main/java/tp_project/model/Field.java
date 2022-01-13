package tp_project.model;

/**
 * There are defined properties of the field containing a pawn in this class
 */
public class Field{

    private int row;
    private int x;
    private Color color;
    private Zone zone;

    /**
     * Constructor
     * @param x First coordinate is order in given diagonal
     * @param row Second coordinate is number of row
     * @param color color
     * @param zone zone
     */
    public Field(int x, int row, Color color, Zone zone){

        this.x = x;
        this.row = row;
        this.color = color;
        this.zone = zone;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Zone getZone(){
        return zone;
    }

    public int getX(){
        return x;
    }

    public int getRow() {
        return row;
    }

}