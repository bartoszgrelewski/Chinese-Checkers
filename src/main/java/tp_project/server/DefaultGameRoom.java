package tp_project.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import tp_project.model.*;

/**
 * this class has implemented mediator in which
 * reduces the dependencies between communicating objects
 * objects no longer communicate directly with each other
 */
public class DefaultGameRoom implements Mediator {

  private IRuleSet rules;
  private Board board;
  private boolean isStarted;
  private ArrayList<Observer> players;
  private Observer movingPlayer;
  private ArrayList<Observer> winners;
  private BoardSize boardSize;

  public DefaultGameRoom() {
    players = new ArrayList<>();
    winners = new ArrayList<>();
    isStarted = false;
    boardSize = BoardSize.BIGGER_BOARD;    //available change smaller board
  }

  @Override
  public void attachObserver(Observer player) {
    players.add(player);
  }

  @Override
  public void detachObserver(Observer player) {
    players.remove(player);
    try {
      player.getSocket().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public synchronized void observerChanged(String command, Observer player) {

    String[] splitCommand = command.split("\\s+");

    if (splitCommand.length == 0) {
      player.updateObserver("FAIL");
    }

    if (splitCommand[0].equals("JOIN")) {

      if (players.size() < 6 && !players.contains(player) && !isStarted) {

        attachObserver(player);
        player.updateObserver("JOINED");
      } else {
        player.updateObserver("FAIL");
      }
    } else if (splitCommand[0].equals("START") && players.contains(player)) { // Jeden z graczy(obojetnie ktory) startuje gre, wszyscy otrzymuja powiadomienia GAME_STARTED

      if (!isStarted) {
        if (players.size() > 1 && players.size() != 5) {

          generateGame(players.size());
          isStarted = true;
          noticeAboutStarting();
          setUpAttributes();
          movingPlayer = setFirstPlayer();
          noticeAboutPlayersTurn(movingPlayer);
        } else {
          player.updateObserver("FAIL");
        }
      }
    } else if (splitCommand[0].equals("MOVE") && player == movingPlayer && isStarted) {

      boolean anotherMove = false;
      int coordinates[] = new int[4];

      if (splitCommand.length < 5) {
        player.updateObserver("FAIL");
        return;
      }

      for (int i = 1; i < 5; i++) {

        try {
          coordinates[i - 1] = Integer.parseInt(splitCommand[i]);
        } catch (NumberFormatException ex) {
          player.updateObserver("FAIL");
          return;
        }
      }

      try {
        board.move(coordinates[0], coordinates[1], coordinates[2], coordinates[3], player.getColor());
        anotherMove = board.getAnotherMoveValue();
        noticeAboutMove(player, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
      } catch (WrongMoveException ex) {
        player.updateObserver("WRONG_MOVE");
        return;
      }

      if (anotherMove) {
        if (board.checkWin(player.getColor()) && !winners.contains(player)) {
          player.updateObserver("FINISH");
          movingPlayer = setAnotherPlayer(player);
          noticeAboutPlayersTurn(setAnotherPlayer(player));
          winners.add(player);
          player.updateObserver("WIN " + winners.size());
          players.remove(player);
          return;
        }

        player.updateObserver("MOVE_AGAIN");
      } else if (!anotherMove) {

        player.updateObserver("FINISH");
        movingPlayer = setAnotherPlayer(player);
        noticeAboutPlayersTurn(setAnotherPlayer(player));

        if (board.checkWin(player.getColor()) && !winners.contains(player)) {
          winners.add(player);
          player.updateObserver("WIN " + winners.size());
          players.remove(player);
          return;
        }
      }
    } else if (splitCommand[0].equals("PASS") && player == movingPlayer && players.size() > 0 && isStarted) {

      movingPlayer = setAnotherPlayer(player);
      board.setAnotherMoveValue(false);
      player.updateObserver("PASSED");
      noticeAboutPlayersTurn(movingPlayer);
    } else if (splitCommand[0].equals("QUIT")) {
      detachObserver(player);

      if (players.size() == 0) {
        players = new ArrayList<Observer>();
        winners = new ArrayList<Observer>();
        isStarted = false;
        return;
      } else if (player == movingPlayer) {
        movingPlayer = setAnotherPlayer(player);
        noticeAboutPlayersTurn(movingPlayer);
      }
    }
  }

  @Override
  public void setUpAttributes() {

    String Color;
    String number = Integer.toString(players.size());

    for (Observer p : players) {
      Color = p.getColor().toString();
      p.updateObserver(Color);
      p.updateObserver(number);
    }
  }

  @Override
  public void noticeAboutStarting() {

    for (Observer p : players) {
      p.updateObserver("GAME_STARTED");
    }
  }

  @Override
  public void noticeAboutMove(Observer player, int oldX, int oldRow, int newX, int newRow) {

    String command = "MOVED ";
    command += (player.getColor().toString() + " ");
    command += (Integer.toString(oldX) + " ");
    command += (Integer.toString(oldRow) + " ");
    command += (Integer.toString(newX) + " ");
    command += (Integer.toString(newRow) + " ");

    for (Observer p : players) {
      if (p != player) {
        p.updateObserver(command);
      }
    }
    for (Observer p : winners) {
      if (p != player) {
        p.updateObserver(command);
      }
    }
  }

  public Observer setFirstPlayer() {
    Observer firstPlayer;
    Random rand = new Random();
    int i = rand.nextInt(players.size() - 1);
    if (i == -1) {
      return players.get(0);
    }
    firstPlayer = players.get(i);
    return firstPlayer;
  }

  @Override
  public void noticeAboutPlayersTurn(Observer player) {
    player.updateObserver("MOVE");
  }

  public Observer setAnotherPlayer(Observer player) {
    Observer anotherPlayer;
    int i = players.indexOf(player);

    if (i + 1 == players.size()) {
      anotherPlayer = players.get(0);
    } else {
      anotherPlayer = players.get(i + 1);
    }
    return anotherPlayer;
  }

  public void generateGame(int numberOfPlayers) {
    rules = new DefaultRuleSet();
    BoardGenerator generator = null;
    // TODO sprawdzic zmiane
    switch (numberOfPlayers) {
      case 2:
        generator = new PlayerDependentBoardGenerator(2, boardSize);
        players.get(1).setColor(Color.RED);
        players.get(0).setColor(Color.YELLOW);
        break;
      case 3:
        generator = new PlayerDependentBoardGenerator(3, boardSize);
        players.get(2).setColor(Color.GREEN);
        players.get(1).setColor(Color.BLUE);
        players.get(0).setColor(Color.RED);
        break;
      case 4:
        generator = new PlayerDependentBoardGenerator(4, boardSize);
        players.get(3).setColor(Color.RED);
        players.get(2).setColor(Color.ORANGE);
        players.get(1).setColor(Color.YELLOW);
        players.get(0).setColor(Color.BLUE);
        break;
      case 6:
        generator = new PlayerDependentBoardGenerator(6, boardSize);
        players.get(5).setColor(Color.RED);
        players.get(4).setColor(Color.ORANGE);
        players.get(3).setColor(Color.BLACK);
        players.get(2).setColor(Color.YELLOW);
        players.get(1).setColor(Color.BLUE);
        players.get(0).setColor(Color.GREEN);
    }

    if (generator != null) {
      board = generator.generateBoard(rules);
    }
  }
}