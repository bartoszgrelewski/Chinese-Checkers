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

}
