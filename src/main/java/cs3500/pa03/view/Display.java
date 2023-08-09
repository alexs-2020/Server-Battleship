package cs3500.pa03.view;

import cs3500.pa03.model.types.BattleBoard;
import cs3500.pa03.model.types.Coord;
import java.util.List;

/**
 * Interface for Display class
 */
public interface Display {
  /**
   * Retrieves user's input for the shots they want to take.
   *
   * @param phrase   the prompt phrase to display
   * @param numShots the number of shots the user is able to take
   * @return a list of coordinates representing the users shots
   */

  List<Coord> getUserShots(String phrase, int numShots);


  void displayUserBoard();

  void displayEnemyBoard();

  /**
   * Displays a string message.
   *
   * @param phrase the message to be displayed
   */

  void displayString(String phrase);
  /**
   * Gets the fleet from the user
   *
   * @param maxSize the maximum size allowed for the fleet
   * @return an array of fleet sizes index representing #[Carrier, Battleship, Destroyer, Submarine]
   */

  int[] getFleet(int maxSize);
  /**
   * Gets users dimension input
   *
   * @return an array of board dimensions
   */

  int[] getBoardSize();
}
