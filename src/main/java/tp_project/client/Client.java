package tp_project.client;

import java.io.*;
import java.net.Socket;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tp_project.model.*;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.StrokeType;


/**
 * Client class which should be started after Server
 * It extends Application which is JavaFX library
 * Client has JavaFX GUI implemented in
 * This class also sends messages to Server.
 */
public class Client extends Application {

  static final int MARBLE_DIAMETER_SIZE = 28;
  static final int MARBLE_RADIUS_SIZE = 14;

  private String playerColor;
  private boolean join;
  private boolean win;

  private Marble oldField;
  private Marble newField;

  private String commandLine;
  private Socket socket;
  private BufferedReader input;
  private PrintWriter output;

  private Scene primaryScene;
  private TextArea text;
  private Board board;
  private List<Field> fields;
  private Group marblesGroup;

  /**
   * It is main method which runs JavaFX GUI
   *
   * @param args arguments
   * @throws IOException I/O exceptions
   */
  public static void main(String args[]) throws IOException {
    launch();
  }

  /**
   * There is JavaFX GUI implemented in
   *
   * @param primaryStage is constructed by the platform
   * @throws Exception exceptions
   */
  @Override
  public void start(Stage primaryStage) throws Exception {

    BorderPane borderPane = new BorderPane();
    primaryStage.setTitle("Chinese-Checkers-Game");

    marblesGroup = new Group();
    text = new TextArea();
    text.setPrefSize(200, 100);
    text.setMaxWidth(400);
    text.setMaxHeight(100);
    text.setEditable(false);
    primaryScene = new Scene(borderPane, 900, 600);

    //Buttons
    Button button_1 = new Button("JOIN");
    Button button_2 = new Button("START");
    Button button_3 = new Button("PASS");
    Button button_4 = new Button("QUIT");

    //Buttons' handlers
    button_1.setOnMouseClicked(e -> output.println("JOIN"));
    button_2.setOnMouseClicked(e -> output.println("START"));
    button_3.setOnMouseClicked(e -> output.println("PASS"));
    button_4.setOnAction((ActionEvent event) -> {
      output.println("QUIT");
      Platform.exit();
    });

    VBox vbox = new VBox(button_2, button_1, button_3, button_4);

    vbox.setAlignment(Pos.CENTER_LEFT);

    VBox.setMargin(button_1, new Insets(10, 0, 0, 20));
    VBox.setMargin(button_2, new Insets(10, 0, 0, 20));
    VBox.setMargin(button_3, new Insets(10, 0, 0, 20));
    VBox.setMargin(button_4, new Insets(10, 0, 0, 20));

    borderPane.setCenter(marblesGroup);
    borderPane.setTop(text);
    borderPane.setLeft(vbox);

    primaryStage.setScene(primaryScene);

    primaryStage.setOnCloseRequest(e -> {
      if (output != null) {
        output.println("QUIT");
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        Platform.exit();
      }
    });
    primaryStage.show();
    this.connectToServer();

    new Thread(() -> {
      try {
        while (input != null) {

          try {
            commandLine = input.readLine();
          } catch (IOException ex) {
            socket.close();
            return;
          }
          if (commandLine == null) return;

          if (commandLine.equals("JOINED")) {
            printUserMessage("You have joined the game successfully.\n");
            join = true;
          } else if (commandLine.equals("FAIL") && !join) {
            printUserMessage("Failed during joining the game.\n");
          } else if (commandLine.equals("GAME_STARTED")) {

            String playerNumber;
            playerColor = input.readLine();
            playerNumber = input.readLine();
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                setUpGame(playerColor, playerNumber, marblesGroup, BoardSize.BIGGER_BOARD); //available change for smaller board
              }
            });
            printUserMessage("The game has started, your color is " + playerColor + ".\n");
          } else if (commandLine.equals("WRONG_MOVE")) {
            printUserMessage("Wrong move !\n");
          } else if (commandLine.equals("FINISH")) {

            printUserMessage("Wait for your turn.\n");

            if (oldField != null && newField != null) {
              Color temp = (Color) oldField.getFill();
              oldField.setFill(Color.valueOf("#e4e4e4"));
              newField.setColor(oldField.getColor());
              oldField.setColor("NONE");
              oldField.setStroke(Color.BLACK);
              oldField.setStrokeWidth(1);
              oldField.setStrokeType(StrokeType.INSIDE);
              newField.setStroke(Color.GOLD);
              newField.setStrokeType(StrokeType.INSIDE);
              newField.setStrokeWidth(3);
              newField.setFill(temp);
              oldField = newField;
              newField = null;
            }
          } else if (commandLine.equals("MOVE_AGAIN")) {

            printUserMessage("You can move again.\n");
            Color temp = (Color) oldField.getFill();
            oldField.setFill(Color.valueOf("#e4e4e4"));
            newField.setColor(oldField.getColor());
            oldField.setColor("NONE");
            newField.setFill(temp);
            oldField.setStroke(Color.BLACK);
            oldField.setStrokeWidth(1);
            oldField.setStrokeType(StrokeType.INSIDE);
            newField.setStroke(Color.GOLD);
            newField.setStrokeType(StrokeType.INSIDE);
            newField.setStrokeWidth(3);
            oldField = newField;
            newField = null;
          } else if (commandLine.equals("MOVE")) {
            printUserMessage("It is your turn to move.\n");
          } else if (commandLine.startsWith("MOVED ")) {
            String movingCommand = commandLine;
            Platform.runLater(new Runnable() {
              @Override
              public void run() {
                moveOpponentsMarble(movingCommand);
              }
            });
          } else if (commandLine.equals("PASSED")) {
            printUserMessage("Wait for your turn.\n");
          } else if ((commandLine.startsWith("WIN"))) {

            String[] splited = commandLine.split("\\s+");

            if (splited[1].equals("1")) {
              text.appendText("Congratulations! you are WINNER\n");
            } else {
              text.appendText("Congratulations, you have finished, your place : " + splited[1]);
            }

            win = true;
          }
        }
      } catch (IOException e) {
        try {
          socket.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }).start();
  }

  /**
   * This method has I/O between Client and Server implemented in
   */
  private void connectToServer() {
    try {
      socket = new Socket("localhost", 9090);
    } catch (IOException e) {
      Platform.exit();
      return;
    }
    try {
      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      text.appendText("Cannot connect to server");
    }
    try {
      output = new PrintWriter(socket.getOutputStream(), true);
    } catch (IOException e) {
      text.appendText("Cannot connect to sever");
    }
  }

  /**
   * Setting up a new game
   */
  public void setUpGame(String color, String playersNumber, Group marbles, BoardSize boardSize) {
    int[] verseShift = new int[18];
    int[] firstInRow = new int[18];

    if (boardSize.equals(BoardSize.BIGGER_BOARD)) {
      verseShift = new int[]{12, 11, 10, 9, 0, 1, 2, 3, 4, 3, 2, 1, 0, 9, 10, 11, 12};       // Bigger board
      firstInRow = new int[]{13, 12, 11, 10, 5, 5, 5, 5, 5, 4, 3, 2, 1, 5, 5, 5, 5};
    } else if (boardSize.equals(BoardSize.SMALLER_BOARD)) {
      verseShift = new int[]{1, 2, 1, 0, 1, 2, 3, 2, 3, 0, 1, 2, 3};                         // Smaller board
      firstInRow = new int[]{6, 6, 5, 4, 4, 4, 4, 3, 3, 1, 1, 1, 1};
    }

    //int[]verseShift = {12, 11, 10, 9, 0, 1, 2, 3, 4, 3, 2, 1, 0, 9, 10, 11, 12};       // Bigger board
    //int[]firstInRow = {13, 12, 11, 10, 5, 5, 5, 5, 5, 4, 3, 2, 1, 5, 5, 5, 5};
    BoardGenerator generator = null;
    IRuleSet rules = new DefaultRuleSet();

    switch (playersNumber) {
      case "2" -> generator = new PlayerDependentBoardGenerator(2, boardSize);
      case "3" -> generator = new PlayerDependentBoardGenerator(3, boardSize);
      case "4" -> generator = new PlayerDependentBoardGenerator(4, boardSize);
      case "6" -> generator = new PlayerDependentBoardGenerator(6, boardSize);
    }

    if (generator != null) board = generator.generateBoard(rules);

    fields = board.getFields();
    Marble marble;

    for (Field f : fields) {

      int Row = f.getRow();
      int X = f.getX();
      int ShiftX = (X - firstInRow[Row - 1]) * MARBLE_DIAMETER_SIZE + verseShift[Row - 1] * MARBLE_RADIUS_SIZE;
      int ShiftRow = (Row - 1) * MARBLE_DIAMETER_SIZE;

      marble = new Marble(X, Row, f.getColor().toString(), ShiftX, ShiftRow);

      marble.setOnMouseClicked(e -> {
        Marble tempMarble = (Marble) e.getSource();
        String marbleColor = tempMarble.getColor();

        if (!win && marbleColor.equals(playerColor) && tempMarble != oldField) {
          if (oldField != null) {
            oldField.setStrokeWidth(1);
            oldField.setStroke(Color.BLACK);
            oldField.setStrokeType(StrokeType.INSIDE);
            oldField = null;
          }

          oldField = (Marble) e.getSource();
          ((Marble) e.getSource()).setStroke(Color.GOLD);
          ((Marble) e.getSource()).setStrokeType(StrokeType.INSIDE);
          ((Marble) e.getSource()).setStrokeWidth(3);
        } else if (oldField == tempMarble && !win) {
          oldField.setStrokeWidth(1);
          oldField.setStroke(Color.BLACK);
          oldField.setStrokeType(StrokeType.INSIDE);
          oldField = null;
        } else if (!win && oldField != null && oldField != tempMarble && marbleColor != playerColor) {
          newField = (Marble) e.getSource();
          if (newField != null) {
            String command = "MOVE";
            command += " " + oldField.getX();
            command += " " + oldField.getRow();
            command += " " + newField.getX();
            command += " " + newField.getRow();
            output.println(command);
          }
        }
      });
      marbles.getChildren().add(marble);
    }
  }

  /**
   * This method reads other players moves
   *
   * @param coordinates are written in string variable
   *                    later are splitted by .split()
   *                    \\s+ -> it means that all regex are based on whitespaces
   */
  public void moveOpponentsMarble(String coordinates) {

    String[] movingCoordinates = coordinates.split("\\s+");
    if (movingCoordinates.length != 6) {
      return;
    }

    String color = movingCoordinates[1];
    int oldX = 0;
    int oldRow = 0;
    int newX = 0;
    int newRow = 0;

    try {
      oldX = Integer.parseInt(movingCoordinates[2]);
      oldRow = Integer.parseInt(movingCoordinates[3]);
      newX = Integer.parseInt(movingCoordinates[4]);
      newRow = Integer.parseInt(movingCoordinates[5]);
    } catch (NumberFormatException ex) {
    }

    for (Node marble : marblesGroup.getChildren()) {
      if (((Marble) marble).getX() == oldX && ((Marble) marble).getRow() == oldRow) {
        ((Marble) marble).setFill(Color.valueOf("#e4e4e4"));
      } else if (((Marble) marble).getX() == newX && ((Marble) marble).getRow() == newRow) {

        switch (color) {
          case "BLUE":
            ((Marble) marble).setFill(Color.BLUE);
            break;
          case "ORANGE":
            ((Marble) marble).setFill(Color.ORANGE);
            break;
          case "BLACK":
            ((Marble) marble).setFill(Color.BLACK);
            break;
          case "YELLOW":
            ((Marble) marble).setFill(Color.YELLOW);
            break;
          case "RED":
            ((Marble) marble).setFill(Color.RED);
            break;
          case "GREEN":
            ((Marble) marble).setFill(Color.GREEN);
            break;
          default:
            break;
        }
      }
    }
  }

  /**
   * Printing message to Server
   *
   * @param message
   */
  public void printUserMessage(String message) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        text.appendText(message);
      }
    });
  }
}
