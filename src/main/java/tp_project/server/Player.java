package tp_project.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import tp_project.model.Color;

/**
 * I/O between Client/Server
 */
public class Player extends Thread implements Observer {

  private Color color;
  private Socket socket;
  private BufferedReader input;
  private PrintWriter output;
  private String clientCommand;
  private DefaultGameRoom interpreter;

  public Player(Socket socket, DefaultGameRoom interpreter) throws IOException {

    this.socket = socket;
    this.interpreter = interpreter;
    input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    output = new PrintWriter(this.socket.getOutputStream(), true);
  }


  @Override
  public void run() {

    while (true) {

      try {
        clientCommand = input.readLine();
      } catch (IOException e) {
        try {
          socket.close();
          interpreter.observerChanged("QUIT", this);
          break;
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }

      if (clientCommand == null) {
        interpreter.observerChanged("QUIT", this);
        break;
      }
      interpreter.observerChanged(clientCommand, this);
    }
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public Socket getSocket() {
    return this.socket;
  }

  @Override
  public void updateObserver(String message) {
    this.output.println(message);
  }
}