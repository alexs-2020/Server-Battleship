package cs3500.pa03.model.player;

import cs3500.pa03.model.board.GridImp;
import cs3500.pa03.model.board.GridInterface;
import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import cs3500.pa03.model.types.GameResult;
import cs3500.pa03.model.types.Ship;
import cs3500.pa03.model.types.ShipType;
import cs3500.pa03.view.DisplayImp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a User player in a game of BattleSalvo.
 */

public class PlayerImp implements Player {
  protected List<Ship> playerShips;
  //  protected DisplayImp view = new DisplayImp();
  protected GridImp userBoard;
  protected Map<ShipType, Integer> uboats;
  protected List<Ship> userBoats;
  private DisplayImp view = new DisplayImp(userBoard, userBoard);
  private Map<ShipType, Integer> fleetFromServer;

  public PlayerImp(GridImp userBoard) {
    this.userBoard = userBoard;
  }

  @Override
  public BattleBoard[][] getGrid() {
    return this.userBoard.getGrid();
  }

  @Override
  public void initializeBoards(int x, int y) {
    this.userBoard.initializeGrid(x, y);
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */

  @Override
  public String name() {
    //using view to keep consistency
    Scanner input = new Scanner(System.in);
    view.displayString("What is your name ");
    if (input.hasNextLine()) {
      return input.nextLine();
    } else {
      throw new IllegalArgumentException("input a valid name");
    }
  }

  /**
   * Checks if a list of coordinates overlaps with the players existing ship coordinates.
   *
   * @param coordsToTest The list of coordinates to test for overlap.
   * @return true if there is an overlap, false otherwise.
   */

  private boolean isOverlap(List<Coord> coordsToTest) {
    for (Ship excistingShips : playerShips) {
      for (Coord excistingShipCoords : excistingShips.getCoords()) {
        for (Coord coordTest : coordsToTest) {
          if (excistingShipCoords.toString().equals(coordTest.toString())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private List<Coord> createCoords(int height, int width, int shipLength) {
    List<Coord> shipCoords = new ArrayList<>();
    int endX;
    int endY;
    int maxStartX;
    int maxStartY;
    int startX;
    int startY;
    Random random = new Random();
    //50% chance to place horizontally or vertically
    int randomInt = random.nextInt(2);
    Coord coordToAdd = null;
    if (randomInt == 1) { // placed horizontally
      maxStartX = width - 1 - shipLength;
      maxStartY = height - 1;
      //get random starting point
      startX = (int) (Math.random() * (maxStartX + 1));
      startY = (int) (Math.random() * (maxStartY + 1));
      shipCoords.add(new Coord(startX, startY));
      // get the ending point
      // get array of coordinates
      endX = startX + shipLength - 1;
      endY = startY;
      while (startX < endX) {
        startX += 1;
        coordToAdd = new Coord(startX, endY);
        shipCoords.add(coordToAdd);
      }
    } else { //vertical placement
      maxStartX = width - 1;
      maxStartY = height - 1 - shipLength;
      startX = (int) (Math.random() * (maxStartX + 1));
      startY = (int) (Math.random() * (maxStartY + 1));
      shipCoords.add(new Coord(startX, startY));
      endX = startX;
      endY = startY + shipLength - 1;
      while (startY < endY) {
        startY += 1;
        coordToAdd = new Coord(endX, startY);
        shipCoords.add(coordToAdd);
      }
    }
    return shipCoords;
  }

  private boolean isVertical(List<Coord> shipCoords) {
    return shipCoords.get(0).getX() == shipCoords.get(1).getX();
  }


  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
     * on the board.
   *
   * @param height the height of the board, range: [6, 15] inclusive
   * @param width the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */

  @Override
  public List<Ship> setup(int width, int height, Map<ShipType, Integer> specifications) {
    playerShips  = new ArrayList<>();
    for (Map.Entry<ShipType, Integer> ship : specifications.entrySet()) {
      ShipType shipType = ship.getKey();
      int quantity = ship.getValue();
      List<Coord>  shipCoords;
      int i = 0;
      int restart = 0;
      while (i < quantity) {
        shipCoords = createCoords(height, width, shipType.getLength());
        if (isOverlap(shipCoords)) {
          restart++;
        } else {
          Ship shipPlaced = new Ship(shipType, shipCoords, isVertical(shipCoords));
          playerShips.add(shipPlaced);
          i++;
          // if over 10 iiterations start over
        }
        //after too many attempts, restart placement
        if (restart >= 20) {
          return setup(width, height, specifications);
        }
      }
    }
    placeShips(playerShips);
    //keep
    return playerShips;
  }

  @Override
  public void placeShips(List<Ship> ships) {
    this.userBoard.userGridSetup(ships);
  }

  //temporary to see user shisps


  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
     * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */

  @Override
  public List<Coord> takeShots() {

    List<Coord>  takeShots = new ArrayList<>();
    takeShots.add(new Coord(5, 5));
    return takeShots;
  }

  @Override
  public int getNumShips() {
    return this.playerShips.size();
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
     * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations
   *     of shots that hit a ship on this board
   */

  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    //check if list of coords overlapps with Ship list
    //update ship list
    List<Coord> hits = new ArrayList<>();
    for (int j = 0; j < playerShips.size(); j++) {
      for (Coord shots : opponentShotsOnBoard) {
        for (int i = 0; i < playerShips.get(j).getCoords().size(); i++) {
          if (playerShips.get(j).getCoords().get(i).toString().equals(shots.toString())) {
            hits.add(shots);
            System.out.println("The enemy hit the Coordinates " + shots);
            playerShips.get(j).deleteCoord(playerShips.get(j).getCoords().get(i));
          }
        }
      }
      //delete ship if all coords destroyed
      if (playerShips.get(j).getCoords().size() == 0) {
        System.out.println(" You have lost a " + playerShips.get(j).name());
        playerShips.remove(playerShips.get(j));
      }
    }
    userBoard.setHit(hits);
    return hits;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */

  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    // display the shots that hit opp
    view.displayString("The following shots hit the enemy!!");
    for (Coord shotsThatHit : shotsThatHitOpponentShips) {
      view.displayString(shotsThatHit.toString());
    }
  }



  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */

  @Override
  public void endGame(GameResult result, String reason) {
    view.displayString(reason);
  }

  @Override
  public void setFleetFromServer(Map<ShipType, Integer> fleetFromServer) {
    this.fleetFromServer = fleetFromServer;
  }


}
