package tp_project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * server class
 */
public class Server {
  private static Server instance = null;
  private ServerSocket serverSocket;
  private DefaultGameRoom interpreter;

  public Server() {
  }

  public static Server getInstance() {
    if (instance == null) {
      synchronized (Server.class) {
        if (instance == null) {
          instance = new Server();
        }
      }
    }

    return instance;
  }

  public void run() throws InterruptedException {

    try {
      serverSocket = new ServerSocket(9090);
      interpreter = new DefaultGameRoom();

      while (true) {
        Socket socket = serverSocket.accept();
        Player newPlayer = new Player(socket, interpreter);
        newPlayer.start();
      }
    } catch (IOException ex) {
    }
  }

  public static void main(String[] args) throws InterruptedException {
    getInstance().run();
  }
}