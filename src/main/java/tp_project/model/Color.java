package tp_project.model;

/**
 * Colors of pawns are defined in this class.
 */
public enum Color {
    RED,
    YELLOW,
    GREEN,
    BLACK,
    BLUE,
    ORANGE,
    NONE;

    private Zone endingZone;

    /**
     * We want to initialize variables without static block.
     **/

    public static void initColor() {
        RED.endingZone = Zone.RED_ZONE.getOppositeZone();
        YELLOW.endingZone = Zone.YELLOW_ZONE.getOppositeZone();
        GREEN.endingZone = Zone.GREEN_ZONE.getOppositeZone();
        BLACK.endingZone = Zone.BLACK_ZONE.getOppositeZone();
        BLUE.endingZone = Zone.BLUE_ZONE.getOppositeZone();
        ORANGE.endingZone = Zone.ORANGE_ZONE.getOppositeZone();
        NONE.endingZone = Zone.CENTER.getOppositeZone();
    }

    public Zone getEndingZone() {
        initColor();
        return endingZone;
    }
}
