package cs3500.pa03.model.player;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.Ship;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * The AiPlayer class extends the PlayerImp class representing and AI player
 * Implements AI-controlled strategies for taking shots,
 * reporting damage, and tracking successful hits.
 */

public class AiPlayer extends PlayerImp {
  protected GridImp aiBoard;
  protected List<Coord> previousSuccessfulShots = new ArrayList<>();
  protected List<Coord> missedShots = new ArrayList<>();
  protected List<Coord> previousShots = new ArrayList<>();
  private int height;
  private int width;

  public AiPlayer(GridImp aiBoard) {
    super(null);
    this.aiBoard = aiBoard;
  }
  /**
   * greates a grid with the provided dimensions
   *
   * @param x width dimension
   * @param y height dimension
   */

  @Override
  public void initializeBoards(int x, int y) {
    System.out.println("this one");
    height = y;
    width = x;
    this.aiBoard.initializeGrid(x, y);
  }
  /**
   * Generates a list of coordinates representing the shots to be taken by the player.
   * The method determines the shots based on the
   * previous successful shots and the state of the player's ships.
   *
   * @return The list of coordinates representing the shots
   */

  @Override
  public List<Coord> takeShots() {
    List<Coord> shots = new ArrayList<>();
    Random random = new Random();
    Coord newGuess;
    if (previousSuccessfulShots.size() == 0) {
      for (int i = 0; i < playerShips.size(); i++) {
        newGuess = new Coord(random.nextInt(width), random.nextInt(height));
        shots.add(newGuess);
      }
    } else {
      //iterate of succ hits
      while (shots.size() < playerShips.size()) {
        Coord guess;
        int counter = 0;
        do {
          guess = generateGuess();
          counter++;
          if (counter > 15) {
            guess = new Coord(random.nextInt(width), random.nextInt(height));
          }
        } while (shotContain(guess, shots) || check(guess));
        shots.add(guess);
      }
    }
    if (prevContain(shots) ||  hasDoub(shots)) {
      shots = takeShots();
    }
    previousShots.addAll(shots);
    return shots;
  }
  /**
   * Generates a guess based on the previous successful shots
   *
   * @return Coord representing the guess
   */

  public Coord generateGuess() {
    Random random = new Random();
    Coord succShot = previousSuccessfulShots.get(random.nextInt(previousSuccessfulShots.size()));
    int randomGuess = random.nextInt(3) - 1;
    Coord newGuess = new Coord(succShot.getX() + randomGuess, succShot.getY() + randomGuess);
    return newGuess;
  }

  /**
   * Checks if the previous shots contain the coords to add
   *
   * @param coordsToAdd the coords to add
   * @return true if the previous shots contain the coords to add
   */
  public boolean prevContain(List<Coord> coordsToAdd) {
    for (Coord prevShot :      previousShots) {
      for (Coord coordToAdd :      coordsToAdd) {
        //System.out.println(prevShot);
        if (coordToAdd.toString().equals(prevShot.toString())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if the coord to add is valid
   *
   * @param coordToAdd the coord to add
   * @return true if the coord to add is valid
   */
  public boolean check(Coord coordToAdd) {
    if (coordToAdd.getX() < 0 || coordToAdd.getY() < 0) {
      return true;
    }
    for (Coord prevShot : previousShots) {
      if (coordToAdd.toString().equals(prevShot.toString())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the list of shots contains the coord to add
   *
   * @param coordToAdd the coord to add.
   * @param shots the list of shots
   * @return true if the list of shots contains the coord to add
   */

  public boolean shotContain(Coord coordToAdd, List<Coord> shots) {
    for (Coord shot : shots) {
      if (coordToAdd.toString().equals(shot.toString())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the list of coordinates contains duplicates
   *
   * @param list the list of coordinates
   * @return true if the list of coordinates contains duplicates
   */
  public static boolean hasDoub(List<Coord> list) {
    for (int i = 0; i < list.size() - 1; i++) {
      for (int j = i + 1; j < list.size(); j++) {
        if (list.get(i).toString().equals(list.get(j).toString())) {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Processes the opponent's shots on the AI player's ships and reports the damaged coordinates.
   * It also updates the state of the AI players ships if a ship is hit or destroyed.
   *
   * @param opponentShotsOnBoard The list of coordinates representing the opponents shots
   * @return list of coordinates representing successfull hits on Ai player ships.
   */


  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> hits = new ArrayList<>();
    System.out.println(" shots comparing ");
    for (Coord opShots :
        opponentShotsOnBoard) {
      System.out.println(opShots);
    }
    List<Ship> shipsToRemove = new ArrayList<>();
    for (int j = 0; j < playerShips.size(); j++) {
      List<Coord> shipCoords = playerShips.get(j).getCoords();
      for (int i = 0; i < shipCoords.size(); i++) {
        Coord shipCoord = shipCoords.get(i);
        for (Coord shot : opponentShotsOnBoard) {
          if (shipCoord.toString().equals(shot.toString())) {
            System.out.println("They are equal");
            hits.add(shot);
            System.out.println("Deleting " + shipCoord);
            shipCoords.remove(i);
            i--; // Decrement the loop counter to account for the removed coordinate
            break; // Exit the loop after finding a match
          }
        }
      }
      if (shipCoords.isEmpty()) {
        System.out.println("You have destroyed a " + playerShips.get(j).name());
        shipsToRemove.add(playerShips.get(j));
      }
    }
    playerShips.removeAll(shipsToRemove); // Remove destroyed ships
    aiBoard.setHit(hits);
    return hits;
  }



  /**
   * Updates the list of previous successful shots
   *
   * @param shotsThatHitOpponentShips The list of coordinates representing  successful hits
   */

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    previousSuccessfulShots.addAll(shotsThatHitOpponentShips);
  }

  @Override
  public void placeShips(List<Ship> ships) {
    //override to do nothing
  }
}
