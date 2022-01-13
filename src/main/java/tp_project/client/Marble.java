package tp_project.client;

import javafx.scene.shape.*;
import javafx.scene.paint.*;

import java.util.Objects;

/**
 * Class extending Ellipse (JavaFX)
 */
public class Marble extends Ellipse {

  private final int X;
  private final int Row;
  private String marbleColor;

  /**
   * It creates Ellipses
   * @param X coordinate
   * @param Row row
   * @param marbleColor color
   * @param ShiftX shift of x
   * @param ShiftRow shift of row
   */
  Marble(int X, int Row, String marbleColor, int ShiftX, int ShiftRow) {

    this.X = X;
    this.Row = Row;
    this.marbleColor = marbleColor;

    setRadiusX(Client.MARBLE_RADIUS_SIZE - 3);
    setRadiusY(Client.MARBLE_RADIUS_SIZE - 3);
    relocate(ShiftX + 2, ShiftRow + 2);

    if (Objects.equals(marbleColor, "RED")) {
      setFill(Color.RED);
    } else if (Objects.equals(marbleColor, "YELLOW")) {
      setFill(Color.YELLOW);
    } else if (Objects.equals(marbleColor, "BLUE")) {
      setFill(Color.BLUE);
    } else if (Objects.equals(marbleColor, "NONE")) {
      setFill(Color.valueOf("#e4e4e4"));
    } else if (Objects.equals(marbleColor, "ORANGE")) {
      setFill(Color.ORANGE);
    } else if (Objects.equals(marbleColor, "GREEN")) {
      setFill(Color.GREEN);
    } else if (Objects.equals(marbleColor, "BLACK")) {
      setFill(Color.BLACK);
    }

    setStroke(Color.BLACK);
    setStrokeWidth(1);
    setStrokeType(StrokeType.INSIDE);
  }

  /**
   * getter of X
   * @return X
   */
  public int getX() {
    return X;
  }

  /**
   * getter of row
   * @return Row
   */
  public int getRow() {
    return Row;
  }

  /**
   * getter of Color
   * @return Color
   */
  public String getColor() {
    return marbleColor;
  }

  /**
   * setter of color used in setting colo
   * @param color color
   */
  public void setColor(String color) {
    this.marbleColor = color;
  }
}
