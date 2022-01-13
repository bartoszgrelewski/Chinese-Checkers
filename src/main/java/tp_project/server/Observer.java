package tp_project.server;

import java.net.Socket;

import tp_project.model.Color;

/**
 * design pattern observer class
 */
public interface Observer {

  public void updateObserver(String message);

  public Color getColor();

  public void setColor(Color color);

  public Socket getSocket();

}