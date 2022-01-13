package tp_project.model;

/**
 * class which has enums consists of pawns' colors
 */
public enum Zone {
    YELLOW_ZONE,
    RED_ZONE,
    BLACK_ZONE,
    GREEN_ZONE,
    ORANGE_ZONE,
    BLUE_ZONE,
    CENTER;

    private Zone opposite;

    public static void initZone() {
        YELLOW_ZONE.opposite = RED_ZONE;
        RED_ZONE.opposite = YELLOW_ZONE;
        BLACK_ZONE.opposite = GREEN_ZONE;
        GREEN_ZONE.opposite = BLACK_ZONE;
        ORANGE_ZONE.opposite = BLUE_ZONE;
        BLUE_ZONE.opposite = ORANGE_ZONE;
        CENTER.opposite = CENTER;
    }

    public Zone getOppositeZone() {
        initZone();
        return opposite;
    }
}
