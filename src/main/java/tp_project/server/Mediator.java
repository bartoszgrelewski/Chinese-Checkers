package tp_project.server;

/**
 * design pattern Mediator class
 */
public interface Mediator {

  public void attachObserver(Observer player);

  public void detachObserver(Observer player);

  public void observerChanged(String command, Observer player);

  public void setUpAttributes();

  public void noticeAboutStarting();

  public void noticeAboutMove(Observer player, int oldX, int oldRow, int newX, int newRow);

  public void noticeAboutPlayersTurn(Observer player);
}